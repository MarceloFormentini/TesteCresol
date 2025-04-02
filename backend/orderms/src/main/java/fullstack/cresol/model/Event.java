package fullstack.cresol.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime startDate;

	@Column(name="end_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime endDate;

	@Column(name="active", nullable = false)
	private Integer active;
	
	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime created_at;

	@ManyToOne
	@JoinColumn(name="location_id")
	private Location location;

	public Event() {
	}

	public Event(String name, String description, LocalDateTime startDate, LocalDateTime endDate, 
			Integer active, Location location) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.active = active;
		this.location = location;
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

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}
	
	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
			.append("created_at", created_at)
			.append("location", location != null ? location.getId() : "null")
			.toString();
	}
}
