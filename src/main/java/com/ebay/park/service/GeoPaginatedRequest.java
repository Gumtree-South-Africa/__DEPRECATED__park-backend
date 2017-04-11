package com.ebay.park.service;

/**
 * @author Julieta Salvadó
 */
public class GeoPaginatedRequest extends PaginatedRequest {
    private Double latitude;
    private Double longitude;
    private Double radius;

    public GeoPaginatedRequest(String token, String language, Integer page, Integer pageSize) {
        super(token, language, page, pageSize);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
