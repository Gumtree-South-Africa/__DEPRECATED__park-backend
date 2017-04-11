package com.ebay.park.service.item.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.dto.DeletePhotoRequest;

@Component
public class DeletePhotoValidator implements ServiceValidator<DeletePhotoRequest>{

    private static final long FIRST_PICTURE = 1l;
    @Value("${createItem.maxAmountPictures}")
    private Integer max;

    @Override
    public void validate(DeletePhotoRequest request) {
        if (request.getPictureIdList().contains(FIRST_PICTURE)) {
            throw createServiceException(ServiceExceptionCode.CANNOT_DELETE_PICTURE);
        }

        Optional<Long> optMax = request.getPictureIdList().stream().max(Long::compare);
        Optional<Long> optMin = request.getPictureIdList().stream().min(Long::compare);
        if (
                (optMax.isPresent() && optMax.get() > max)
                ||
                (optMin.isPresent() && optMin.get() < FIRST_PICTURE)) {
            throw createServiceException(ServiceExceptionCode.INVALID_PICTURE_ID);
        }
    }

}
