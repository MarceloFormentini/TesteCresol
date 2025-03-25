package br.com.cresol.orderms.messaging;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cresol.orderms.config.RabbitMQConfig;

@Service
public class EventProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper(); // Converte objetos para JSON

	public EventProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

//	public void sendInactivateEvent(Integer eventId, LocalDateTime endDate) {
//		long delay = Duration.between(LocalDateTime.now(), endDate).toMillis();
//
//		if (delay <= 0) {
//			System.out.println("A data de fim já passou, inativando imediatamente.");
////			rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_INACTIVATION_QUEUE, eventId);
//			return;
//		}
//
//		Map<String, Object> eventData = new HashMap<>();
//		eventData.put("eventId", eventId);
//		
//		MessageProperties properties = new MessageProperties();
//		properties.setExpiration(String.valueOf(delay)); // Define o TTL
//		Message message = new Message(eventData.toString().getBytes(), properties);
//
//		rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_INACTIVATION_QUEUE, message);
//		System.out.println("Publicou mensagem com o código: " + eventId + " para ser executada em " + delay);
//	}
	
	public void scheduleEventInactivation(Integer eventId, LocalDateTime dataFim) {
        // Calcula o TTL em milissegundos
        long ttlMillis = Duration.between(LocalDateTime.now(), dataFim).toMillis();
        if (ttlMillis <= 0) {
            System.out.println("Evento já está vencido. Inativação imediata.");
            return;
        }

        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", eventId);

            // Converte o objeto para JSON corretamente
            String jsonMessage = objectMapper.writeValueAsString(eventData);

            MessageProperties properties = new MessageProperties();
            properties.setContentType("application/json"); // Define o tipo correto
            properties.setExpiration(String.valueOf(ttlMillis)); // Define o TTL
            Message message = new Message(jsonMessage.getBytes(), properties);

            System.out.println("Publicando evento com TTL de " + ttlMillis + "ms para inativação: " + eventId);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_SCHEDULED_QUEUE, message);
        } catch (Exception e) {
            System.err.println("Erro ao converter mensagem para JSON: " + e.getMessage());
        }
    }
}
