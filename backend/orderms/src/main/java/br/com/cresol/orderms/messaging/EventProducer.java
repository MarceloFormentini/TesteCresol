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
import br.com.cresol.orderms.model.EventStatus;

@Service
public class EventProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper(); // Converte objetos para JSON

	public EventProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

//	public void scheduleEventInactivation(Integer eventId, LocalDateTime dataFim) {
//        // Calcula o TTL em milissegundos
//        long ttlMillis = Duration.between(LocalDateTime.now(), dataFim).toMillis();
//        if (ttlMillis <= 0) {
//            System.out.println("Evento já está vencido. Inativação imediata.");
//            return;
//        }
//
//        try {
//            Map<String, Object> eventData = new HashMap<>();
//            eventData.put("eventId", eventId);
//
//            // Converte o objeto para JSON corretamente
//            String jsonMessage = objectMapper.writeValueAsString(eventData);
//
//            MessageProperties properties = new MessageProperties();
//            properties.setContentType("application/json"); // Define o tipo correto
//            properties.setExpiration(String.valueOf(ttlMillis)); // Define o TTL
//            Message message = new Message(jsonMessage.getBytes(), properties);
//
//            System.out.println("Publicando evento com TTL de " + ttlMillis + "ms para inativação: " + eventId);
//            rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_SCHEDULED_QUEUE, message);
//        } catch (Exception e) {
//            System.err.println("Erro ao converter mensagem para JSON: " + e.getMessage());
//        }
//    }
	
	public void scheduleEventStatus(Integer eventId, LocalDateTime startDate, LocalDateTime endDate) {
        sendMessage(eventId, startDate, EventStatus.EM_ANDAMENTO.getCode()); // Atualiza para "Em andamento"
        sendMessage(eventId, endDate, EventStatus.FINALIZADO.getCode()); // Atualiza para "Finalizado"
    }

    private void sendMessage(Integer eventId, LocalDateTime triggerTime, Integer status) {
        long delayMillis = Duration.between(LocalDateTime.now(), triggerTime).toMillis();
        if (delayMillis <= 0) {
            System.out.println("Evento já atingiu o status " + status + ". Atualizando imediatamente.");
//            eventService.updateEventStatus(eventId, status);
            return;
        }

        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", eventId);
            eventData.put("status", status.toString());

            String jsonMessage = objectMapper.writeValueAsString(eventData);

            MessageProperties properties = new MessageProperties();
            properties.setContentType("application/json");
            properties.setExpiration(String.valueOf(delayMillis));

            Message message = new Message(jsonMessage.getBytes(), properties);

            System.out.println("Agendando atualização para status " + status + " em " + delayMillis + "ms. Evento ID: " + eventId);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_SCHEDULED_QUEUE, message);
        } catch (Exception e) {
            System.err.println("Erro ao converter mensagem para JSON: " + e.getMessage());
        }
    }
}
