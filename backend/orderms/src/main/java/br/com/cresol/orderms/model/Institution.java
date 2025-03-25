package br.com.cresol.orderms.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="institution")
public class Institution {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@NotBlank(message="O nome não pode esta vazio")
	@Size(min=3, message="O nome de ter no mínimo 3 caracteres")
	@Column(name="name", length = 255, nullable = false)
	private String name;

	@NotBlank(message="O tipo não pode esta vazio")
	@Size(min=3, message="O tipo de ter no mínimo 3 caracteres")
	@Column(name="type", length = 255, nullable = false)
	private String type;

	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime created_at;

	public Institution() {
	}

	public Institution(Integer id, String name, String type, LocalDateTime created_at) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.created_at = created_at;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("name", name)
			.append("type", type)
			.append("created_at", created_at)
			.toString();
	}

	public String getNameAndType() {
		return getName() + " - " + getType();
	}
}
