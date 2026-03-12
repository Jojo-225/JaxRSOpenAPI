package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Concert;
import java.util.List;

public class OrganizerDao extends AbstractJpaDao <Long,Organizer>{
    public OrganizerDao() {
        super();
        this.setClazz(Organizer.class);
    }

    public List<Concert> findConcertsByOrganizerId(Long organizerId) {
        return this.entityManager
                .createQuery("SELECT c FROM Concert c WHERE c.organizer.id = :organizerId", Concert.class)
                .setParameter("organizerId", organizerId)
                .getResultList();
    }


}
