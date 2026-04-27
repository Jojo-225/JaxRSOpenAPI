package fr.istic.taa.jaxrs.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertResponseDto {
    private Long id;
    private String topic;
    private LocalDateTime date;
    private String description;
    private Long organizerId;
    private Double minPrice;
    private List<Long> ticketIds = new ArrayList<>();
    private List<Long> artistIds = new ArrayList<>();

    public ConcertResponseDto() {
    }

    public ConcertResponseDto(Long id, String topic, LocalDateTime date, String description,
                              Long organizerId, Double minPrice, List<Long> ticketIds, List<Long> artistIds) {
        this.id = id;
        this.topic = topic;
        this.date = date;
        this.description = description;
        this.organizerId = organizerId;
        this.minPrice = minPrice;
        if (ticketIds != null) {
            this.ticketIds = ticketIds;
        }
        if (artistIds != null) {
            this.artistIds = artistIds;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public List<Long> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Long> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public List<Long> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<Long> artistIds) {
        this.artistIds = artistIds;
    }
}
