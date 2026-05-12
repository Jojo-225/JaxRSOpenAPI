package fr.istic.taa.jaxrs.dao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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

    public List<Concert> findConcertsCreatedAfter(LocalDateTime createdAfter) {
        return this.entityManager
                .createQuery("SELECT c FROM Concert c WHERE c.createdAt >= :createdAfter ORDER BY c.createdAt ASC", Concert.class)
                .setParameter("createdAfter", createdAfter)
                .getResultList();
    }

    // Critère pour trouver les concerts en fonction du topic, de la date, de la description,  du nom d'un artiste,  et ou d'un organisateur
 public List<Concert> findConcertsByCriteria(String topic,String date,String description,String artistName,String organizerName, Double priceMin,Double priceMax) {
    StringBuilder queryBuilder = new StringBuilder(
            "SELECT DISTINCT c FROM Concert c " +
            "LEFT JOIN c.artists a " +
            "LEFT JOIN c.organizer o " +
            "LEFT JOIN c.tickets t " +
            "WHERE 1=1"
    );

    if (topic != null && !topic.trim().isEmpty()) {
        queryBuilder.append(" AND LOWER(c.topic) LIKE :topic");
    }

    if (date != null && !date.trim().isEmpty()) {
        queryBuilder.append(" AND c.date >= :startDate AND c.date < :endDate");
    }

    if (description != null && !description.trim().isEmpty()) {
        queryBuilder.append(" AND LOWER(c.description) LIKE :description");
    }
    if (priceMin != null) {
        queryBuilder.append(" AND t.price >= :priceMin");
    }

    if (priceMax != null) {
        queryBuilder.append(" AND t.price <= :priceMax");
    }

    if (artistName != null && !artistName.trim().isEmpty()) {
        queryBuilder.append(" AND LOWER(a.name) LIKE :artistName");
    }

    if (organizerName != null && !organizerName.trim().isEmpty()) {
        queryBuilder.append(" AND (LOWER(o.lastName) LIKE :organizerName OR LOWER(o.firstName) LIKE :organizerName)");
    }

    queryBuilder.append(" ORDER BY c.date ASC");

    TypedQuery<Concert> query = this.entityManager.createQuery(queryBuilder.toString(), Concert.class);

    if (topic != null && !topic.trim().isEmpty()) {
        query.setParameter("topic", "%" + topic.trim().toLowerCase() + "%");
    }

    if (date != null && !date.trim().isEmpty()) {
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim());

            LocalDateTime startDate = parsedDate.atStartOfDay();
            LocalDateTime endDate = parsedDate.plusDays(1).atStartOfDay();

            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
        } catch (DateTimeParseException e) {
            return List.of();
        }
    }

    if (description != null && !description.trim().isEmpty()) {
        query.setParameter("description", "%" + description.trim().toLowerCase() + "%");
    }
    if (priceMin != null) {
        query.setParameter("priceMin", priceMin);
    }

    if (priceMax != null) {
        query.setParameter("priceMax", priceMax);
    }

    if (artistName != null && !artistName.trim().isEmpty()) {
        query.setParameter("artistName", "%" + artistName.trim().toLowerCase() + "%");
    }

    if (organizerName != null && !organizerName.trim().isEmpty()) {
        query.setParameter("organizerName", "%" + organizerName.trim().toLowerCase() + "%");
    }

    return query.getResultList();
}
}
