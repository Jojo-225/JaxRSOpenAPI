package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.dto.concert.CreateConcertDto;
import fr.istic.taa.jaxrs.dto.concert.UpdateConcertDto;

import java.util.List;
public class ConcertService {
    private final ConcertDao concertDao =new ConcertDao();
    private final OrganizerDao organizerDao = new OrganizerDao();

    public Concert createConcert(CreateConcertDto createConcertDto){
        Organizer organizer = this.organizerDao.findOne(createConcertDto.getOrganizer());
       if(organizer == null){
        throw new IllegalArgumentException("Organizer not found");
       }
        Concert concert = new Concert();
        concert.setTopic(createConcertDto.getTopic());
        concert.setDate(createConcertDto.getDate());
        concert.setDescription(createConcertDto.getDescription());
        concert.setOrganizer(organizer);
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
        return concert;

    }
    public void delete(Concert concert){
        concertDao.delete(concert);
    }
}
