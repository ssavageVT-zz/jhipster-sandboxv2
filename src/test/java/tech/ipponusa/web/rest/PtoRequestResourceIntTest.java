package tech.ipponusa.web.rest;

import tech.ipponusa.PtotrackerApp;

import tech.ipponusa.domain.PtoRequest;
import tech.ipponusa.repository.PtoRequestRepository;
import tech.ipponusa.service.PtoRequestService;
import tech.ipponusa.service.dto.PtoRequestDTO;
import tech.ipponusa.service.mapper.PtoRequestMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PtoRequestResource REST controller.
 *
 * @see PtoRequestResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = PtotrackerApp.class)

public class PtoRequestResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_REQUEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_REQUEST_DATE_STR = dateTimeFormatter.format(DEFAULT_REQUEST_DATE);

    private static final Long DEFAULT_HOURS_REQUESTED = 1L;
    private static final Long UPDATED_HOURS_REQUESTED = 2L;

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;
    private static final String DEFAULT_APPROVED_BY = "AAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBB";

    @Inject
    private PtoRequestRepository ptoRequestRepository;

    @Inject
    private PtoRequestMapper ptoRequestMapper;

    @Inject
    private PtoRequestService ptoRequestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPtoRequestMockMvc;

    private PtoRequest ptoRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PtoRequestResource ptoRequestResource = new PtoRequestResource();
        ReflectionTestUtils.setField(ptoRequestResource, "ptoRequestService", ptoRequestService);
        this.restPtoRequestMockMvc = MockMvcBuilders.standaloneSetup(ptoRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PtoRequest createEntity(EntityManager em) {
        PtoRequest ptoRequest = new PtoRequest()
                .requestDate(DEFAULT_REQUEST_DATE)
                .hoursRequested(DEFAULT_HOURS_REQUESTED)
                .isApproved(DEFAULT_IS_APPROVED)
                .approvedBy(DEFAULT_APPROVED_BY);
        return ptoRequest;
    }

    @Before
    public void initTest() {
        ptoRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createPtoRequest() throws Exception {
        int databaseSizeBeforeCreate = ptoRequestRepository.findAll().size();

        // Create the PtoRequest
        PtoRequestDTO ptoRequestDTO = ptoRequestMapper.ptoRequestToPtoRequestDTO(ptoRequest);

        restPtoRequestMockMvc.perform(post("/api/pto-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ptoRequestDTO)))
                .andExpect(status().isCreated());

        // Validate the PtoRequest in the database
        List<PtoRequest> ptoRequests = ptoRequestRepository.findAll();
        assertThat(ptoRequests).hasSize(databaseSizeBeforeCreate + 1);
        PtoRequest testPtoRequest = ptoRequests.get(ptoRequests.size() - 1);
        assertThat(testPtoRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testPtoRequest.getHoursRequested()).isEqualTo(DEFAULT_HOURS_REQUESTED);
        assertThat(testPtoRequest.isIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testPtoRequest.getApprovedBy()).isEqualTo(DEFAULT_APPROVED_BY);
    }

    @Test
    @Transactional
    public void getAllPtoRequests() throws Exception {
        // Initialize the database
        ptoRequestRepository.saveAndFlush(ptoRequest);

        // Get all the ptoRequests
        restPtoRequestMockMvc.perform(get("/api/pto-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ptoRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE_STR)))
                .andExpect(jsonPath("$.[*].hoursRequested").value(hasItem(DEFAULT_HOURS_REQUESTED.intValue())))
                .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
                .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY.toString())));
    }

    @Test
    @Transactional
    public void getPtoRequest() throws Exception {
        // Initialize the database
        ptoRequestRepository.saveAndFlush(ptoRequest);

        // Get the ptoRequest
        restPtoRequestMockMvc.perform(get("/api/pto-requests/{id}", ptoRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ptoRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE_STR))
            .andExpect(jsonPath("$.hoursRequested").value(DEFAULT_HOURS_REQUESTED.intValue()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPtoRequest() throws Exception {
        // Get the ptoRequest
        restPtoRequestMockMvc.perform(get("/api/pto-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePtoRequest() throws Exception {
        // Initialize the database
        ptoRequestRepository.saveAndFlush(ptoRequest);
        int databaseSizeBeforeUpdate = ptoRequestRepository.findAll().size();

        // Update the ptoRequest
        PtoRequest updatedPtoRequest = ptoRequestRepository.findOne(ptoRequest.getId());
        updatedPtoRequest
                .requestDate(UPDATED_REQUEST_DATE)
                .hoursRequested(UPDATED_HOURS_REQUESTED)
                .isApproved(UPDATED_IS_APPROVED)
                .approvedBy(UPDATED_APPROVED_BY);
        PtoRequestDTO ptoRequestDTO = ptoRequestMapper.ptoRequestToPtoRequestDTO(updatedPtoRequest);

        restPtoRequestMockMvc.perform(put("/api/pto-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ptoRequestDTO)))
                .andExpect(status().isOk());

        // Validate the PtoRequest in the database
        List<PtoRequest> ptoRequests = ptoRequestRepository.findAll();
        assertThat(ptoRequests).hasSize(databaseSizeBeforeUpdate);
        PtoRequest testPtoRequest = ptoRequests.get(ptoRequests.size() - 1);
        assertThat(testPtoRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testPtoRequest.getHoursRequested()).isEqualTo(UPDATED_HOURS_REQUESTED);
        assertThat(testPtoRequest.isIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPtoRequest.getApprovedBy()).isEqualTo(UPDATED_APPROVED_BY);
    }

    @Test
    @Transactional
    public void deletePtoRequest() throws Exception {
        // Initialize the database
        ptoRequestRepository.saveAndFlush(ptoRequest);
        int databaseSizeBeforeDelete = ptoRequestRepository.findAll().size();

        // Get the ptoRequest
        restPtoRequestMockMvc.perform(delete("/api/pto-requests/{id}", ptoRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PtoRequest> ptoRequests = ptoRequestRepository.findAll();
        assertThat(ptoRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
