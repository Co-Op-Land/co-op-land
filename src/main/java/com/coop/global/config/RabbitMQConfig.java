package com.coop.global.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Declarables rabbitMQBindings() {
        Queue queue = new Queue("post.queue", true, false, false); // durable=true
        DirectExchange exchange = new DirectExchange("post.exchange", true, false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("post.saved");
        return new Declarables(queue, exchange, binding);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public Declarables deadLetterQueueBindings() {
        Queue dlq = new Queue("post.dlq", true, false, false);
        DirectExchange dlx = new DirectExchange("post.dlx", true, false);
        Binding dlqBinding = BindingBuilder.bind(dlq).to(dlx).with("post.saved");
        return new Declarables(dlq, dlx, dlqBinding);
    }
}
