package fullstack.cresol.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fullstack.cresol.model.Event;
import fullstack.cresol.model.Location;

public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where e.location.id = :location")
	Page<Event> findByLocation(@Param("location") Integer location, Pageable pageable);

	@Query("SELECT COUNT(e) > 0 FROM Event e WHERE e.location = :location AND " +
		"(e.startDate BETWEEN :startDate AND :endDate OR e.endDate BETWEEN :startDate AND :endDate)")
	boolean existsByLocationAndStartDateBetweenOrEndDateBetween(
		@Param("location") Location location,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
