package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Ticket;

public class TicketDao extends AbstractJpaDao<Long, Ticket> {

    public TicketDao() {
        super();
        this.setClazz(Ticket.class);
    }

    public boolean isValidTicketForConcert(Long concertId) {
        Long count = this.entityManager
                .createQuery(
                        "SELECT COUNT(t) FROM Ticket t " +
                                "WHERE t.concert.id = :concertId " +
                                "AND LOWER(t.statut) = 'available' " +
                                "AND t.capacity > 0",
                        Long.class
                )
                .setParameter("concertId", concertId)
                .getSingleResult();

        return count > 0;
    }
}