
package com.project.ddm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "device")
@JsonDeserialize(builder = Device.Builder.class)

public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double deliverDistance;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private String deviceType;

    @OneToOne (mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;

    public  Device(){}

    private Device(Builder builder){
        this.id = builder.id;
        this.deliverDistance = builder.deliverDistance;
        this.station = builder.station;
        this.deviceType = builder.deviceType;
        this.order = builder.order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDeliverDistance() {
        return deliverDistance;
    }

    public void setDeliverDistance(double deliverDistance) {
        this.deliverDistance = deliverDistance;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Order getOrders() {
        return order;
    }

    public void setOrders(Order order) {
        this.order = order;
    }

    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public static class Builder{
        @JsonProperty("id")
        private Long id;

        @JsonProperty("deliver_distance")
        private double deliverDistance;

        @JsonProperty("station")
        private Station station;

        @JsonProperty("device_reverseDates")
        private List<DeviceReserveDate> deviceReverseDates;
        @JsonProperty("device_type")
        private String deviceType;

        @JsonProperty
        private Order order;

        public Builder setDeviceId(Long id){
            this.id = id;;
            return this;
        }

        public Builder setDeliverDistance(double deliverDistance){
            this.deliverDistance = deliverDistance;
            return this;
        }

        public Builder setStation(Station station){
            this.station = station;
            return this;
        }

        public Builder setDeviceReverseDate(List<DeviceReserveDate> deviceReverseDates){
            this.deviceReverseDates = deviceReverseDates;
            return this;
        }

        public Builder setOrders(Order order) {
            this.order = order;
            return this;
        }
    }
}
