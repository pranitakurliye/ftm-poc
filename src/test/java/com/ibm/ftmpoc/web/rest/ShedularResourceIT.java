package com.ibm.ftmpoc.web.rest;

import com.ibm.ftmpoc.FtmpocApp;
import com.ibm.ftmpoc.domain.Shedular;
import com.ibm.ftmpoc.repository.ShedularRepository;
import com.ibm.ftmpoc.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.ibm.ftmpoc.web.rest.TestUtil.sameInstant;
import static com.ibm.ftmpoc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShedularResource} REST controller.
 */
@SpringBootTest(classes = FtmpocApp.class)
public class ShedularResourceIT {

    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TASK_SHECDULED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TASK_SHECDULED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ShedularRepository shedularRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restShedularMockMvc;

    private Shedular shedular;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShedularResource shedularResource = new ShedularResource(shedularRepository);
        this.restShedularMockMvc = MockMvcBuilders.standaloneSetup(shedularResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shedular createEntity(EntityManager em) {
        Shedular shedular = new Shedular()
            .taskName(DEFAULT_TASK_NAME)
            .taskShecduledAt(DEFAULT_TASK_SHECDULED_AT)
            .status(DEFAULT_STATUS);
        return shedular;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shedular createUpdatedEntity(EntityManager em) {
        Shedular shedular = new Shedular()
            .taskName(UPDATED_TASK_NAME)
            .taskShecduledAt(UPDATED_TASK_SHECDULED_AT)
            .status(UPDATED_STATUS);
        return shedular;
    }

    @BeforeEach
    public void initTest() {
        shedular = createEntity(em);
    }

    @Test
    @Transactional
    public void createShedular() throws Exception {
        int databaseSizeBeforeCreate = shedularRepository.findAll().size();

        // Create the Shedular
        restShedularMockMvc.perform(post("/api/shedulars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shedular)))
            .andExpect(status().isCreated());

        // Validate the Shedular in the database
        List<Shedular> shedularList = shedularRepository.findAll();
        assertThat(shedularList).hasSize(databaseSizeBeforeCreate + 1);
        Shedular testShedular = shedularList.get(shedularList.size() - 1);
        assertThat(testShedular.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testShedular.getTaskShecduledAt()).isEqualTo(DEFAULT_TASK_SHECDULED_AT);
        assertThat(testShedular.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createShedularWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shedularRepository.findAll().size();

        // Create the Shedular with an existing ID
        shedular.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShedularMockMvc.perform(post("/api/shedulars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shedular)))
            .andExpect(status().isBadRequest());

        // Validate the Shedular in the database
        List<Shedular> shedularList = shedularRepository.findAll();
        assertThat(shedularList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShedulars() throws Exception {
        // Initialize the database
        shedularRepository.saveAndFlush(shedular);

        // Get all the shedularList
        restShedularMockMvc.perform(get("/api/shedulars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shedular.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].taskShecduledAt").value(hasItem(sameInstant(DEFAULT_TASK_SHECDULED_AT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getShedular() throws Exception {
        // Initialize the database
        shedularRepository.saveAndFlush(shedular);

        // Get the shedular
        restShedularMockMvc.perform(get("/api/shedulars/{id}", shedular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shedular.getId().intValue()))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME))
            .andExpect(jsonPath("$.taskShecduledAt").value(sameInstant(DEFAULT_TASK_SHECDULED_AT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingShedular() throws Exception {
        // Get the shedular
        restShedularMockMvc.perform(get("/api/shedulars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShedular() throws Exception {
        // Initialize the database
        shedularRepository.saveAndFlush(shedular);

        int databaseSizeBeforeUpdate = shedularRepository.findAll().size();

        // Update the shedular
        Shedular updatedShedular = shedularRepository.findById(shedular.getId()).get();
        // Disconnect from session so that the updates on updatedShedular are not directly saved in db
        em.detach(updatedShedular);
        updatedShedular
            .taskName(UPDATED_TASK_NAME)
            .taskShecduledAt(UPDATED_TASK_SHECDULED_AT)
            .status(UPDATED_STATUS);

        restShedularMockMvc.perform(put("/api/shedulars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedShedular)))
            .andExpect(status().isOk());

        // Validate the Shedular in the database
        List<Shedular> shedularList = shedularRepository.findAll();
        assertThat(shedularList).hasSize(databaseSizeBeforeUpdate);
        Shedular testShedular = shedularList.get(shedularList.size() - 1);
        assertThat(testShedular.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testShedular.getTaskShecduledAt()).isEqualTo(UPDATED_TASK_SHECDULED_AT);
        assertThat(testShedular.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingShedular() throws Exception {
        int databaseSizeBeforeUpdate = shedularRepository.findAll().size();

        // Create the Shedular

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShedularMockMvc.perform(put("/api/shedulars")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shedular)))
            .andExpect(status().isBadRequest());

        // Validate the Shedular in the database
        List<Shedular> shedularList = shedularRepository.findAll();
        assertThat(shedularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShedular() throws Exception {
        // Initialize the database
        shedularRepository.saveAndFlush(shedular);

        int databaseSizeBeforeDelete = shedularRepository.findAll().size();

        // Delete the shedular
        restShedularMockMvc.perform(delete("/api/shedulars/{id}", shedular.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shedular> shedularList = shedularRepository.findAll();
        assertThat(shedularList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
