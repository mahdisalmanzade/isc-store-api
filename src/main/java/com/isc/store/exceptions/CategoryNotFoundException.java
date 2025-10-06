package com.isc.store.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Byte categoryId) {
        super("Couldn't find category with ID: " + categoryId);
    }
}
