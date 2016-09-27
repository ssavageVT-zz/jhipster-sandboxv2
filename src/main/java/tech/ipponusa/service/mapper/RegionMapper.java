package tech.ipponusa.service.mapper;

import tech.ipponusa.domain.*;
import tech.ipponusa.service.dto.RegionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Region and its DTO RegionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RegionMapper {

    RegionDTO regionToRegionDTO(Region region);

    List<RegionDTO> regionsToRegionDTOs(List<Region> regions);

    @Mapping(target = "countries", ignore = true)
    Region regionDTOToRegion(RegionDTO regionDTO);

    List<Region> regionDTOsToRegions(List<RegionDTO> regionDTOs);
}
