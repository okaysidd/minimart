package com.minimart.ordersvc.controllers;

import com.minimart.ordersvc.models.OrderRequest;
import com.minimart.ordersvc.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void create(@RequestBody OrderRequest request) {
        orderService.placeOrderForInventoryCheck(request);
    }
}
