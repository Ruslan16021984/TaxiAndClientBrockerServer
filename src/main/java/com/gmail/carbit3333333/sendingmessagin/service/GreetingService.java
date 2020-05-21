package com.gmail.carbit3333333.sendingmessagin.service;

import com.gmail.carbit3333333.sendingmessagin.model.TaxiClient;
import com.gmail.carbit3333333.sendingmessagin.model.TaxiWorker;

import java.util.Queue;

public interface GreetingService {
    void addTaxiWorker(TaxiWorker taxiWorker, String uuId);
    void getCoordinatTaxi(TaxiWorker taxiWorker);
    void deleteTaxiWorker(String login);
    void requestClient(TaxiClient taxiClient);
    void deleteSomeUser();
    void getTaxiWorkersQueue();

}
