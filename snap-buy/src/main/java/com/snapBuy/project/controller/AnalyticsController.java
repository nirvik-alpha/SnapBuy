package com.snapBuy.project.controller;

import com.snapBuy.project.payload.AnalyticsResponse;
import com.snapBuy.project.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyticsController {

    // Service layer responsible for analytics calculations

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Retrieve dashboard analytics data.
     * Returns aggregated statistics required for administrative reporting and monitoring.
     */
    @GetMapping("/admin/app/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        AnalyticsResponse response = analyticsService.getAnalyticsData();
        return new ResponseEntity<AnalyticsResponse>(response, HttpStatus.OK);
    }

}
