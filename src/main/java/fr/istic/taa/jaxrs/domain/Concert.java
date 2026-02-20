package fr.istic.taa.jaxrs.domain;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;

@Entity
public class Concert implements Serializable {
    
    private Long id;
    private String topic;
    private LocalDateTime date;
    private String description;
    private Organizer organizer;
    private List<Ticket> tickets = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();

    public Concert(String topic, LocalDateTime date, String description, Organizer organizer) {
        this.topic = topic;
        this.date = date;
        this.description = description;
        this.organizer = organizer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
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

    @ManyToOne
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

    @OneToMany(mappedBy = "concert", cascade = CascadeType.PERSIST)
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

   @ManyToMany (mappedBy = "concerts", cascade = CascadeType.PERSIST)
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