package com.isc.store.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Couldn't find product with ID: " + id);
    }
}
