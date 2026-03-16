package fr.istic.taa.jaxrs.service;
import java.util.List;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.CreateTicketDto;
import fr.istic.taa.jaxrs.dto.UpdateTicketDto;

public class TicketService {
    private final TicketDao ticketDao =new TicketDao();

    public Ticket createTicket(CreateTicketDto createTicketDto){
        Ticket ticket=new Ticket();
        ticket.setTitle(createTicketDto.getTitle());
        ticket.setCapacity(createTicketDto.getCapacity());
        ticket.setStatut(createTicketDto.getStatut());
        ticket.setConcert(createTicketDto.getConcert());
        ticketDao.save(ticket);
        return ticket;

    }

    public Ticket findOne(Long id) {
        return ticketDao.findOne(id);
    }

    public List<Ticket> findAll() {
        return ticketDao.findAll();
    }

    public Ticket update(UpdateTicketDto updateTicketDto, Ticket ticket) {
        ticket.setTitle(updateTicketDto.getTitle());
        ticket.setCapacity(updateTicketDto.getCapacity());
        ticket.setStatut(updateTicketDto.getStatut());
        ticket.setConcert(updateTicketDto.getConcert());
       
        return ticketDao.update(ticket);
    }
    
    public void delete(Ticket ticket){
        ticketDao.delete(ticket);
    }

}
