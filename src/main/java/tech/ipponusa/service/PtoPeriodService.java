package tech.ipponusa.service;

import tech.ipponusa.service.dto.PtoPeriodDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing PtoPeriod.
 */
public interface PtoPeriodService {

    /**
     * Save a ptoPeriod.
     *
     * @param ptoPeriodDTO the entity to save
     * @return the persisted entity
     */
    PtoPeriodDTO save(PtoPeriodDTO ptoPeriodDTO);

    /**
     *  Get all the ptoPeriods.
     *  
     *  @return the list of entities
     */
    List<PtoPeriodDTO> findAll();

    /**
     *  Get the "id" ptoPeriod.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PtoPeriodDTO findOne(Long id);

    /**
     *  Delete the "id" ptoPeriod.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
