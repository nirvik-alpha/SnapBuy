package com.snapBuy.project.service;

import com.snapBuy.project.payload.AnalyticsResponse;

/**
 * Service interface for application analytics.
 * Provides methods to retrieve business statistics such as total products, orders, and revenue.
 */
public interface AnalyticsService {

    AnalyticsResponse getAnalyticsData();
}