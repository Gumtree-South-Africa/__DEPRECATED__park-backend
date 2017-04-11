package com.ebay.park.service.eps.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
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
public class MigrateItemsToEPSCmd implements ServiceCommand<MigrateImagesRequest, Void> {

    @Autowired
    private ItemDao itemDao;
    
    @Autowired
    private EPSClient epsClient;
    
    @Autowired
    private EPSUtils EPSUtils;

    private static Logger logger = LoggerFactory.getLogger(MigrateItemsToEPSCmd.class);

    @Override
    public Void execute(MigrateImagesRequest request) throws ServiceException {

        List<Item> items = itemDao.getItemsFromIdRange(request.getFromId(), new PageRequest(0, request.getLimit()));

        try {

            MigrateImagesResponse response = uploadItemsFromMap(buildItemUploadMap(items));
            logger.info("Items Images Migrated: {} with ids: {}", response.getSuccessImagesId().size(), response.getSuccessImagesId());
            logger.info("Items with errors : {} with ids: {}", response.getFailedImagesId().size(), response.getFailedImagesId());

        } catch (NoSuchMethodException e) {
            logger.error(
                    "An exception occurred when trying to migrate items to the EPS",
                    e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
	protected MigrateImagesResponse uploadItemsFromMap(Map<Item, Map<String, String>> uploads) throws NoSuchMethodException {

        List<Long> successItemsId = new ArrayList<>();

        List<Long> failedItemsId = new ArrayList<>();

        MigrateImagesResponse response = new MigrateImagesResponse();

        List<Item> items = new ArrayList<>();

        for (Map.Entry<Item, Map<String, String>> itemEntry : uploads.entrySet()) {

            try {
                for (Map.Entry<String, String> urlEntry : itemEntry.getValue().entrySet()) {
                        Method getter = itemEntry.getKey().getClass().getMethod("get" + urlEntry.getKey());
                        Method setter = itemEntry.getKey().getClass().getMethod("set" + urlEntry.getKey(), String.class);
                        String newUrl = upload(EPSUtils.getResourceNameFromUrl((String) getter.invoke(itemEntry.getKey())), urlEntry.getValue());

                    if (StringUtils.isBlank(newUrl)) {
                        failedItemsId.add(itemEntry.getKey().getId());
                        throw new RuntimeException("Unable to Migrate Item Image in EPS for Item Id: " + itemEntry.getKey().getId());
                    }
                        setter.invoke(itemEntry.getKey(), newUrl);
                }

                items.add(itemEntry.getKey());

            } catch (Exception e) {
                logger.error("Unable to migrate item picture.", e);
            }
        }

        itemDao.save(items);
        successItemsId.addAll((List<Long>) CollectionUtils.collect(items,
                new BeanToPropertyValueTransformer("id")));

        response.setSuccessImagesId(successItemsId);
        response.setFailedImagesId(failedItemsId);

        return response;
    }

    private String upload(String name, String externalUrl) {
        return epsClient.publishFromUrl(name, externalUrl);
    }


    protected Map<Item, Map<String, String>> buildItemUploadMap(List<Item> items) {
        Map<Item, Map<String, String>> uploads = new HashMap<>();
        for (Item item : items) {
            Map<String, String> urls = new HashMap<>();
            if (StringUtils.isNotEmpty(item.getPicture1Url())) {
                urls.put("Picture1Url", item.getPicture1Url());
            }
            if (StringUtils.isNotEmpty(item.getPicture2Url())) {
                urls.put("Picture2Url", item.getPicture2Url());
            }
            if (StringUtils.isNotEmpty(item.getPicture3Url())) {
                urls.put("Picture3Url", item.getPicture3Url());
            }
            if (StringUtils.isNotEmpty(item.getPicture4Url())) {
                urls.put("Picture4Url", item.getPicture4Url());
            }

            if (!urls.isEmpty()) {
                uploads.put(item, urls);
            }
        }
        return uploads;
    }

}
