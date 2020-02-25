package com.ibm.ftmpoc.web.rest;

import com.ibm.ftmpoc.domain.Shedular;
import com.ibm.ftmpoc.repository.ShedularRepository;
import com.ibm.ftmpoc.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ibm.ftmpoc.domain.Shedular}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ShedularResource {

    private final Logger log = LoggerFactory.getLogger(ShedularResource.class);

    private static final String ENTITY_NAME = "shedular";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShedularRepository shedularRepository;

    public ShedularResource(ShedularRepository shedularRepository) {
        this.shedularRepository = shedularRepository;
    }

    /**
     * {@code POST  /shedulars} : Create a new shedular.
     *
     * @param shedular the shedular to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shedular, or with status {@code 400 (Bad Request)} if the shedular has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shedulars")
    public ResponseEntity<Shedular> createShedular(@RequestBody Shedular shedular) throws URISyntaxException {
        log.debug("REST request to save Shedular : {}", shedular);
        if (shedular.getId() != null) {
            throw new BadRequestAlertException("A new shedular cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shedular result = shedularRepository.save(shedular);
        return ResponseEntity.created(new URI("/api/shedulars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shedulars} : Updates an existing shedular.
     *
     * @param shedular the shedular to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shedular,
     * or with status {@code 400 (Bad Request)} if the shedular is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shedular couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shedulars")
    public ResponseEntity<Shedular> updateShedular(@RequestBody Shedular shedular) throws URISyntaxException {
        log.debug("REST request to update Shedular : {}", shedular);
        if (shedular.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Shedular result = shedularRepository.save(shedular);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shedular.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shedulars} : get all the shedulars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shedulars in body.
     */
    @GetMapping("/shedulars")
    public List<Shedular> getAllShedulars() {
        log.debug("REST request to get all Shedulars");
        return shedularRepository.findAll();
    }

    /**
     * {@code GET  /shedulars/:id} : get the "id" shedular.
     *
     * @param id the id of the shedular to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shedular, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shedulars/{id}")
    public ResponseEntity<Shedular> getShedular(@PathVariable Long id) {
        log.debug("REST request to get Shedular : {}", id);
        Optional<Shedular> shedular = shedularRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shedular);
    }

    /**
     * {@code DELETE  /shedulars/:id} : delete the "id" shedular.
     *
     * @param id the id of the shedular to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shedulars/{id}")
    public ResponseEntity<Void> deleteShedular(@PathVariable Long id) {
        log.debug("REST request to delete Shedular : {}", id);
        shedularRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
