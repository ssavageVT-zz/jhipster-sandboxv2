package tech.ipponusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.ipponusa.service.PtoRequestService;
import tech.ipponusa.web.rest.util.HeaderUtil;
import tech.ipponusa.service.dto.PtoRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PtoRequest.
 */
@RestController
@RequestMapping("/api")
public class PtoRequestResource {

    private final Logger log = LoggerFactory.getLogger(PtoRequestResource.class);
        
    @Inject
    private PtoRequestService ptoRequestService;

    /**
     * POST  /pto-requests : Create a new ptoRequest.
     *
     * @param ptoRequestDTO the ptoRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ptoRequestDTO, or with status 400 (Bad Request) if the ptoRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pto-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoRequestDTO> createPtoRequest(@RequestBody PtoRequestDTO ptoRequestDTO) throws URISyntaxException {
        log.debug("REST request to save PtoRequest : {}", ptoRequestDTO);
        if (ptoRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ptoRequest", "idexists", "A new ptoRequest cannot already have an ID")).body(null);
        }
        PtoRequestDTO result = ptoRequestService.save(ptoRequestDTO);
        return ResponseEntity.created(new URI("/api/pto-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ptoRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pto-requests : Updates an existing ptoRequest.
     *
     * @param ptoRequestDTO the ptoRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ptoRequestDTO,
     * or with status 400 (Bad Request) if the ptoRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the ptoRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pto-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoRequestDTO> updatePtoRequest(@RequestBody PtoRequestDTO ptoRequestDTO) throws URISyntaxException {
        log.debug("REST request to update PtoRequest : {}", ptoRequestDTO);
        if (ptoRequestDTO.getId() == null) {
            return createPtoRequest(ptoRequestDTO);
        }
        PtoRequestDTO result = ptoRequestService.save(ptoRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ptoRequest", ptoRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pto-requests : get all the ptoRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ptoRequests in body
     */
    @RequestMapping(value = "/pto-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PtoRequestDTO> getAllPtoRequests() {
        log.debug("REST request to get all PtoRequests");
        return ptoRequestService.findAll();
    }

    /**
     * GET  /pto-requests/:id : get the "id" ptoRequest.
     *
     * @param id the id of the ptoRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ptoRequestDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pto-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoRequestDTO> getPtoRequest(@PathVariable Long id) {
        log.debug("REST request to get PtoRequest : {}", id);
        PtoRequestDTO ptoRequestDTO = ptoRequestService.findOne(id);
        return Optional.ofNullable(ptoRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pto-requests/:id : delete the "id" ptoRequest.
     *
     * @param id the id of the ptoRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pto-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePtoRequest(@PathVariable Long id) {
        log.debug("REST request to delete PtoRequest : {}", id);
        ptoRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ptoRequest", id.toString())).build();
    }

}
