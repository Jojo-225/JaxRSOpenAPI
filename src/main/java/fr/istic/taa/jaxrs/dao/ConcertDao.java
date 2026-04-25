package fr.istic.taa.jaxrs.dao;
import java.util.List;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Concert;
import jakarta.persistence.TypedQuery;

public class ConcertDao extends AbstractJpaDao<Long,Concert>{
    public ConcertDao() {
        super();
        this.setClazz(Concert.class);
    }   

    public List<Concert> findLatestConcerts(int limit) {
        TypedQuery<Concert> query = this.entityManager.createQuery("SELECT c FROM Concert c ORDER BY c.date DESC", Concert.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Concert> findIncomingConcerts(int limit) {
        TypedQuery<Concert> query = this.entityManager.createQuery("SELECT c FROM Concert c WHERE c.date > CURRENT_DATE ORDER BY c.date ASC", Concert.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    // Critère pour trouver les concerts en fonction du topic, de la date, de la description,  du nom d'un artiste,  et ou d'un organisateur
    public List<Concert> findConcertsByCriteria(String topic, String date, String description, String artistName, String organizerName) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM Concert c WHERE 1=1");
        
        if (topic != null && !topic.isEmpty()) {
            queryBuilder.append(" AND c.topic LIKE :topic");
        }
        if (date != null && !date.isEmpty()) {
            queryBuilder.append(" AND c.date = :date");
        }
        if (description != null && !description.isEmpty()) {
            queryBuilder.append(" AND c.description LIKE :description");
        }
        if (artistName != null && !artistName.isEmpty()) {
            queryBuilder.append(" AND EXISTS (SELECT a FROM c.artists a WHERE a.name LIKE :artistName)");
        }
        if (organizerName != null && !organizerName.isEmpty()) {
            queryBuilder.append(" AND EXISTS (SELECT o FROM c.organizers o WHERE o.name LIKE :organizerName)");
        }

        TypedQuery<Concert> query = this.entityManager.createQuery(queryBuilder.toString(), Concert.class);
        
        if (topic != null && !topic.isEmpty()) {
            query.setParameter("topic", "%" + topic + "%");
        }
        if (date != null && !date.isEmpty()) {
            query.setParameter("date", date);
        }
        if (description != null && !description.isEmpty()) {
            query.setParameter("description", "%" + description + "%");
        }
        if (artistName != null && !artistName.isEmpty()) {
            query.setParameter("artistName", "%" + artistName + "%");
        }
        if (organizerName != null && !organizerName.isEmpty()) {
            query.setParameter("organizerName", "%" + organizerName + "%");
        }

        return query.getResultList();
    }
}
