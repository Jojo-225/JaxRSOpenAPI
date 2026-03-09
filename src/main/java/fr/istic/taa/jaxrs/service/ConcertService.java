package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.domain.Concert;
import java.util.List;
public class ConcertService {
    public ConcertDao dao = new ConcertDao();

    public Concert getById(Long id){

        return dao.findOne(id);

    }

    public List <Concert> getAllConcerts(){
        return dao.findAll();
    }
    public void create(Concert concert){
        dao.save(concert);
    }

    public void update(Concert concert){
        dao.update(concert);
    }
}
