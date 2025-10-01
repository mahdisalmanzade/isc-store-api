package com.isc.store.mappers;

import com.isc.store.dtos.CartDto;
import com.isc.store.dtos.CartItemDto;
import com.isc.store.entities.Cart;
import com.isc.store.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto mapToDto(Cart cart);

    CartItemDto toDto(CartItem cartItem);

}
