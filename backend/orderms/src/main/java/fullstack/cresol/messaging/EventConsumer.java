package fullstack.cresol.messaging;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fullstack.cresol.config.RabbitMQConfig;
import fullstack.cresol.service.EventService;

@Component
public class EventConsumer {

	private final EventService eventService;
	private final ObjectMapper objectMapper = new ObjectMapper(); // Conversor JSON

	public EventConsumer(EventService eventService) {
		this.eventService = eventService;
	}
	
//	@RabbitListener(queues = RabbitMQConfig.EVENT_PROCESSING_QUEUE)
//    public void processInactivationEvent(String messageJson) {
//        try {
//            // Converte a string JSON para um Map
//            Map<String, Object> message = objectMapper.readValue(messageJson, Map.class);
//            Integer eventId = (Integer) message.get("eventId");
//
//            if (eventId != null) {
//                System.out.println("Inativando evento ID: " + eventId);
//                eventService.inactivateEvent(eventId);
//            } else {
//                System.err.println("ID do evento ausente na mensagem.");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro ao processar mensagem do RabbitMQ: " + e.getMessage());
//        }
//    }
	
	 @RabbitListener(queues = RabbitMQConfig.EVENT_PROCESSING_QUEUE, concurrency = "5") // Suporte a 5 processos simultâneos
	    public void processEventStatusUpdate(String messageJson) {
	        try {
	            // Converte JSON para Map
	            Map<String, Object> message = objectMapper.readValue(messageJson, Map.class);
	            Integer eventId = (Integer) message.get("eventId");
	            String status = (String) message.get("status");

	            if (eventId != null && status != null) {
	                System.out.println("Atualizando evento ID: " + eventId + " para status: " + status);
	                eventService.updateEventStatus(eventId, Integer.parseInt(status));
	            } else {
	                System.err.println("ID do evento ou status ausente na mensagem.");
	            }
	        } catch (Exception e) {
	            System.err.println("Erro ao processar mensagem do RabbitMQ: " + e.getMessage());
	        }
	    }
}
