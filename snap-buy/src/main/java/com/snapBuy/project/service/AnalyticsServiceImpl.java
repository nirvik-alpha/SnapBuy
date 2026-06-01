package com.snapBuy.project.service;

import com.snapBuy.project.payload.AnalyticsResponse;
import com.snapBuy.project.repositories.OrderRepository;
import com.snapBuy.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of AnalyticsService.
 * Responsible for collecting and aggregating data
 * from different repositories to generate business insights for the admin dashboard.
 */

@Service
public class AnalyticsServiceImpl implements AnalyticsService{

    // Repository for product-related data
    @Autowired
    private ProductRepository productRepository;


    // Repository for order-related data
    @Autowired
    private OrderRepository orderRepository;


    /**
     * Generate analytics data for admin dashboard.
     * Aggregates:
     * - Total number of products
     * - Total number of orders
     * - Total revenue from all orders
     */

    @Override
    public AnalyticsResponse getAnalyticsData() {
        AnalyticsResponse response = new AnalyticsResponse();

        long productCount = productRepository.count();
        long totalOrders = orderRepository.count();;
        Double totalRevenue = orderRepository.getTotalRevenue();

        response.setProductCount(String.valueOf(productCount));
        response.setTotalOrders(String.valueOf(totalOrders));
        response.setTotalRevenue(String.valueOf(totalRevenue != null ? totalRevenue : 0));
        return response;
    }
}