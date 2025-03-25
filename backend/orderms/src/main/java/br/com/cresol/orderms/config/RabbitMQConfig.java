package br.com.cresol.orderms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EVENT_SCHEDULED_QUEUE = "event_scheduled_queue";
	public static final String EVENT_PROCESSING_QUEUE = "event_processing_queue";
	public static final String EVENT_EXCHANGE = "event_exchange";
    public static final String EVENT_ROUTING_KEY = "event_routing_key";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EVENT_EXCHANGE);
    }

    @Bean
    public Queue scheduledQueue() {
        return QueueBuilder.durable(EVENT_SCHEDULED_QUEUE)
                .withArgument("x-dead-letter-exchange", EVENT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EVENT_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue processingQueue() {
        return QueueBuilder.durable(EVENT_PROCESSING_QUEUE).build();
    }

    @Bean
    public Binding bindingProcessingQueue() {
        return BindingBuilder.bind(processingQueue()).to(exchange()).with(EVENT_ROUTING_KEY);
    }
 
}
