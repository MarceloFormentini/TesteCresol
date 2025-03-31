package br.com.cresol.orderms.dto;

import br.com.cresol.orderms.model.TypeInstitution;

public class TypeInstitutionDTO {
	private Integer id;
	private String name;

	public TypeInstitutionDTO() {
	}

	public TypeInstitutionDTO(TypeInstitution typeInstitution) {
		this.id = typeInstitution.getId();
		this.name = typeInstitution.getName();
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
}
