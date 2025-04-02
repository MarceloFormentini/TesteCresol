package fullstack.cresol.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fullstack.cresol.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
	Optional<Location> findByName(String name);

	@Query("SELECT l FROM Location l WHERE l.institution.id = :institution")
	Page<Location> findAllByInstitution(@Param("institution") Integer institution, Pageable pageable);

	@Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Location l WHERE l.institution.id = :institution")
	Boolean findByInstitution(@Param("institution") Integer institution);
}
