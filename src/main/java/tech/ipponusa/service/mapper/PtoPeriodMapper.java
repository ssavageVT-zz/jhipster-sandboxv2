package tech.ipponusa.service.mapper;

import tech.ipponusa.domain.*;
import tech.ipponusa.service.dto.PtoPeriodDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PtoPeriod and its DTO PtoPeriodDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PtoPeriodMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    PtoPeriodDTO ptoPeriodToPtoPeriodDTO(PtoPeriod ptoPeriod);

    List<PtoPeriodDTO> ptoPeriodsToPtoPeriodDTOs(List<PtoPeriod> ptoPeriods);

    @Mapping(source = "employeeId", target = "employee")
    PtoPeriod ptoPeriodDTOToPtoPeriod(PtoPeriodDTO ptoPeriodDTO);

    List<PtoPeriod> ptoPeriodDTOsToPtoPeriods(List<PtoPeriodDTO> ptoPeriodDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
