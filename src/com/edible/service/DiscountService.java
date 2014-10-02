package com.edible.service;

import com.edible.entity.Discount;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public interface DiscountService extends BasicService {

    public List<Discount> getRecentDiscounts() throws Exception;
    public List<Discount> getDiscountsByRestaurant(Long restaurantId) throws Exception;

}
