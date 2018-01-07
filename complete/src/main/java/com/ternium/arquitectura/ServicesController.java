package com.ternium.arquitectura;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicesController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


    @RequestMapping("/sendMessageTelegram")
    public String sendMessageTelegram(@RequestParam(value = "mensaje", defaultValue = "Hola") String mensaje) {
        TelegramMessage telegram = new TelegramMessage();
        return telegram.sendTelegramMessage(mensaje);

    }
}
