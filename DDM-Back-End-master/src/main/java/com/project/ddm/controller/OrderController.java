package com.project.ddm.controller;

import java.security.Principal;

import com.project.ddm.model.*;
import com.project.ddm.repository.StationRepository;
import com.project.ddm.service.DeliveryService;
import com.project.ddm.service.DispatchService;
import com.project.ddm.service.OrderService;
import com.project.ddm.service.GeoCodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private DispatchService dispatchService;

    private OrderService orderService;

    private DeliveryService deliveryService;

    private StationRepository stationRepository;

    private GeoCodingService geoCodingService;


    @Autowired
    public OrderController(DispatchService dispatchService,
                           OrderService orderService,
                           DeliveryService deliveryService,
                           StationRepository stationRepository,
                           GeoCodingService geoCodingService) {
        this.dispatchService = dispatchService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.stationRepository = stationRepository;
        this.geoCodingService = geoCodingService;
    }

    @GetMapping(value = "/order/search/device/{lon1}/{lat1}/{lon2}/{lat2}/{weight}/{size}/{device}")
    public double getCost(@PathVariable double lon1, @PathVariable double lat1, @PathVariable double lon2, @PathVariable double lat2, @PathVariable double weight, @PathVariable double size, @PathVariable String device) {
        return orderService.getCost(weight, size, lon1, lat1, lon2, lat2, device);
    }

    @GetMapping(value = "/order/generate")
    public Map<String, Object> generateOrder(@RequestParam("sending_address") String sendingAddress,
                                             @RequestParam("receiving_address") String receivingAddress,
                                             @RequestParam("weight") double weight,
                                             Order order) {

        double[] origin = geoCodingService.getLatLng(sendingAddress);
        double[] destination = geoCodingService.getLatLng(receivingAddress);
        double sendingLat = origin[0];
        double sendingLon = origin[1];
        double receivingLat = destination[0];
        double receivingLon = destination[1];

        Long stationId = dispatchService.getClosestStationId(sendingLat, sendingLon);
        Station station = stationRepository.findStationById(stationId);
        double stationLat = station.getLatitude();
        double stationLon = station.getLongitude();

        List<Long> pickUpTime = deliveryService.getPickUpTime(sendingLon, sendingLat, stationLon, stationLat);
        List<Long> deliveryTime = deliveryService.getDeliveryTime(sendingLon, sendingLat, receivingLon, receivingLat);
        List<Double> cost = new ArrayList<>();
        cost.add(orderService.getCost(weight, 10, sendingLon, sendingLat, receivingLon, receivingLat, "ROBOT"));
        cost.add(orderService.getCost(weight, 10, sendingLon, sendingLat, receivingLon, receivingLat, "DRONE"));

        Map<String, Object> map = new HashMap<>(2);
        map.put("pick_up_time", pickUpTime);
        map.put("delivery_time", deliveryTime);
        map.put("cost", cost);
        return map;
    }

    @PostMapping("/order/{deviceType}")
    public int addOrder(@RequestBody Order order, @PathVariable String deviceType, Principal principal) {// principal 在authentication之后存在用户线程中
        order.setUser(new User.Builder().setUsername(principal.getName()).build());
        orderService.placeOrder(order, deviceType);
        return order.getTrackId();
    }

}
