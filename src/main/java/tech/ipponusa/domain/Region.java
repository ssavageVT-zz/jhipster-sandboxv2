package tech.ipponusa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "region_name")
    private String regionName;

    @OneToMany(mappedBy = "region")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Country> countries = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public Region regionName(String regionName) {
        this.regionName = regionName;
        return this;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public Region countries(Set<Country> countries) {
        this.countries = countries;
        return this;
    }

    public Region addCountry(Country country) {
        countries.add(country);
        country.setRegion(this);
        return this;
    }

    public Region removeCountry(Country country) {
        countries.remove(country);
        country.setRegion(null);
        return this;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        if(region.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Region{" +
            "id=" + id +
            ", regionName='" + regionName + "'" +
            '}';
    }
}
