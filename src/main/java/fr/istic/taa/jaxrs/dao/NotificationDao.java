package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Notification;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class NotificationDao extends AbstractJpaDao<Long, Notification> {

    public NotificationDao() {
        super();
        this.setClazz(Notification.class);
    }

    public List<Notification> findByUserId(Long userId) {
        return this.entityManager
                .createQuery(
                        "SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createdAt DESC",
                        Notification.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    public long countUnreadByUserId(Long userId) {
        return this.entityManager
                .createQuery(
                        "SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.read = false",
                        Long.class
                )
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public Notification markAsRead(Long notificationId) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();

            Notification notification = this.entityManager.find(Notification.class, notificationId);
            if (notification == null) {
                transaction.commit();
                return null;
            }

            notification.setRead(true);
            Notification updated = this.entityManager.merge(notification);

            transaction.commit();
            return updated;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public int markAllAsRead(Long userId) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();

            int updated = this.entityManager
                    .createQuery("UPDATE Notification n SET n.read = true WHERE n.userId = :userId AND n.read = false")
                    .setParameter("userId", userId)
                    .executeUpdate();

            transaction.commit();
            return updated;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
