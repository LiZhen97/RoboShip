package com.project.ddm.controller;

import com.project.ddm.model.Order;
import com.project.ddm.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrackOrderController {

    private TrackService trackService;

    @Autowired
    public TrackOrderController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/track/{trackId}")
    public Order getOrder(@PathVariable Long trackId) {
        return trackService.findByTrackId(trackId);
    }

    @GetMapping(value="/historyorders/{userId}")
    public List<Order> historyOrders(@PathVariable Long userId) {
        return trackService.findByUser(userId);
    }
}
