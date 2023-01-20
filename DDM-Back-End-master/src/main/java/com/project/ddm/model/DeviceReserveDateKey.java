package com.project.ddm.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

@Embeddable
public class DeviceReserveDateKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long device_id;
    private Time time;

    public DeviceReserveDateKey() {}

    public DeviceReserveDateKey(Long device_id, Time time) {
        this.device_id = device_id;
        this.time = time;
    }

    public Long getDevice_id() {
        return device_id;
    }

    public DeviceReserveDateKey setDevice_id(Long deviceId) {
        this.device_id = device_id;
        return this;
    }

    public Time getTime() {
        return time;
    }

    public DeviceReserveDateKey setTime(Time time) {
        this.time = time;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceReserveDateKey that = (DeviceReserveDateKey) o;
        return device_id.equals(that.device_id) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device_id, time);
    }
}
