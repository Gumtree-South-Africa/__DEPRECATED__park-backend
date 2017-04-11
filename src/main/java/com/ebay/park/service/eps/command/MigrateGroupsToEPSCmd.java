package com.ebay.park.service.eps.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.entity.Group;
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
public class MigrateGroupsToEPSCmd implements ServiceCommand<MigrateImagesRequest, Void> {

    @Autowired
    private GroupDao groupDao;
    
    @Autowired
    private EPSClient epsClient;
    
    @Autowired
    private EPSUtils EPSUtils;

    private static Logger logger = LoggerFactory.getLogger(MigrateGroupsToEPSCmd.class);

    @Override
    public Void execute(MigrateImagesRequest request) throws ServiceException {

        List<Group> groups = groupDao.getGroupsFromIdRange(request.getFromId(),new PageRequest(0, request.getLimit()));

        try {
            MigrateImagesResponse response = uploadGroupFromMap(buildGroupUploadMap(groups));
            logger.info("Groups Images Migrated: {} with ids: {}", response.getSuccessImagesId().size(), response.getSuccessImagesId());
            logger.info("Groups with errors : {} with ids: {}", response.getFailedImagesId().size(), response.getFailedImagesId());
        } catch (NoSuchMethodException e) {
            logger.error(
                    "An exception occurred when trying to migrate groups to the EPS",
                    e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
	protected MigrateImagesResponse uploadGroupFromMap(Map<Group, Map<String, String>> uploads) throws NoSuchMethodException {

        List<Long> successGroupsId = new ArrayList<>();

        List<Long> failedGroupsId = new ArrayList<>();

        MigrateImagesResponse response = new MigrateImagesResponse();

        List<Group> groups = new ArrayList<>();
        for (Map.Entry<Group, Map<String, String>> groupEntry : uploads.entrySet()) {

            try {

                for (Map.Entry<String, String> urlEntry : groupEntry.getValue().entrySet()) {
                    Method getter = groupEntry.getKey().getClass().getMethod("get" + urlEntry.getKey());
                    Method setter = groupEntry.getKey().getClass().getMethod("set" + urlEntry.getKey(), String.class);
                    String newUrl = upload(EPSUtils.getResourceNameFromUrl((String) getter.invoke(groupEntry.getKey())), urlEntry.getValue());

                    if (StringUtils.isBlank(newUrl)) {
                        failedGroupsId.add(groupEntry.getKey().getId());
                        throw new RuntimeException("Unable to Migrate Group Image in EPS for Group Id: " + groupEntry.getKey().getId());
                    }

                    setter.invoke(groupEntry.getKey(), newUrl);
                }

                groups.add(groupEntry.getKey());

            } catch (Exception e) {
                logger.error("Unable to migrate groups picture.", e);
            }
        }
        groupDao.save(groups);
        successGroupsId.addAll((List<Long>) CollectionUtils.collect(groups,
                new BeanToPropertyValueTransformer("id")));

        response.setSuccessImagesId(successGroupsId);
        response.setFailedImagesId(failedGroupsId);

        return response;
    }

    private String upload(String name, String externalUrl) {
        return epsClient.publishFromUrl(name, externalUrl);
    }

    protected Map<Group, Map<String, String>> buildGroupUploadMap(List<Group> groups) {
        Map<Group, Map<String, String>> uploads = new HashMap<>();
        for (Group group : groups) {

            Map<String, String> urls = new HashMap<>();

            if (StringUtils.isNotEmpty(group.getPicture())) {
                urls.put("Picture", group.getPicture());
            }

            if (!urls.isEmpty()) {
                uploads.put(group, urls);
            }
        }
        return uploads;
    }

}
