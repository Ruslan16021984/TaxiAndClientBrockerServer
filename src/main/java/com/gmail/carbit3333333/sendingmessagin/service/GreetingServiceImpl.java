package com.gmail.carbit3333333.sendingmessagin.service;

import com.gmail.carbit3333333.sendingmessagin.model.TaxiClient;
import com.gmail.carbit3333333.sendingmessagin.model.TaxiWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GreetingServiceImpl implements GreetingService {

    private static final String TAG = "TAG";
    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/taxiOnline/greetings";
    private static final String WS_MESSAGE_RQUEST_CLIENT_DESTINATION = "/requestclient/greetings";
    //эта константа нужна для отправки позиции в очереди
    private static final String WS_MESSAGE_ORDER_DESTINATION = "/queue/greetings";
    private Map<String, String> userNamesMap = new HashMap<>();
    private Queue<String> taxiWorkersQueue = new LinkedList<>();

    GreetingServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    //добавление водителя в конец очереди Queue и общий список Map
    @Override
    public void addTaxiWorker(TaxiWorker taxiWorker, String uuId) {
        userNamesMap.put(taxiWorker.getPhone(), uuId);
        taxiWorkersQueue.add(taxiWorker.getPhone());
        log.info("taxiWorkersQueue: " + taxiWorkersQueue.element());
        log.info("userNamesMap: " + userNamesMap.toString());
        //просто проверил связь с пользователем кто-бы он нибыл
        //simpMessagingTemplate.convertAndSendToUser(uuId, WS_MESSAGE_TRANSFER_DESTINATION, taxiWorker);
    }

    //метод обрабатывает запрос от клиента и отправляет первому в очереди таксисту taxiWorkersQueue.peek()
    //todo:изменить AddressOrder на клиент
    @Override
    public void requestClient(TaxiClient taxiClient) {
        log.info("requestClient______----===>>" + taxiClient.getUuId());
        userNamesMap.put(taxiClient.getPhone(), taxiClient.getUuId());
        String taxiWorkerPhone = taxiWorkersQueue.peek();
        String taxiUuId = userNamesMap.get(taxiWorkerPhone);
        simpMessagingTemplate.convertAndSendToUser(taxiUuId, WS_MESSAGE_RQUEST_CLIENT_DESTINATION, taxiClient);
    }


    //метод получения координат от водителя и отправки определенному клиенту
    @Override
    public void getCoordinatTaxi(TaxiWorker taxiWorker) {
        log.info("Получил координаты" + taxiWorker.getUuId() + " ___ "+ taxiWorker.getClientPhone());
        String clientUuid = userNamesMap.get(taxiWorker.getClientPhone());
        simpMessagingTemplate.convertAndSendToUser(clientUuid, WS_MESSAGE_TRANSFER_DESTINATION, taxiWorker);
    }

    //удаление по логину водителя из очереди Queue и общей Map
    @Override
    public void deleteTaxiWorker(String login) {
        taxiWorkersQueue.remove(login);
        userNamesMap.remove(login);
        log.info("deleteTaxiWorker " + login);
        log.info("taxiWorkersQueue " + taxiWorkersQueue.size());
        log.info("userNamesMap " + userNamesMap.toString());
        //здесь дальше внизу сразу вытягиваем из очереди taxiWorkersQueue и при наличии заказов отправляем запрос таксисту
    }

    //здесь происходит пробежка в течении 1 сек
    // по очереди и отстылка позиции каждому таксисту
    public void getTaxiWorkersQueue() {
        int x = 0;
        if (taxiWorkersQueue.size() != 0) {
            for (String taxiWorker : taxiWorkersQueue) {
                String uuId = userNamesMap.get(taxiWorker);
                x = x + 1;
                log.info("" + x);
                String orderTaxi = String.valueOf(x);
                simpMessagingTemplate.convertAndSendToUser(uuId, WS_MESSAGE_ORDER_DESTINATION, orderTaxi);

            }
        }
    }

}
