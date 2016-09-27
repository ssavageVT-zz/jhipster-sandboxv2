package tech.ipponusa.repository;

import tech.ipponusa.domain.Employee;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

	@Query("From Employee e where e.email = :emailAddress")
	@Transactional(readOnly=true)
	public Employee findEmployeeByEmail(@Param("emailAddress") String emailAddress);
	
}
