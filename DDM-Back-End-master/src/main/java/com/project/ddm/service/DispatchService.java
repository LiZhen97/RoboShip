package com.project.ddm.service;

import com.project.ddm.model.Device;
import com.project.ddm.model.DeviceType;
import com.project.ddm.model.Station;
import com.project.ddm.repository.DeviceRepository;
import com.project.ddm.repository.DeviceReserveDateRepository;
import com.project.ddm.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DispatchService {

    private StationRepository stationRepository;
    private DeviceReserveDateRepository deviceReserveDateRepository;
    private DeviceRepository deviceRepository;

    @Autowired
    public DispatchService(StationRepository stationRepository,
                           DeviceReserveDateRepository deviceReserveDateRepository,
                            DeviceRepository deviceRepository) {
        this.stationRepository = stationRepository;
        this.deviceReserveDateRepository = deviceReserveDateRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * @param lon delivery origin lontitude
     * @param lat delivery origin latitude
     * @return id of the station closest to sender's location
     */
    public Long getClosestStationId(double lat, double lon) {
        List<Station> stations = stationRepository.findAll();
        Long bestId = -1L;
        double bestDist = Double.MAX_VALUE;

        // get station closest to lat lon
        for (Station station : stations) {
            double stationLat = station.getLatitude();
            double stationLon = station.getLongitude();
            double dist = getGlobeDistance(lat, lon, stationLat, stationLon);
            if (dist < bestDist) {
                bestDist = dist;
                bestId = station.getStationId();
            }
        }

        return bestId;
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findDeviceById(id);
    }
    /**
     * @param lon delivery origin's longitude
     * @param lat delivery origin's latitude
     * @param stationLon station's longitude
     * @param stationLat station's latitude
     * @return distance from sender's location to station's location
     */
    public double getDistance(double lat, double lon, double stationLat, double stationLon) {
        return Math.abs(lat - stationLat) + Math.abs(lon - stationLon);
    }

    // calculates the distance between two points (given the
    //latitude/longitude of those points). It is being used to calculate
    //the distance between two locations using GeoDataSource (TM) products
    // https://www.geodatasource.com/developers/java
    public double getGlobeDistance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

            return (dist);
        }
    }

    public List<Long> getAvailableDeviceIdsAtStation (Long stationId, String type) {
        Station station = stationRepository.findStationById(stationId);
        List<Device> devices = new ArrayList<>();

        // get device that matches type
        for (Device device : station.getDevices()) {
            if (device.getDeviceType().equals(type)) {
                devices.add(device);
            }
        }
        List<Long> deviceIds = new ArrayList<>();
        for (Device device : devices) {
            deviceIds.add(device.getId());
        }

        Set<Long> reservedDeviceIds = deviceReserveDateRepository.findByIdAndTimeAfter(deviceIds, Time.valueOf(LocalTime.now()));

        List<Long> filteredDeviceIds = new ArrayList<>();
        for (Long id : deviceIds) {
            if (!reservedDeviceIds.contains(id)) {
                filteredDeviceIds.add(id);
            }
        }
        return filteredDeviceIds;
    }
}
