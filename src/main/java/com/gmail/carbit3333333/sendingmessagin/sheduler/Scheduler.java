package com.gmail.carbit3333333.sendingmessagin.sheduler;

import com.gmail.carbit3333333.sendingmessagin.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//Это планировщик , выполняющий отправку каждую секунду положение в
// очереди каждому таксисту
@Slf4j
@Component
public class Scheduler {

    @Autowired
    private final GreetingService greetingService;

    public Scheduler(GreetingService greetingServiceImpl) {
        this.greetingService = greetingServiceImpl;
    }
    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void schedulingTask() {
        greetingService.getTaxiWorkersQueue();
    }
}
