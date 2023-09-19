package ru.alekseenko.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.alekseenko.RabbitQueue.*;

@Configuration
public class RabbitConfigurationJava {
    @Bean
    public MessageConverter jsoneMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
