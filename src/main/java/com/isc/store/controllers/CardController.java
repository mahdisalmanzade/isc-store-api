package com.isc.store.controllers;

import com.isc.store.dtos.CartDto;
import com.isc.store.entities.Cart;
import com.isc.store.mappers.CartMapper;
import com.isc.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CardController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.mapToDto(cart);

        uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);


    }
}
