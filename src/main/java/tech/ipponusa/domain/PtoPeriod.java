package tech.ipponusa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PtoPeriod.
 */
@Entity
@Table(name = "pto_period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PtoPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "hours_allowed")
    private Long hoursAllowed;

    @Column(name = "days_in_period")
    private Long daysInPeriod;

    @Column(name = "hours_accrued")
    private Long hoursAccrued;

    @Column(name = "hours_remaining")
    private Long hoursRemaining;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public PtoPeriod startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public PtoPeriod endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getHoursAllowed() {
        return hoursAllowed;
    }

    public PtoPeriod hoursAllowed(Long hoursAllowed) {
        this.hoursAllowed = hoursAllowed;
        return this;
    }

    public void setHoursAllowed(Long hoursAllowed) {
        this.hoursAllowed = hoursAllowed;
    }

    public Long getDaysInPeriod() {
        return daysInPeriod;
    }

    public PtoPeriod daysInPeriod(Long daysInPeriod) {
        this.daysInPeriod = daysInPeriod;
        return this;
    }

    public void setDaysInPeriod(Long daysInPeriod) {
        this.daysInPeriod = daysInPeriod;
    }

    public Long getHoursAccrued() {
        return hoursAccrued;
    }

    public PtoPeriod hoursAccrued(Long hoursAccrued) {
        this.hoursAccrued = hoursAccrued;
        return this;
    }

    public void setHoursAccrued(Long hoursAccrued) {
        this.hoursAccrued = hoursAccrued;
    }

    public Long getHoursRemaining() {
        return hoursRemaining;
    }

    public PtoPeriod hoursRemaining(Long hoursRemaining) {
        this.hoursRemaining = hoursRemaining;
        return this;
    }

    public void setHoursRemaining(Long hoursRemaining) {
        this.hoursRemaining = hoursRemaining;
    }

    public Employee getEmployee() {
        return employee;
    }

    public PtoPeriod employee(Employee employee) {
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
        PtoPeriod ptoPeriod = (PtoPeriod) o;
        if(ptoPeriod.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ptoPeriod.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PtoPeriod{" +
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
