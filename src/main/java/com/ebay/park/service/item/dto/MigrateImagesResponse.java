package com.ebay.park.service.item.dto;

import java.util.List;

/**
 * Created by gabriel.sideri on 10/1/15.
 */
public class MigrateImagesResponse {

    private List<Long> successImagesId;

    private List<Long> failedImagesId;


    public List<Long> getSuccessImagesId() {
        return successImagesId;
    }

    public void setSuccessImagesId(List<Long> successImagesId) {
        this.successImagesId = successImagesId;
    }

    public List<Long> getFailedImagesId() {
        return failedImagesId;
    }

    public void setFailedImagesId(List<Long> failedImagesId) {
        this.failedImagesId = failedImagesId;
    }
}
