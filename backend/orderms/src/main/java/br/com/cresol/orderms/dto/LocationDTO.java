package br.com.cresol.orderms.dto;

import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.model.Location;

public class LocationDTO {

	private Integer id;
	private String name;
	private Institution institution;
	
	public LocationDTO() {
	}

	public LocationDTO(Location location) {
		this.id = location.getId();
		this.name = location.getName();
		this.institution = location.getInstitution();
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

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
}
