package fr.istic.taa.jaxrs.service;
import java.util.List;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.ticket.CreateTicketDto;
import fr.istic.taa.jaxrs.dto.ticket.UpdateTicketDto;

public class TicketService {
    private final TicketDao ticketDao =new TicketDao();
    private final ConcertDao concertDao = new ConcertDao();

    public Ticket createTicket(CreateTicketDto createTicketDto){

        Concert concert = this.concertDao.findOne(createTicketDto.getConcert());
        if(concert == null){
            throw new IllegalArgumentException("Concert not found");
        }
        Ticket ticket=new Ticket();
        ticket.setTitle(createTicketDto.getTitle());
        ticket.setCapacity(createTicketDto.getCapacity());
        ticket.setStatut(createTicketDto.getStatut());
        ticket.setConcert(concert);
        ticketDao.save(ticket);
        return ticket;

    }

    public Ticket findOne(Long id) {
        return ticketDao.findOne(id);
    }

    public List<Ticket> findAll() {
        return ticketDao.findAll();
    }

    public List<Ticket> getMyTickets() {
        // Temporary fallback: ownership filtering will be plugged to current user context.
        return ticketDao.findAll();
    }

    public Ticket update(UpdateTicketDto updateTicketDto, Ticket ticket) {
        ticket.setTitle(updateTicketDto.getTitle());
        ticket.setCapacity(updateTicketDto.getCapacity());
        ticket.setStatut(updateTicketDto.getStatut());
       
        return ticketDao.update(ticket);
    }
    
    public void delete(Ticket ticket){
        ticketDao.delete(ticket);
    }

}
