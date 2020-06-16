package com.gmail.carbit3333333.sendingmessagin.controller;

import com.gmail.carbit3333333.sendingmessagin.model.TaxiClient;
import com.gmail.carbit3333333.sendingmessagin.model.TaxiWorker;
import com.gmail.carbit3333333.sendingmessagin.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class GreetingController {
    @Autowired
    private final GreetingService greetingService;

    GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
    //сюда прилетает запрос от клиента с его данными
    @MessageMapping("/requestTaxi")
    public void requestClient(TaxiClient taxiClient, Principal principal, SimpMessageHeaderAccessor headerAccessor){
        taxiClient.setUuId(principal.getName());
        greetingService.requestClient(taxiClient);
    }
    // здесь добавляется в очередь таксист
    @MessageMapping("/addTaxiWorkerQueue")
    public void addTaxiWorker(TaxiWorker taxiWorker, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        taxiWorker.setUuId(principal.getName());
        greetingService.addTaxiWorker(taxiWorker, taxiWorker.getUuId());
        log.info("" + taxiWorker.getLogin());
        headerAccessor.getSessionAttributes().put("username", taxiWorker.getPhone());
    }
    //здесь присылаются постоянно координаты водителя
    @MessageMapping("/sendCoordinatTaxi")
    public void watchTaxi(TaxiWorker taxiWorker, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        taxiWorker.setUuId(principal.getName());
        greetingService.getCoordinatTaxi(taxiWorker);
    }

}
