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
        Organizer organizer = this.organizerDao.findOne(createConcertDto.getOrganizerId());
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

    public List<Concert> getLatestConcerts() {
        return concertDao.findLatestConcerts(10); // Get the 10 latest concerts
    }

    public List<Concert> getIncomingConcerts() {
        return concertDao.findIncomingConcerts(10); // Get the 10 upcoming concerts
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

    public List<Concert> findConcertsByCriteria(String topic, String date, String description, String artistName, String organizerName, Double priceMin, Double priceMax) {
        return concertDao.findConcertsByCriteria(topic, date, description, artistName, organizerName, priceMin, priceMax);
    }
}
