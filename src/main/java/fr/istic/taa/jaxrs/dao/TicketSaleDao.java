package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.TicketSale;

import java.util.List;

public class TicketSaleDao extends AbstractJpaDao<Long, TicketSale> {

    public TicketSaleDao() {
        super();
        this.setClazz(TicketSale.class);
    }

    public List<TicketSale> findLatestByOrganizerId(Long organizerId, int limit) {
        return this.entityManager
                .createQuery(
                        "SELECT s FROM TicketSale s WHERE s.concert.organizer.id = :organizerId ORDER BY s.purchaseDate DESC",
                        TicketSale.class
                )
                .setParameter("organizerId", organizerId)
                .setMaxResults(Math.max(limit, 0))
                .getResultList();
    }
}

