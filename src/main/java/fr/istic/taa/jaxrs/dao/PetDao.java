package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Pet;

public class PetDao extends AbstractJpaDao<Long,Pet> {

    public PetDao() {
        super();
        this.setClazz(
            Pet.class
        );
    }
    
}
