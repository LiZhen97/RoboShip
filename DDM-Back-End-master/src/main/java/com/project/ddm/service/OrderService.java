package com.project.ddm.service;

import com.project.ddm.model.*;
import com.project.ddm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private DispatchService dispatchService;
    private StationRepository stationRepository;
    private TrackRepository trackRepository;
    private OrderRepository orderRepository;
    private DeviceReserveDateRepository deviceReserveDateRepository;
    private DeliveryService deliveryService;
    private GeoCodingService geoCodingService;
    private final String BEING_PICKED_UP = "Package is being picked up";

    @Autowired
    public OrderService(DispatchService dispatchService,
                        StationRepository stationRepository,
                        TrackRepository trackRepository,
                        OrderRepository orderRepository,
                        DeviceReserveDateRepository deviceReserveDateRepository,
                        DeliveryService deliveryService,
                        GeoCodingService geoCodingService) {
        this.dispatchService = dispatchService;
        this.stationRepository = stationRepository;
        this.trackRepository = trackRepository;
        this.orderRepository = orderRepository;
        this.deviceReserveDateRepository = deviceReserveDateRepository;
        this.deliveryService = deliveryService;
        this.geoCodingService = geoCodingService;
    }

    public double getCost(double weight, double size, double lon1, double lat1, double lon2, double lat2, String deviceType) {
        double ratio;

        Long stationId = dispatchService.getClosestStationId(lon1, lat1);

        Station station = stationRepository.findStationById(stationId);
        double OriginToStation = dispatchService.getDistance(lon1, lat1, station.getLongitude(), station.getLatitude());
        double DestToStation = dispatchService.getDistance(lon2, lat2, station.getLongitude(), station.getLatitude());
        double OriginToDest = dispatchService.getGlobeDistance(lat1, lon1, lat2, lon2);

        if (deviceType.equals("DRONE")) {
            ratio = 10;
        } else {
            ratio = 8;
        }

        double price = ratio * weight / size + 0.1 * (OriginToStation + OriginToDest + DestToStation);
        double priceTwoDec = Math.round(price * 100.0) / 100.0;

        return priceTwoDec;
    }

    public int generateTrackingNumber() {
        int trackId = Math.abs(new Random(100).nextInt());

        while (trackRepository.findOrderByTrackId(trackId) != null) {
            trackId = Math.abs(new Random().nextInt() / 10000);
        }

        return trackId;
    }

    public int placeOrder(Order order, String deviceType) {
        String sendingAddress = order.getSendingAddress();
        String receivingAddress = order.getReceivingAddress();

        double[] sendingLatLon = geoCodingService.getLatLng(sendingAddress);
        double[] receivingLatLon = geoCodingService.getLatLng(receivingAddress);

        Long stationId = dispatchService.getClosestStationId(sendingLatLon[0], sendingLatLon[1]);
        List<Long> deviceIds = dispatchService.getAvailableDeviceIdsAtStation(stationId, deviceType);
        // can have some logic to pick device to use as extension
        Device device = dispatchService.getDeviceById(deviceIds.get((int) Math.random() * deviceIds.size()));
        Station station = device.getStation();

        List<Long> pickUpTimes = deliveryService.getPickUpTime(station.getLatitude(), station.getLongitude(), sendingLatLon[0], sendingLatLon[1]);
        List<Long> deliveryTimes = deliveryService.getDeliveryTime(receivingLatLon[0], receivingLatLon[1], sendingLatLon[0], sendingLatLon[1]);
        long orderFulfillTime = 0L;
        if (device.getDeviceType().equals("ROBOT")) {
            orderFulfillTime = pickUpTimes.get(0) + deliveryTimes.get(0);
        } else if (device.getDeviceType().equals("DRONE")) {
            orderFulfillTime = pickUpTimes.get(1) + deliveryTimes.get(1);
        }

        LocalTime deliveryTime = LocalTime.now().plusSeconds(orderFulfillTime);
        Time reserveTime = Time.valueOf(deliveryTime);

        order.setDevice(device);

        // Get Lat/Lon
        double sendLat = sendingLatLon[0];
        double sendLon = sendingLatLon[1];
        double receiveLat = receivingLatLon[0];
        double receiveLon = receivingLatLon[1];
        double weight = order.getWeight();

        double price = getCost(weight, 10, sendLon, sendLat, receiveLon, receiveLat, deviceType);
        order.setPrice(price);

        // Get tracking number
        int trackNum = generateTrackingNumber();
        order.setTrackId(trackNum);
        order.setStatus(BEING_PICKED_UP);

        DeviceReserveDateKey deviceReserveDateKey = new DeviceReserveDateKey(device.getId(), reserveTime);
        DeviceReserveDate deviceReserveDate = new DeviceReserveDate(deviceReserveDateKey, device);
        deviceReserveDateRepository.save(deviceReserveDate);
        orderRepository.save(order);
        return trackNum;
    }
}
