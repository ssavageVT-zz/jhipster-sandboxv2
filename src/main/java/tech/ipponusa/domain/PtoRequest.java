package tech.ipponusa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PtoRequest.
 */
@Entity
@Table(name = "pto_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PtoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "request_date")
    private ZonedDateTime requestDate;

    @Column(name = "hours_requested")
    private Long hoursRequested;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "approved_by")
    private String approvedBy;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRequestDate() {
        return requestDate;
    }

    public PtoRequest requestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Long getHoursRequested() {
        return hoursRequested;
    }

    public PtoRequest hoursRequested(Long hoursRequested) {
        this.hoursRequested = hoursRequested;
        return this;
    }

    public void setHoursRequested(Long hoursRequested) {
        this.hoursRequested = hoursRequested;
    }

    public Boolean isIsApproved() {
        return isApproved;
    }

    public PtoRequest isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public PtoRequest approvedBy(String approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Employee getEmployee() {
        return employee;
    }

    public PtoRequest employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PtoRequest ptoRequest = (PtoRequest) o;
        if(ptoRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ptoRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PtoRequest{" +
            "id=" + id +
            ", requestDate='" + requestDate + "'" +
            ", hoursRequested='" + hoursRequested + "'" +
            ", isApproved='" + isApproved + "'" +
            ", approvedBy='" + approvedBy + "'" +
            '}';
    }
}
