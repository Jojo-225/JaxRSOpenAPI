package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.dto.concert.CreateConcertDto;
import fr.istic.taa.jaxrs.dto.concert.UpdateConcertDto;

import java.util.List;
public class ConcertService {
    private final ConcertDao concertDao =new ConcertDao();

    public Concert createConcert(CreateConcertDto createConcertDto){
        Concert concert = new Concert();
        concert.setTopic(createConcertDto.getTopic());
        concert.setDate(createConcertDto.getDate());
        concert.setDescription(createConcertDto.getDescription());
        concert.setOrganizer(createConcertDto.getOrganizer());
        concertDao.save(concert);
        return concert;
    }

    public Concert findOne (Long id){
        return concertDao.findOne(id);
    }


    public List <Concert> findAll(){
        return concertDao.findAll();
    }

    public Concert update(UpdateConcertDto updateConcertDto, Concert concert){
        concert.setTopic(updateConcertDto.getTopic());
        concert.setDate(updateConcertDto.getDate());
        concert.setDescription(updateConcertDto.getDescription());
        concert.setOrganizer(updateConcertDto.getOrganizer());

        return concert;

    }
    public void delete(Concert concert){
        concertDao.delete(concert);
    }
}
