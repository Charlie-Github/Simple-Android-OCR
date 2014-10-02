package com.edible.service;

import com.edible.entity.Restaurant;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public interface RestaurantService extends BasicService {

    public List<Restaurant> searchRestaurantsByName(String name) throws Exception;
    public List<Restaurant> searchRestaurantsByType(String type) throws Exception;
    public List<Restaurant> searchRestaurantsByCoordinates(Double latitude, Double longitude) throws Exception;

}
