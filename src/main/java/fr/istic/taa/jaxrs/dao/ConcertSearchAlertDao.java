package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.ConcertSearchAlert;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertSearchAlertDao extends AbstractJpaDao<Long, ConcertSearchAlert> {

    public ConcertSearchAlertDao() {
        super();
        this.setClazz(ConcertSearchAlert.class);
    }

    public List<ConcertSearchAlert> findByUserId(Long userId) {
        return this.entityManager
                .createQuery("SELECT a FROM ConcertSearchAlert a WHERE a.userId = :userId ORDER BY a.createdAt DESC", ConcertSearchAlert.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<ConcertSearchAlert> findActiveAlerts() {
        return this.entityManager
                .createQuery("SELECT a FROM ConcertSearchAlert a WHERE a.active = true", ConcertSearchAlert.class)
                .getResultList();
    }

    public ConcertSearchAlert markLastNotified(Long alertId, LocalDateTime dateTime) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            ConcertSearchAlert alert = this.entityManager.find(ConcertSearchAlert.class, alertId);
            if (alert == null) {
                tx.commit();
                return null;
            }
            alert.setLastNotifiedAt(dateTime);
            ConcertSearchAlert updated = this.entityManager.merge(alert);
            tx.commit();
            return updated;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}

