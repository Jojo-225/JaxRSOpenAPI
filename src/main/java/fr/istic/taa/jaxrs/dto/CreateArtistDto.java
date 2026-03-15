package fr.istic.taa.jaxrs.dto;
import java.util.ArrayList;
import java.util.List;

import fr.istic.taa.jaxrs.domain.Concert;

public class CreateArtistDto {
    private String name;
    private List<Concert> concerts = new ArrayList<>();

    public CreateArtistDto(String name, List<Concert> concerts){
        this.name=name;
        this.concerts=concerts;
    }

    public CreateArtistDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

