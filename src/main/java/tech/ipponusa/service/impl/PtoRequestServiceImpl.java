package tech.ipponusa.service.impl;

import tech.ipponusa.service.PtoRequestService;
import tech.ipponusa.domain.PtoRequest;
import tech.ipponusa.repository.PtoRequestRepository;
import tech.ipponusa.service.dto.PtoRequestDTO;
import tech.ipponusa.service.mapper.PtoRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PtoRequest.
 */
@Service
@Transactional
public class PtoRequestServiceImpl implements PtoRequestService{

    private final Logger log = LoggerFactory.getLogger(PtoRequestServiceImpl.class);
    
    @Inject
    private PtoRequestRepository ptoRequestRepository;

    @Inject
    private PtoRequestMapper ptoRequestMapper;

    /**
     * Save a ptoRequest.
     *
     * @param ptoRequestDTO the entity to save
     * @return the persisted entity
     */
    public PtoRequestDTO save(PtoRequestDTO ptoRequestDTO) {
        log.debug("Request to save PtoRequest : {}", ptoRequestDTO);
        PtoRequest ptoRequest = ptoRequestMapper.ptoRequestDTOToPtoRequest(ptoRequestDTO);
        ptoRequest = ptoRequestRepository.save(ptoRequest);
        PtoRequestDTO result = ptoRequestMapper.ptoRequestToPtoRequestDTO(ptoRequest);
        return result;
    }

    /**
     *  Get all the ptoRequests.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PtoRequestDTO> findAll() {
        log.debug("Request to get all PtoRequests");
        List<PtoRequestDTO> result = ptoRequestRepository.findAll().stream()
            .map(ptoRequestMapper::ptoRequestToPtoRequestDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one ptoRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PtoRequestDTO findOne(Long id) {
        log.debug("Request to get PtoRequest : {}", id);
        PtoRequest ptoRequest = ptoRequestRepository.findOne(id);
        PtoRequestDTO ptoRequestDTO = ptoRequestMapper.ptoRequestToPtoRequestDTO(ptoRequest);
        return ptoRequestDTO;
    }

    /**
     *  Delete the  ptoRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PtoRequest : {}", id);
        ptoRequestRepository.delete(id);
    }
}
