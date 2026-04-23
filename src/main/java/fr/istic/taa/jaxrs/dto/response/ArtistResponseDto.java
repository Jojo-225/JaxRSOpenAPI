package fr.istic.taa.jaxrs.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ArtistResponseDto {
    private Long id;
    private String name;
    private List<Long> concertIds = new ArrayList<>();

    public ArtistResponseDto() {
    }

    public ArtistResponseDto(Long id, String name, List<Long> concertIds) {
        this.id = id;
        this.name = name;
        if (concertIds != null) {
            this.concertIds = concertIds;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setConcertIds(List<Long> concertIds) {
        this.concertIds = concertIds;
    }
}
