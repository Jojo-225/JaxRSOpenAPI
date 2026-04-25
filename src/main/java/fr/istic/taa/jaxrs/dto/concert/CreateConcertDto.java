package fr.istic.taa.jaxrs.dto.concert;

import java.time.LocalDateTime;

public class CreateConcertDto {
    private String topic;
    private LocalDateTime date;
    private String description;
    private Long organizerId;
    public CreateConcertDto(){

    }

    public CreateConcertDto(String topic, LocalDateTime date, String description, Long organizerId) {
        this.topic = topic;
        this.date = date;
        this.description = description;
        this.organizerId = organizerId;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
    
    public Long getOrganizerId() {
        return organizerId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

}
