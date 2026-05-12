package fr.istic.taa.jaxrs.dto.alert;

import java.time.LocalDateTime;

public class ConcertAlertResponseDto {
    private Long id;
    private Long userId;
    private String topic;
    private String artistName;
    private String date;
    private String description;
    private String organizerName;
    private Double priceMin;
    private Double priceMax;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastNotifiedAt;

    public ConcertAlertResponseDto() {
    }

    public ConcertAlertResponseDto(Long id, Long userId, String topic, String artistName, String date, String description,
                                   String organizerName, Double priceMin, Double priceMax,
                                   boolean active, LocalDateTime createdAt, LocalDateTime lastNotifiedAt) {
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        this.artistName = artistName;
        this.date = date;
        this.description = description;
        this.organizerName = organizerName;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.active = active;
        this.createdAt = createdAt;
        this.lastNotifiedAt = lastNotifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastNotifiedAt() {
        return lastNotifiedAt;
    }

    public void setLastNotifiedAt(LocalDateTime lastNotifiedAt) {
        this.lastNotifiedAt = lastNotifiedAt;
    }
}

