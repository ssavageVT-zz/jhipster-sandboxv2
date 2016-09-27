package tech.ipponusa.service.mapper;

import tech.ipponusa.domain.*;
import tech.ipponusa.service.dto.EmployeeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper {

    @Mapping(source = "department.id", target = "departmentId")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "ptoperiods", ignore = true)
    @Mapping(target = "ptorequests", ignore = true)
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    List<Employee> employeeDTOsToEmployees(List<EmployeeDTO> employeeDTOs);

    default Department departmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
