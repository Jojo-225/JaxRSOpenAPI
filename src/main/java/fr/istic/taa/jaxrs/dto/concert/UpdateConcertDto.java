package fr.istic.taa.jaxrs.dto.concert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;

public class UpdateConcertDto {
    
    private String topic;
    private LocalDateTime date;
    private String description;
    private Organizer organizer;
    private List<Ticket> tickets = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    public UpdateConcertDto(){

    }

    public UpdateConcertDto(String topic, LocalDateTime date, String description, Organizer organizer) {
        this.topic = topic;
        this.date = date;
        this.description = description;
        this.organizer = organizer;
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

    public Organizer getOrganizer() {
        return organizer;
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

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void removeArtist(Artist artist) {
        this.artists.remove(artist);
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;     
    }
}

