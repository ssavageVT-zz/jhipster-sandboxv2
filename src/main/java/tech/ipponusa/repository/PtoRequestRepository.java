package tech.ipponusa.repository;

import tech.ipponusa.domain.PtoRequest;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the PtoRequest entity.
 */
@SuppressWarnings("unused")
public interface PtoRequestRepository extends JpaRepository<PtoRequest,Long> {

	@Query("From PtoRequest p where p.employee.id = :employeeID order by request_date desc")
	@Transactional(readOnly=true)
	public List<PtoRequest> findPtoRequestsForEmployee(@Param("employeeID") Long employeeID);
	
	
}
