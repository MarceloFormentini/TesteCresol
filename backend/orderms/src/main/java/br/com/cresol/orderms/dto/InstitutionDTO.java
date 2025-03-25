package br.com.cresol.orderms.dto;

import br.com.cresol.orderms.model.Institution;

public class InstitutionDTO {
	private Integer id;
	private String name;
	private String type;

	public InstitutionDTO() {
	}

	public InstitutionDTO(Institution institution) {
		this.id = institution.getId();
		this.name = institution.getName();
		this.type = institution.getType();
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

	public void setName(String nome) {
		this.name = nome;
	}

	public String getType() {
		return type;
	}

	public void setType(String tipo) {
		this.type = tipo;
	}

}
