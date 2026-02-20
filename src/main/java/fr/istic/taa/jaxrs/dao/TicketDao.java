package fr.istic.taa.jaxrs.dao;
import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Ticket;

public class TicketDao extends AbstractJpaDao<Long, Ticket> {
    public TicketDao() {
        super();
        this.setClazz(Ticket.class);
    }

}
