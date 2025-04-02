package fullstack.cresol.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fullstack.cresol.dto.LocationDTO;
import fullstack.cresol.exception.InstitutionNotFoundException;
import fullstack.cresol.exception.LocatationNotFoudException;
import fullstack.cresol.exception.LocationConflictException;
import fullstack.cresol.exception.LocationInstitutionIncompatibleException;
import fullstack.cresol.model.Institution;
import fullstack.cresol.model.Location;
import fullstack.cresol.repository.InstitutionRepository;
import fullstack.cresol.repository.LocationRepository;
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
		return locationRepository.findAllByInstitution(institution, pageable)
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
