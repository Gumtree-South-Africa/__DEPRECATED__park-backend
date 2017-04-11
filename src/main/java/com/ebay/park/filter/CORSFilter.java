/*
 * Copyright eBay, 2014
 */
package com.ebay.park.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

/**
 * @author jpizarro, gamy
 * 
 */
@Component
public class CORSFilter implements Filter {

	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest)req;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, token, Content-Type, Accept, Origin, Accept-Language, platform, apiVersion");

		// if this is an OPTIONS preflight so the chain ENDS HERE. Let's get out!
        if (!HttpMethod.OPTIONS.equals(request.getMethod())) {
            chain.doFilter(req, res);
        }
	}

	@Override
    public void init(FilterConfig filterConfig) {
	}

	@Override
    public void destroy() {
	}

}