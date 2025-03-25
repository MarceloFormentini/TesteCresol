package br.com.cresol.orderms.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cresol.orderms.model.Event;
import br.com.cresol.orderms.model.Institution;

public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select case when count(e) > 0 then true else false end from Event e where e.institution.id = :institution")
	public boolean existsEventByInstitution(@Param("institution") Integer institution);

	@Query("select e from Event e where e.institution.id = :institution and e.active = true")
	Page<Event> findByInstitution(@Param("institution") Integer institution, Pageable pageable);

	@Query("SELECT COUNT(e) > 0 FROM Event e WHERE e.institution = :institution AND " +
		"(e.startDate BETWEEN :startDate AND :endDate OR e.endDate BETWEEN :startDate AND :endDate)")
	boolean existsByInstitutionAndStartDateBetweenOrEndDateBetween(
		@Param("institution") Institution institution,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
