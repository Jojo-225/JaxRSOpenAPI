package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ArtistDao;
import fr.istic.taa.jaxrs.domain.Artist;
import java.util.List;

public class ArtistService {
    public ArtistDao dao = new ArtistDao();

    public Artist getById(Long id) {
        Artist a = dao.findOne(id);
        return a;   
    }
    
    public List<Artist> getAllArtists() {
        return dao.findAll();
    }

    public void create(Artist artist) {
        dao.save(artist);
    }

    public void update(Artist artist) {
        dao.update(artist);
    }

    public void delete(Artist artist) {
        dao.delete(artist);
    }
    
}
