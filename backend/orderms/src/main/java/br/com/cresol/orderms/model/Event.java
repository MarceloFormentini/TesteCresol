package br.com.cresol.orderms.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="name", length=255, nullable=false)
	private String name;

	@Column(name="description", length=255, nullable=false)
	private String description;

	@Column(name="start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name="end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name="active", nullable = false)
	private Boolean active;

	@ManyToOne
	@JoinColumn(name="institution_id")
	private Institution institution;

	public Event() {
	}

	public Event(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Boolean active, Institution institution) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.active = active;
		this.institution = institution;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("name", name)
			.append("description", description)
			.append("startDate)", startDate)
			.append("endDate", endDate)
			.append("active", active)
			.append("institution", institution != null ? institution.getId() : "null")
			.toString();
	}
}
