package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ArtistDao;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.dto.CreateArtistDto;
import fr.istic.taa.jaxrs.dto.UpdateArtistDto;

import java.util.List;

public class ArtistService {

    private final ArtistDao artistDao = new ArtistDao();

    public Artist createArtist(CreateArtistDto createArtistDto) {
       Artist artist = new Artist(null);
       artist.setName(createArtistDto.getName());
       artistDao.save(artist);
       return artist;

    }

    public Artist findOne(Long id){
        return artistDao.findOne(id);
    }
    
    public List<Artist> findAll() {
        return artistDao.findAll();
    }



    public Artist update(UpdateArtistDto updateArtistDto, Artist artist) {
        artist.setName(updateArtistDto.getName());
        return artistDao.update(artist);
    }


    public void delete(Artist artist) {
        artistDao.delete(artist);
    }
    
}
