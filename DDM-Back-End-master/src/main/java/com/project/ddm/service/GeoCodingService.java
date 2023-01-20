package com.project.ddm.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.project.ddm.exception.GeoCodingException;
import com.project.ddm.exception.InvalidAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingService {

    private GeoApiContext context;

    @Autowired
    public GeoCodingService(GeoApiContext context) {
        this.context = context;
    }

    /**
     *
     * @param address
     * @return  an array of latitude and longitude of address
     * @throws GeoCodingException
     */
    public double[] getLatLng(String address) throws GeoCodingException {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
            if (result.partialMatch) {
                throw new InvalidAddressException("address entered is invalid. Please enter correct address");
            }

            return new double[]{result.geometry.location.lat, result.geometry.location.lng};
        } catch (IOException | ApiException | InterruptedException ex) { // exception thrown by await()
            ex.printStackTrace();
            throw new GeoCodingException("failed to encode stay address");
        }
    }
}
