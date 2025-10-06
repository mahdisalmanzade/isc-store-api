package com.isc.store.services;

import com.isc.store.dtos.CartDto;
import com.isc.store.entities.Cart;
import com.isc.store.mappers.CartMapper;
import com.isc.store.repositories.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;


    public CardService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.mapToDto(cart);

        return cartDto;


    }
}
