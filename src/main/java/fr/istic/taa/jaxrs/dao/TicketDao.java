package fr.istic.taa.jaxrs.dao;
import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import jakarta.persistence.NamedQuery;

@NamedQuery(name = "Ticket.isvalid", query = "SELECT t FROM Ticket t WHERE t.concertId = :concertId AND t.status = ")
public class TicketDao extends AbstractJpaDao<Long, Ticket> {
    public TicketDao() {
        super();
        this.setClazz(Ticket.class);
    }

    // TODO : Create a function using named queries to find if tickets exist for a specific concert, and if so, verify if is valid and return true, otherwise return false
    public boolean isValidTicketForConcert(Long concertId) {
        Long count = this.entityManager
                .createNamedQuery("Ticket.isvalid", Long.class)
                .setParameter("concertId", concertId)
                .getSingleResult();
        return count > 0;
    }
}
