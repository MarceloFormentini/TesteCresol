package br.com.cresol.orderms.dto;

import java.security.DrbgParameters.Instantiation;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import br.com.cresol.orderms.config.LocalDateTimeDeserializer;
import br.com.cresol.orderms.config.LocalDateTimeSerializer;
import br.com.cresol.orderms.model.Event;
import br.com.cresol.orderms.model.Institution;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventDTO {

	private Integer id;
	private Boolean active;
	private Institution institution;

	@NotBlank(message="O nome não pod estar vazio")
	@Size(min=3, message="O nome deve ter no mínimo 3 caracteres")
	private String name;

	@NotBlank(message="A descrição não pode estar vazia")
	@Size(min=3, message="A descrição deve ter no mínimo 3 caracteres")
	private String description;

	@NotNull(message="A data de início não pode ser nula")
	@FutureOrPresent(message="A data de início deve ser maior/igual a data atual")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime startDate;

	@NotNull(message="A data de término não pode ser nula")
	@FutureOrPresent(message="A data de término deve ser maior/igual a data atual")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime endDate;

	public EventDTO() {
	}

	public EventDTO(Event event) {
		this.id = event.getId();
		this.name = event.getName();
		this.description = event.getDescription();
		this.startDate = event.getStartDate();
		this.endDate = event.getEndDate();
		this.active = event.getActive();
		this.institution = event.getInstitution();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

}
