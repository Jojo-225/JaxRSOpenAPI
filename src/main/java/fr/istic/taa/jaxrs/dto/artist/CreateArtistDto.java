package fr.istic.taa.jaxrs.dto.artist;

public class CreateArtistDto {
    private String name;
    private Long concertId;

    public CreateArtistDto(String name, Long concertId){
        this.name=name;
        this.concertId = concertId;
    }

    public CreateArtistDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getConcertId() {
        return this.concertId;
    }
    
    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }   

}

