package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Artist;

public class ArtistDao extends AbstractJpaDao<Long, Artist> {
    public ArtistDao() {
        super();
        this.setClazz(Artist.class);
    }
}