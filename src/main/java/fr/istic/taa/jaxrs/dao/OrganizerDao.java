package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Organizer;

public class OrganizerDao extends AbstractJpaDao <Long,Organizer>{
    public OrganizerDao() {
        super();
        this.setClazz(Organizer.class);
    }
}
