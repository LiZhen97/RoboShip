package com.project.ddm.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "station")
public class Station implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double longitude;
    private double latitude;

    @OneToMany (mappedBy = "station", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<Device> devices;

    public long getStationId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setStationId(Long stationId) {
        this.id = stationId;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }
}
