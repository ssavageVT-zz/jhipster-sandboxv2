package tech.ipponusa.service;

import tech.ipponusa.service.dto.PtoRequestDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing PtoRequest.
 */
public interface PtoRequestService {

    /**
     * Save a ptoRequest.
     *
     * @param ptoRequestDTO the entity to save
     * @return the persisted entity
     */
    PtoRequestDTO save(PtoRequestDTO ptoRequestDTO);

    /**
     *  Get all the ptoRequests.
     *  
     *  @return the list of entities
     */
    List<PtoRequestDTO> findAll();

    /**
     *  Get the "id" ptoRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PtoRequestDTO findOne(Long id);

    /**
     *  Delete the "id" ptoRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
