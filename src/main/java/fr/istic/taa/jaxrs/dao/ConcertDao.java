package fr.istic.taa.jaxrs.dao;
import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Concert;   

public class ConcertDao extends AbstractJpaDao<Long,Concert>{
    public ConcertDao() {
        super();
        this.setClazz(Concert.class);
    }   


}
