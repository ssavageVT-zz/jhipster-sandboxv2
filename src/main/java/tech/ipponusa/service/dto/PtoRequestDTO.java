package tech.ipponusa.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PtoRequest entity.
 */
public class PtoRequestDTO implements Serializable {

    private Long id;

    private ZonedDateTime requestDate;

    private Long hoursRequested;

    private Boolean isApproved;

    private String approvedBy;


    private Long employeeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }
    public Long getHoursRequested() {
        return hoursRequested;
    }

    public void setHoursRequested(Long hoursRequested) {
        this.hoursRequested = hoursRequested;
    }
    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PtoRequestDTO ptoRequestDTO = (PtoRequestDTO) o;

        if ( ! Objects.equals(id, ptoRequestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PtoRequestDTO{" +
            "id=" + id +
            ", requestDate='" + requestDate + "'" +
            ", hoursRequested='" + hoursRequested + "'" +
            ", isApproved='" + isApproved + "'" +
            ", approvedBy='" + approvedBy + "'" +
            '}';
    }
}
