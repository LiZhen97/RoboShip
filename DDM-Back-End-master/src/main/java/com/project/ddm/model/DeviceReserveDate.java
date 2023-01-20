package com.project.ddm.model;

import javax.persistence.*;

@Entity
@Table(name = "device_reserve_date")
public class DeviceReserveDate {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private DeviceReserveDateKey id;

    @OneToOne
    @MapsId("device_id")
    private Device device;

    public DeviceReserveDate() {}

    public DeviceReserveDate(DeviceReserveDateKey id, Device device) {
        this.id = id;
        this.device = device;
    }

    public DeviceReserveDateKey getId() {
        return id;
    }

    public Device getStay() {
        return device;
    }
}
