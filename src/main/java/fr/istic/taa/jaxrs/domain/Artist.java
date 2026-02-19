package fr.istic.taa.jaxrs.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Artist {
    private Long id;
    private String name;
    private List<Concert> concerts = new ArrayList<>();

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    public List<Concert> getConcerts() {
        return concerts;
    }

    public void addConcert(Concert concert) {
        this.concerts.add(concert);
    }

    public void removeConcert(Concert concert) {
        this.concerts.remove(concert);
    }
    
    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }   

}
