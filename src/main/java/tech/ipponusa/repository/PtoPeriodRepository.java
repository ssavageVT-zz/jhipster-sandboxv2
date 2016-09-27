package tech.ipponusa.repository;

import tech.ipponusa.domain.PtoPeriod;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the PtoPeriod entity.
 */
@SuppressWarnings("unused")
public interface PtoPeriodRepository extends JpaRepository<PtoPeriod,Long> {

	@Query("From PtoPeriod p where p.employee.id = :employeeID order by end_date desc")
	@Transactional(readOnly=true)
	public List<PtoPeriod> findPtoPeriodForEmployee(@Param("employeeID") Long employeeID);
	
	
}
