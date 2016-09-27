package tech.ipponusa.service.mapper;

import tech.ipponusa.domain.*;
import tech.ipponusa.service.dto.LocationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper {

    @Mapping(source = "country.id", target = "countryId")
    LocationDTO locationToLocationDTO(Location location);

    List<LocationDTO> locationsToLocationDTOs(List<Location> locations);

    @Mapping(source = "countryId", target = "country")
    @Mapping(target = "departments", ignore = true)
    Location locationDTOToLocation(LocationDTO locationDTO);

    List<Location> locationDTOsToLocations(List<LocationDTO> locationDTOs);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
