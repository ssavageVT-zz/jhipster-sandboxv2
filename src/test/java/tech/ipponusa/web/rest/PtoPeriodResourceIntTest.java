package tech.ipponusa.web.rest;

import tech.ipponusa.PtotrackerApp;

import tech.ipponusa.domain.PtoPeriod;
import tech.ipponusa.repository.PtoPeriodRepository;
import tech.ipponusa.service.PtoPeriodService;
import tech.ipponusa.service.dto.PtoPeriodDTO;
import tech.ipponusa.service.mapper.PtoPeriodMapper;

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
 * Test class for the PtoPeriodResource REST controller.
 *
 * @see PtoPeriodResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = PtotrackerApp.class)

public class PtoPeriodResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);

    private static final Long DEFAULT_HOURS_ALLOWED = 1L;
    private static final Long UPDATED_HOURS_ALLOWED = 2L;

    private static final Long DEFAULT_DAYS_IN_PERIOD = 1L;
    private static final Long UPDATED_DAYS_IN_PERIOD = 2L;

    private static final Long DEFAULT_HOURS_ACCRUED = 1L;
    private static final Long UPDATED_HOURS_ACCRUED = 2L;

    private static final Long DEFAULT_HOURS_REMAINING = 1L;
    private static final Long UPDATED_HOURS_REMAINING = 2L;

    @Inject
    private PtoPeriodRepository ptoPeriodRepository;

    @Inject
    private PtoPeriodMapper ptoPeriodMapper;

    @Inject
    private PtoPeriodService ptoPeriodService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPtoPeriodMockMvc;

    private PtoPeriod ptoPeriod;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PtoPeriodResource ptoPeriodResource = new PtoPeriodResource();
        ReflectionTestUtils.setField(ptoPeriodResource, "ptoPeriodService", ptoPeriodService);
        this.restPtoPeriodMockMvc = MockMvcBuilders.standaloneSetup(ptoPeriodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PtoPeriod createEntity(EntityManager em) {
        PtoPeriod ptoPeriod = new PtoPeriod()
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .hoursAllowed(DEFAULT_HOURS_ALLOWED)
                .daysInPeriod(DEFAULT_DAYS_IN_PERIOD)
                .hoursAccrued(DEFAULT_HOURS_ACCRUED)
                .hoursRemaining(DEFAULT_HOURS_REMAINING);
        return ptoPeriod;
    }

    @Before
    public void initTest() {
        ptoPeriod = createEntity(em);
    }

    @Test
    @Transactional
    public void createPtoPeriod() throws Exception {
        int databaseSizeBeforeCreate = ptoPeriodRepository.findAll().size();

        // Create the PtoPeriod
        PtoPeriodDTO ptoPeriodDTO = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(ptoPeriod);

        restPtoPeriodMockMvc.perform(post("/api/pto-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ptoPeriodDTO)))
                .andExpect(status().isCreated());

        // Validate the PtoPeriod in the database
        List<PtoPeriod> ptoPeriods = ptoPeriodRepository.findAll();
        assertThat(ptoPeriods).hasSize(databaseSizeBeforeCreate + 1);
        PtoPeriod testPtoPeriod = ptoPeriods.get(ptoPeriods.size() - 1);
        assertThat(testPtoPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPtoPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPtoPeriod.getHoursAllowed()).isEqualTo(DEFAULT_HOURS_ALLOWED);
        assertThat(testPtoPeriod.getDaysInPeriod()).isEqualTo(DEFAULT_DAYS_IN_PERIOD);
        assertThat(testPtoPeriod.getHoursAccrued()).isEqualTo(DEFAULT_HOURS_ACCRUED);
        assertThat(testPtoPeriod.getHoursRemaining()).isEqualTo(DEFAULT_HOURS_REMAINING);
    }

    @Test
    @Transactional
    public void getAllPtoPeriods() throws Exception {
        // Initialize the database
        ptoPeriodRepository.saveAndFlush(ptoPeriod);

        // Get all the ptoPeriods
        restPtoPeriodMockMvc.perform(get("/api/pto-periods?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ptoPeriod.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].hoursAllowed").value(hasItem(DEFAULT_HOURS_ALLOWED.intValue())))
                .andExpect(jsonPath("$.[*].daysInPeriod").value(hasItem(DEFAULT_DAYS_IN_PERIOD.intValue())))
                .andExpect(jsonPath("$.[*].hoursAccrued").value(hasItem(DEFAULT_HOURS_ACCRUED.intValue())))
                .andExpect(jsonPath("$.[*].hoursRemaining").value(hasItem(DEFAULT_HOURS_REMAINING.intValue())));
    }

    @Test
    @Transactional
    public void getPtoPeriod() throws Exception {
        // Initialize the database
        ptoPeriodRepository.saveAndFlush(ptoPeriod);

        // Get the ptoPeriod
        restPtoPeriodMockMvc.perform(get("/api/pto-periods/{id}", ptoPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ptoPeriod.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.hoursAllowed").value(DEFAULT_HOURS_ALLOWED.intValue()))
            .andExpect(jsonPath("$.daysInPeriod").value(DEFAULT_DAYS_IN_PERIOD.intValue()))
            .andExpect(jsonPath("$.hoursAccrued").value(DEFAULT_HOURS_ACCRUED.intValue()))
            .andExpect(jsonPath("$.hoursRemaining").value(DEFAULT_HOURS_REMAINING.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPtoPeriod() throws Exception {
        // Get the ptoPeriod
        restPtoPeriodMockMvc.perform(get("/api/pto-periods/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePtoPeriod() throws Exception {
        // Initialize the database
        ptoPeriodRepository.saveAndFlush(ptoPeriod);
        int databaseSizeBeforeUpdate = ptoPeriodRepository.findAll().size();

        // Update the ptoPeriod
        PtoPeriod updatedPtoPeriod = ptoPeriodRepository.findOne(ptoPeriod.getId());
        updatedPtoPeriod
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE)
                .hoursAllowed(UPDATED_HOURS_ALLOWED)
                .daysInPeriod(UPDATED_DAYS_IN_PERIOD)
                .hoursAccrued(UPDATED_HOURS_ACCRUED)
                .hoursRemaining(UPDATED_HOURS_REMAINING);
        PtoPeriodDTO ptoPeriodDTO = ptoPeriodMapper.ptoPeriodToPtoPeriodDTO(updatedPtoPeriod);

        restPtoPeriodMockMvc.perform(put("/api/pto-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ptoPeriodDTO)))
                .andExpect(status().isOk());

        // Validate the PtoPeriod in the database
        List<PtoPeriod> ptoPeriods = ptoPeriodRepository.findAll();
        assertThat(ptoPeriods).hasSize(databaseSizeBeforeUpdate);
        PtoPeriod testPtoPeriod = ptoPeriods.get(ptoPeriods.size() - 1);
        assertThat(testPtoPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPtoPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPtoPeriod.getHoursAllowed()).isEqualTo(UPDATED_HOURS_ALLOWED);
        assertThat(testPtoPeriod.getDaysInPeriod()).isEqualTo(UPDATED_DAYS_IN_PERIOD);
        assertThat(testPtoPeriod.getHoursAccrued()).isEqualTo(UPDATED_HOURS_ACCRUED);
        assertThat(testPtoPeriod.getHoursRemaining()).isEqualTo(UPDATED_HOURS_REMAINING);
    }

    @Test
    @Transactional
    public void deletePtoPeriod() throws Exception {
        // Initialize the database
        ptoPeriodRepository.saveAndFlush(ptoPeriod);
        int databaseSizeBeforeDelete = ptoPeriodRepository.findAll().size();

        // Get the ptoPeriod
        restPtoPeriodMockMvc.perform(delete("/api/pto-periods/{id}", ptoPeriod.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PtoPeriod> ptoPeriods = ptoPeriodRepository.findAll();
        assertThat(ptoPeriods).hasSize(databaseSizeBeforeDelete - 1);
    }
}
