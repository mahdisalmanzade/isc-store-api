package com.isc.store.controllers;

import com.isc.store.dtos.CartDto;
import com.isc.store.services.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CardController {

    private final CardService cardService;
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var createdCart = cardService.createCart();

        uriComponentsBuilder.path("/carts/{id}").buildAndExpand(createdCart.getId()).toUri();

        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }
}
