package fr.istic.taa.jaxrs.dto.artist;
import java.util.List;

public class UpdateArtistDto {
    private String name;

    public UpdateArtistDto(String name, List<Long> concertIds){
        this.name=name;
    }

    public UpdateArtistDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

