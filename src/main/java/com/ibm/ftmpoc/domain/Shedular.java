package com.ibm.ftmpoc.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * The Shedular entity.
 */
@ApiModel(description = "The Shedular entity.")
@Entity
@Table(name = "shedular")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Shedular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_shecduled_at")
    private ZonedDateTime taskShecduledAt;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public Shedular taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ZonedDateTime getTaskShecduledAt() {
        return taskShecduledAt;
    }

    public Shedular taskShecduledAt(ZonedDateTime taskShecduledAt) {
        this.taskShecduledAt = taskShecduledAt;
        return this;
    }

    public void setTaskShecduledAt(ZonedDateTime taskShecduledAt) {
        this.taskShecduledAt = taskShecduledAt;
    }

    public String getStatus() {
        return status;
    }

    public Shedular status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shedular)) {
            return false;
        }
        return id != null && id.equals(((Shedular) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shedular{" +
            "id=" + getId() +
            ", taskName='" + getTaskName() + "'" +
            ", taskShecduledAt='" + getTaskShecduledAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
