package tech.ipponusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.ipponusa.service.PtoPeriodService;
import tech.ipponusa.web.rest.util.HeaderUtil;
import tech.ipponusa.service.dto.PtoPeriodDTO;
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
 * REST controller for managing PtoPeriod.
 */
@RestController
@RequestMapping("/api")
public class PtoPeriodResource {

    private final Logger log = LoggerFactory.getLogger(PtoPeriodResource.class);
        
    @Inject
    private PtoPeriodService ptoPeriodService;

    /**
     * POST  /pto-periods : Create a new ptoPeriod.
     *
     * @param ptoPeriodDTO the ptoPeriodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ptoPeriodDTO, or with status 400 (Bad Request) if the ptoPeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pto-periods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoPeriodDTO> createPtoPeriod(@RequestBody PtoPeriodDTO ptoPeriodDTO) throws URISyntaxException {
        log.debug("REST request to save PtoPeriod : {}", ptoPeriodDTO);
        if (ptoPeriodDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ptoPeriod", "idexists", "A new ptoPeriod cannot already have an ID")).body(null);
        }
        PtoPeriodDTO result = ptoPeriodService.save(ptoPeriodDTO);
        return ResponseEntity.created(new URI("/api/pto-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ptoPeriod", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pto-periods : Updates an existing ptoPeriod.
     *
     * @param ptoPeriodDTO the ptoPeriodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ptoPeriodDTO,
     * or with status 400 (Bad Request) if the ptoPeriodDTO is not valid,
     * or with status 500 (Internal Server Error) if the ptoPeriodDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pto-periods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoPeriodDTO> updatePtoPeriod(@RequestBody PtoPeriodDTO ptoPeriodDTO) throws URISyntaxException {
        log.debug("REST request to update PtoPeriod : {}", ptoPeriodDTO);
        if (ptoPeriodDTO.getId() == null) {
            return createPtoPeriod(ptoPeriodDTO);
        }
        PtoPeriodDTO result = ptoPeriodService.save(ptoPeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ptoPeriod", ptoPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pto-periods : get all the ptoPeriods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ptoPeriods in body
     */
    @RequestMapping(value = "/pto-periods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PtoPeriodDTO> getAllPtoPeriods() {
        log.debug("REST request to get all PtoPeriods");
        return ptoPeriodService.findAll();
    }

    /**
     * GET  /pto-periods/:id : get the "id" ptoPeriod.
     *
     * @param id the id of the ptoPeriodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ptoPeriodDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pto-periods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PtoPeriodDTO> getPtoPeriod(@PathVariable Long id) {
        log.debug("REST request to get PtoPeriod : {}", id);
        PtoPeriodDTO ptoPeriodDTO = ptoPeriodService.findOne(id);
        return Optional.ofNullable(ptoPeriodDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pto-periods/:id : delete the "id" ptoPeriod.
     *
     * @param id the id of the ptoPeriodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pto-periods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePtoPeriod(@PathVariable Long id) {
        log.debug("REST request to delete PtoPeriod : {}", id);
        ptoPeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ptoPeriod", id.toString())).build();
    }

}
