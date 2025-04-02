package br.com.cresol.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cresol.orderms.dto.EventDTO;
import br.com.cresol.orderms.exception.EventDateIncorrectException;
import br.com.cresol.orderms.exception.EventNotFoundException;
import br.com.cresol.orderms.exception.InstitutionNotFoundException;
import br.com.cresol.orderms.exception.LocatationNotFoudException;
import br.com.cresol.orderms.messaging.EventProducer;
import br.com.cresol.orderms.model.Event;
import br.com.cresol.orderms.model.EventStatus;
import br.com.cresol.orderms.model.Location;
import br.com.cresol.orderms.repository.EventRepository;
import br.com.cresol.orderms.repository.InstitutionRepository;
import br.com.cresol.orderms.repository.LocationRepository;

@Service
public class EventService {
	private final EventRepository eventRepository;
	private final InstitutionRepository institutionRepository;
	private final LocationRepository locationRepository;
	private final EventProducer eventProducer;

	public EventService(EventRepository eventRepository, InstitutionRepository institutionRepository, 
			EventProducer eventProducer, LocationRepository locationRepository) {
		this.eventRepository = eventRepository;
		this.institutionRepository = institutionRepository;
		this.eventProducer = eventProducer;
		this.locationRepository = locationRepository;
	}

	public Event addNewEvent(EventDTO event, Integer institution, Integer location) {
		
		if (!institutionRepository.existsById(institution)) {
	 		throw new InstitutionNotFoundException(
	 			"Não existe instituição cadastrada com o código " + institution
	 		);
		}
		
		Location locationRecorded = locationRepository.findById(location)
			.orElseThrow(() -> new LocatationNotFoudException(
				"Não existe local cadastrado com o código " + location
			)
		);

		if (event.getEndDate().isBefore(event.getStartDate())) {
			throw new EventDateIncorrectException("A data final deve ser maior que a data inicial");
		}

		boolean existeConflito = eventRepository.existsByLocationAndStartDateBetweenOrEndDateBetween(
			locationRecorded,
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
			EventStatus.fromCode(
				event.getStartDate(), 
				event.getEndDate()
			),
			locationRecorded
		);

		Event eventSaved = eventRepository.save(newEvent);

		eventProducer.scheduleEventStatus(
			eventSaved.getId(),
			eventSaved.getStartDate(),
			eventSaved.getEndDate()
		);

		return eventSaved;
	}

	public Page<EventDTO> getEvent(Pageable pageable, Integer institution, Integer location) {
		if (!institutionRepository.existsById(institution)) {
	 		throw new InstitutionNotFoundException(
	 			"Não existe instituição cadastrada com o código " + institution
	 		);
		}
		
		if (!locationRepository.existsById(location)) {
			throw new LocatationNotFoudException(
				"Não existe local cadastrado com o código " + location
			);
		}
		
		return eventRepository.findByLocation(location, pageable)
			.map(event -> new EventDTO(event));
	}

	public Event getEventId(Integer institution, Integer location, Integer id) {
		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException(
				"Não existe instituição cadastrada com o código " + institution
			);
		}
		
		if (!locationRepository.existsById(location)) {
			throw new LocatationNotFoudException(
				"Não existe local cadastrado com o código " + location
			);
		}

		Event event = eventRepository.findById(id)
			.orElseThrow(() -> new EventNotFoundException(
				"Não existe evento cadastrado com o código " + id)
			);

		return event;
	}

	public Event updateEvent(EventDTO event, Integer institution, Integer location) {

		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException(
				"Não existe instituição cadastrada com o código " + institution
			);
		}
		
		if (!locationRepository.existsById(location)) {
			throw new LocatationNotFoudException(
				"Não existe local cadastrado com o código " + location
			);
		}

		Event eventRecorded = eventRepository.findById(event.getId())
			.orElseThrow(() -> new EventNotFoundException(
				"Não existe evento cadastrado com o código " + event.getId())
			);

		if (event.getEndDate().isBefore(event.getStartDate())) {
			throw new EventDateIncorrectException("A data final deve ser maior que a data inicial");
		}

		eventRecorded.setName(event.getName());
		eventRecorded.setDescription(event.getDescription());
		eventRecorded.setStartDate(event.getStartDate());
		eventRecorded.setEndDate(event.getEndDate());
		eventRecorded.setActive(
			EventStatus.fromCode(
				event.getStartDate(), 
				event.getEndDate()
			)
		);

		Event eventSaved = eventRepository.save(eventRecorded);

		eventProducer.scheduleEventStatus(
			eventSaved.getId(),
			eventSaved.getStartDate(),
			eventSaved.getEndDate()
		);

		return eventSaved;
	}

	public boolean cancelEvent(Integer institution, Integer location, Integer id) {
		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException(
				"Não existe instituição cadastrada com o código " + institution
			);
		}

		if (!locationRepository.existsById(location)) {
			throw new LocatationNotFoudException(
				"Não existe local cadastrado com o código " + location
			);
		}

		Event eventRecorded = eventRepository.findById(id)
			.orElseThrow(() ->
				new InstitutionNotFoundException("Não existe evento cadastrado com o código " + id)
			);
		
		eventRecorded.setActive(EventStatus.eventCancel());

		eventRepository.save(eventRecorded);
		return true;

	}

	public void updateEventStatus(Integer eventId, Integer status) {
		eventRepository.findById(eventId).ifPresent(
			event -> {
				event.setActive(status);
				eventRepository.save(event);
				System.out.println("Evento inativado: " + eventId);
			}
		);
	}
}
