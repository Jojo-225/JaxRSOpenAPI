package fr.istic.taa.jaxrs.dto.artist;
import java.util.ArrayList;
import java.util.List;

public class CreateArtistDto {
    private String name;
    private List<Long> concertIds = new ArrayList<>();

    public CreateArtistDto(String name, List<Long> concertIds){
        this.name=name;
        this.concertIds=concertIds;
    }

    public CreateArtistDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getConcertIds() {
        return concertIds;
    }

    public void addConcertId(Long concertId) {
        this.concertIds.add(concertId);
    }

    public void removeConcertId(Long concertId) {
        this.concertIds.remove(concertId);
    }
    
    public void setConcertIds(List<Long> concertIds) {
        this.concertIds = concertIds;
    }   

}

