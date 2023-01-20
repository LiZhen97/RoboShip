package com.project.ddm.controller;

import com.project.ddm.model.Order;
import com.project.ddm.model.User;
import com.project.ddm.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    private UserInfoService userInfoService;
    @Autowired
    public UserController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;
    }

    @GetMapping(value="/user/historyorders")
    public List<Order> getOrdersByUserId(Principal principal) {
        return userInfoService.findUserHistoryOrders(principal.getName());
    }

    @GetMapping(value="/user/information")
    public User getUserInfo(Principal principal) {
        return userInfoService.findUserInformation(principal.getName());
    }
}
