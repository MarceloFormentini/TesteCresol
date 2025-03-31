package br.com.cresol.orderms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.model.TypeInstitution;

public interface InstitutionRepository extends JpaRepository<Institution, Integer>{

	public boolean existsByNameAndTypeInstitution(String name, TypeInstitution typeInstitution);

	@Query("select case when count(i) > 0 then true else false end from Institution i where i.name=:name and i.typeInstitution.id=:type")
	public boolean existsInstitutionAndType(@Param("name") String name, @Param("type") Integer type);

	Page<Institution> findAll(Pageable pageable);

	@Query("select case when count(i) > 0 then true else false end from Institution i where i.typeInstitution.id = :type")
	public boolean existsTypeByInstitution(@Param("type") Integer type);
}
