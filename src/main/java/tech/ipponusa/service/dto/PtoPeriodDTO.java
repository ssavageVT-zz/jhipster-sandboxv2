package tech.ipponusa.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PtoPeriod entity.
 */
public class PtoPeriodDTO implements Serializable {

    private Long id;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Long hoursAllowed;

    private Long daysInPeriod;

    private Long hoursAccrued;

    private Long hoursRemaining;


    private Long employeeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
    public Long getHoursAllowed() {
        return hoursAllowed;
    }

    public void setHoursAllowed(Long hoursAllowed) {
        this.hoursAllowed = hoursAllowed;
    }
    public Long getDaysInPeriod() {
        return daysInPeriod;
    }

    public void setDaysInPeriod(Long daysInPeriod) {
        this.daysInPeriod = daysInPeriod;
    }
    public Long getHoursAccrued() {
        return hoursAccrued;
    }

    public void setHoursAccrued(Long hoursAccrued) {
        this.hoursAccrued = hoursAccrued;
    }
    public Long getHoursRemaining() {
        return hoursRemaining;
    }

    public void setHoursRemaining(Long hoursRemaining) {
        this.hoursRemaining = hoursRemaining;
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

        PtoPeriodDTO ptoPeriodDTO = (PtoPeriodDTO) o;

        if ( ! Objects.equals(id, ptoPeriodDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PtoPeriodDTO{" +
            "id=" + id +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", hoursAllowed='" + hoursAllowed + "'" +
            ", daysInPeriod='" + daysInPeriod + "'" +
            ", hoursAccrued='" + hoursAccrued + "'" +
            ", hoursRemaining='" + hoursRemaining + "'" +
            '}';
    }
}
