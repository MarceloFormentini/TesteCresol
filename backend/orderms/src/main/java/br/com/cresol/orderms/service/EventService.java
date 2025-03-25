package br.com.cresol.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cresol.orderms.dto.EventDTO;
import br.com.cresol.orderms.exception.EventDateIncorrectException;
import br.com.cresol.orderms.exception.EventInstitutionIncompatibleException;
import br.com.cresol.orderms.exception.EventNotFoundException;
import br.com.cresol.orderms.exception.InstitutionNotFoundException;
import br.com.cresol.orderms.messaging.EventProducer;
import br.com.cresol.orderms.model.Event;
import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.repository.EventRepository;
import br.com.cresol.orderms.repository.InstitutionRepository;

@Service
public class EventService {
	private final EventRepository eventRepository;
	private final InstitutionRepository institutionRepository;
	private final EventProducer eventProducer;

	public EventService(EventRepository eventRepository, InstitutionRepository institutionRepository, EventProducer eventProducer) {
		this.eventRepository = eventRepository;
		this.institutionRepository = institutionRepository;
		this.eventProducer = eventProducer;
	}

	public Event addNewEvent(Integer institution, EventDTO event) {
		
		Institution institutionRecorded = institutionRepository.findById(institution)
	 			.orElseThrow(() -> new InstitutionNotFoundException(
	 				"Não existe instituição cadastrada com o código " + institution)
	 			);

		if (event.getEndDate().isBefore(event.getStartDate())) {
			throw new EventDateIncorrectException("A data final deve ser maior que a data inicial");
		}

		boolean existeConflito = eventRepository.existsByInstitutionAndStartDateBetweenOrEndDateBetween(
			institutionRecorded,
			event.getStartDate(),
			event.getEndDate()
		);

		if (existeConflito) {
			 throw new EventDateIncorrectException("Já existe um evento agendado para o mesmo dia e horário");
		 }

		Event newEvent = new Event(
			event.getName(),
			event.getDescription(),
			event.getStartDate(), 
			event.getEndDate(),
			true,
			institutionRecorded
		);

		Event eventSaved = eventRepository.save(newEvent);

		eventProducer.scheduleEventInactivation(eventSaved.getId(), eventSaved.getEndDate());

		return eventSaved;
	}

	public Page<EventDTO> getEvent(Pageable pageable, Integer institution) {
		 return eventRepository.findByInstitution(institution, pageable)
			.map(event -> new EventDTO(event));
	}

	public Event getEventId(Integer institution, Integer id) {
		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + institution);
		}

		Event event = eventRepository.findById(id)
			.orElseThrow(() -> new EventNotFoundException(
				"Não existe evento cadastrado com o código " + id)
			);

		return event;
	}

	public Event updateEvent(Integer institution, EventDTO event) {

		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + institution);
		}

		Event eventRecorded = eventRepository.findById(event.getId())
			.orElseThrow(() -> new EventNotFoundException(
				"Não existe evento cadastrado com o código " + event.getId())
			);

		if (!eventRecorded.getInstitution().getId().equals(institution)) {
			throw new EventInstitutionIncompatibleException(
				"O evento não pertence a instituição especificada"
			);
		}

		if (event.getEndDate().isBefore(event.getStartDate())) {
			throw new EventDateIncorrectException("A data final deve ser maior que a data inicial");
		}

		eventRecorded.setName(event.getName());
		eventRecorded.setDescription(event.getDescription());
		eventRecorded.setStartDate(event.getStartDate());
		eventRecorded.setEndDate(event.getEndDate());

		Event eventSaved = eventRepository.save(eventRecorded);

		eventProducer.scheduleEventInactivation(eventSaved.getId(), eventSaved.getEndDate());

		return eventSaved;
	}

	public boolean removeEvent(Integer id) {
		if (!eventRepository.existsById(id)) {
			throw new InstitutionNotFoundException("Não existe evento cadastrado com o código " + id);
		}

		eventRepository.deleteById(id);
		return true;

	}

	public void inactivateEvent(Integer eventId) {
		eventRepository.findById(eventId).ifPresent(
			event -> {
				event.setActive(false);
				eventRepository.save(event);
				System.out.println("Evento inativado: " + eventId);
			}
		);
	}
}
