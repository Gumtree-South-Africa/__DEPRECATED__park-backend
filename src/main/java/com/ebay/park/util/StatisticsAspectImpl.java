package com.ebay.park.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Arrays;



@Aspect
@Configuration
public class StatisticsAspectImpl implements StatisticsAspect {

    private static final String NOT_LOGGED = "NOT_LOGGED";
    private final Log logger = LogFactory.getLog(StatisticsAspectImpl.class);
    
	public StatisticsAspectImpl() {
		logger.info("Creating statistic aspect - No arguments");
	}

    private static void generateMethodName(StringBuilder sb, ProceedingJoinPoint pjp, String args) {
        sb.append(pjp.getSignature().getName());
        sb.append('(');
        sb.append(args);
        sb.append(')');
    }
    
    private String generateArgsString(ProceedingJoinPoint pjp) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        Object [] args = pjp.getArgs();
        if (pjp.getSignature() instanceof MethodSignature) {
            MethodSignature sig = (MethodSignature) pjp.getSignature();
            Annotation[][] parameterAnnotations = sig.getMethod().getParameterAnnotations();
            if (args.length != parameterAnnotations.length) {
                logger.warn("Args length is not the same as declared method! NOT NORMAL:" + Arrays.toString(args) +"/"+Arrays.toString(parameterAnnotations)+"/"+pjp.getSignature());
                
            } else {
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] annotations = parameterAnnotations[i];
                    boolean doNotLog = false;
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof DoNotLog) {
                            doNotLog = true;        
                        }
                    }
                    if (doNotLog) {
                        sb.append(NOT_LOGGED);
                    } else {
                        sb.append(args[i]);
                    }
                    if (i < args.length -1) {
                        sb.append(',');
                    }
                }
            }
            
        } else {
            sb.append(Arrays.toString(args));
        }
        sb.append(')');
        return sb.toString();
    }
    
    @Override
	@Around("(@target(org.springframework.web.bind.annotation.RestController)  "
			+ " ||  @target(org.springframework.stereotype.Controller))  "
			+ "&& @annotation(org.springframework.web.bind.annotation.RequestMapping) "
            + "&& !@annotation(com.ebay.park.util.DoNotLog) ")
    public Object logCall(ProceedingJoinPoint pjp) throws Throwable {
       StringBuilder sb = new StringBuilder();
       String args = null;
       long time = System.currentTimeMillis();
       if (logger.isDebugEnabled()) {
           args = generateArgsString(pjp);
           generateMethodName(sb, pjp, args);
           int length = sb.length();
           sb.append(" called");
           logger.debug(sb); // logging as debug to avoid being verbose...
           sb.setLength(length);
       }
       try {
           // let's continue...
           return pjp.proceed();
           // and then continue logging
       } finally {
           time = System.currentTimeMillis() - time;
           if (args == null) {
               // it means DEBUG was not enables and nothing was logged yet
               args = generateArgsString(pjp);
               generateMethodName(sb, pjp,args);
           }
           if (time > 5000) {
               // WARN
               sb.append(" took too long : " );
               sb.append(time);
               sb.append(" ms");
               logger.warn(sb.toString());
           } else {
               // INFO
               sb.append(" took " );
               sb.append(time);
               sb.append(" ms");
               logger.info(sb.toString());
           }
       }
    }

}
