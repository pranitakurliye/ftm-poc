package com.ibm.ftmpoc.repository;

import com.ibm.ftmpoc.domain.Shedular;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Shedular entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShedularRepository extends JpaRepository<Shedular, Long> {

}
