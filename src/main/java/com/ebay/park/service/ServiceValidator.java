package com.ebay.park.service;


/**
 * A basic validator
 *
 * @param <T> The underlying object to be validated
 */
public interface ServiceValidator<T>{

    /**
     * Validates the given object
     *
     * @param toValidate The object to be validated
     */
    void validate(T toValidate);
}