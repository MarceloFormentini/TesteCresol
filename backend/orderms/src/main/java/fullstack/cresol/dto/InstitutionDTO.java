package fullstack.cresol.dto;

import fullstack.cresol.model.Institution;
import fullstack.cresol.model.TypeInstitution;

public class InstitutionDTO {
	private Integer id;
	private String name;
	private TypeInstitution typeInstitution;

	public InstitutionDTO() {
	}

	public InstitutionDTO(Institution institution) {
		this.id = institution.getId();
		this.name = institution.getName();
		this.typeInstitution = institution.getTypeInstitution();
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

	public TypeInstitution getTypeInstitution() {
		return typeInstitution;
	}

	public void setTypeInstitution(TypeInstitution typeInstitution) {
		this.typeInstitution = typeInstitution;
	}

}
