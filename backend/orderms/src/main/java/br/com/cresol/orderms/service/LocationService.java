package br.com.cresol.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cresol.orderms.dto.LocationDTO;
import br.com.cresol.orderms.exception.InstitutionNotFoundException;
import br.com.cresol.orderms.exception.LocatationNotFoudException;
import br.com.cresol.orderms.exception.LocationConflictException;
import br.com.cresol.orderms.exception.LocationInstitutionIncompatibleException;
import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.model.Location;
import br.com.cresol.orderms.repository.InstitutionRepository;
import br.com.cresol.orderms.repository.LocationRepository;
import jakarta.validation.Valid;

@Service
public class LocationService {

	private final LocationRepository locationRepository;
	private final InstitutionRepository institutionRepository;
	
	
	public LocationService(LocationRepository locationRepository, InstitutionRepository institutionRepository) {
		this.locationRepository = locationRepository;
		this.institutionRepository = institutionRepository;
	}

	public Location addNewLocation(@Valid Location newLocation, Integer institution) {

		Institution institutionRecorded = institutionRepository.findById(institution)
	 			.orElseThrow(() -> new InstitutionNotFoundException(
	 				"Não existe instituição cadastrada com o código " + institution)
	 			);
		
		locationRepository.findByName(
				newLocation.getName()
		).ifPresent(existingLocation -> {
			throw new LocationConflictException("Local " + newLocation.getName() + " já cadastrada.");
		});
		
		Location location = new Location(
			newLocation.getName(),
			institutionRecorded
		);
		
		return locationRepository.save(location);
	}

	public Page<LocationDTO> getAllLocation(Pageable pageable, Integer institution) {
		return locationRepository.findAllLocation(institution, pageable)
				.map(location -> new LocationDTO(location));
	}

	public Location getLocationById(Integer institution, Integer id) {
		if (!institutionRepository.existsById(id)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + institution);
		}

		Location location = locationRepository.findById(id)
			.orElseThrow(() -> 
				new LocatationNotFoudException("Não existe local cadastrado com o código " + id)
		);
		
		return location;
	}

	public Location updateLocation(Location location, Integer institution) {
		if (!institutionRepository.existsById(institution)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + institution);
		}
		
		Location locationRecorded = locationRepository.findById(location.getId())
			.orElseThrow(() ->
				new LocatationNotFoudException("Não existe local cadastrado com o código " + location.getId())
			);
		
		if (!locationRecorded.getInstitution().getId().equals(institution)) {
			throw new LocationInstitutionIncompatibleException(
				"O evento não pertence a instituição especificada"
			);
		}
		
		locationRecorded.setName(location.getName());
		
		return locationRepository.save(locationRecorded);
	}

	public boolean removeLocation(Integer id) {
		if(!locationRepository.existsById(id)) {
			throw new LocatationNotFoudException("Não existe local cadastrado com o código " + id);
		}

		locationRepository.deleteById(id);
		return true;
	}

}
