package tech.ipponusa.service.mapper;

import tech.ipponusa.domain.*;
import tech.ipponusa.service.dto.PtoRequestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PtoRequest and its DTO PtoRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PtoRequestMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    PtoRequestDTO ptoRequestToPtoRequestDTO(PtoRequest ptoRequest);

    List<PtoRequestDTO> ptoRequestsToPtoRequestDTOs(List<PtoRequest> ptoRequests);

    @Mapping(source = "employeeId", target = "employee")
    PtoRequest ptoRequestDTOToPtoRequest(PtoRequestDTO ptoRequestDTO);

    List<PtoRequest> ptoRequestDTOsToPtoRequests(List<PtoRequestDTO> ptoRequestDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
