package com.ebay.park.service.eps.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.dto.MigrateImagesRequest;
import com.ebay.park.service.item.dto.MigrateImagesResponse;
import com.ebay.park.util.EPSUtils;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gabriel.sideri
 */
@Component
public class MigrateProfilesToEPSCmd implements ServiceCommand<MigrateImagesRequest, Void> {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private EPSClient epsClient;
    
    @Autowired
    private EPSUtils EPSUtils;

    private static Logger logger = LoggerFactory.getLogger(MigrateProfilesToEPSCmd.class);

    @Override
    public Void execute(MigrateImagesRequest request) throws ServiceException {

        List<User> users = userDao.getUsersFromIdRange(request.getFromId(), new PageRequest(0, request.getLimit()));

        try {
            MigrateImagesResponse response = uploadUserFromMap(buildUserUploadMap(users));
            logger.info("Profiles Images Migrated: {} with ids: {}", response.getSuccessImagesId().size(), response.getSuccessImagesId());
            logger.info("Profiles with errors : {} with ids: {}", response.getFailedImagesId().size(), response.getFailedImagesId());

        } catch (NoSuchMethodException e) {
            logger.error(
                    "An exception occurred when trying to migrate profiles to the EPS",
                    e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
	protected MigrateImagesResponse uploadUserFromMap(Map<User, Map<String, String>> uploads) throws NoSuchMethodException {

        List<Long> successUsersId = new ArrayList<>();

        List<Long> failedUsersId = new ArrayList<>();

        MigrateImagesResponse response = new MigrateImagesResponse();

        List<User> users = new ArrayList<>();

        for (Map.Entry<User, Map<String, String>> userEntry : uploads.entrySet()) {

            try {
                for (Map.Entry<String, String> urlEntry : userEntry.getValue().entrySet()) {

                    Method getter = userEntry.getKey().getClass().getMethod("get" + urlEntry.getKey());
                    Method setter = userEntry.getKey().getClass().getMethod("set" + urlEntry.getKey(), String.class);
                    String newUrl = upload(EPSUtils.getResourceNameFromUrl((String) getter.invoke(userEntry.getKey())), urlEntry.getValue());
                    if (StringUtils.isBlank(newUrl)) {
                        failedUsersId.add(userEntry.getKey().getId());
                        throw new RuntimeException("Unable to Migrate Profile Image in EPS for User Id: " + userEntry.getKey().getId());
                    }
                    setter.invoke(userEntry.getKey(), newUrl);
                }
                users.add(userEntry.getKey());
            } catch (Exception e) {
                logger.error("Unable to migrate profile picture.", e);
            }

        }
        userDao.save(users);
        successUsersId.addAll((List<Long>) CollectionUtils.collect(users,
                new BeanToPropertyValueTransformer("id")));

        response.setSuccessImagesId(successUsersId);
        response.setFailedImagesId(failedUsersId);

        return response;
    }

    private String upload(String name, String externalUrl) {
        return epsClient.publishFromUrl(name, externalUrl);
    }

    protected Map<User, Map<String, String>> buildUserUploadMap(List<User> users) {

        Map<User, Map<String, String>> uploads = new HashMap<>();

        for (User user : users) {

            Map<String, String> urls = new HashMap<>();

            if (StringUtils.isNotEmpty(user.getPicture())) {
                urls.put("Picture", user.getPicture());
            }

            if (!urls.isEmpty()) {
                uploads.put(user, urls);
            }
        }
        return uploads;
    }

}
