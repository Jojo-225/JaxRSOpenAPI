package fr.istic.taa.jaxrs.dto.concert;

import java.time.LocalDateTime;

public class UpdateConcertDto {
    
    private String topic;
    private LocalDateTime date;
    private String description;

    public UpdateConcertDto(){

    }

    public UpdateConcertDto(String topic, LocalDateTime date, String description) {
        this.topic = topic;
        this.date = date;
        this.description = description;
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

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

