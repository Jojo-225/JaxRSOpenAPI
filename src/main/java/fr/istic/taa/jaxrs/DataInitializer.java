package fr.istic.taa.jaxrs;
 
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import fr.istic.taa.jaxrs.utils.PasswordUtil;



 
public class DataInitializer {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    private static final AtomicBoolean alreadyInitializedThisJvm = new AtomicBoolean(false);
    

 
    private DataInitializer() {}
 
    public static void initialize() {
        new DataInitializer()._initialize();
    }

    public static void initializeIfEmpty() {
        ensureTicketPriceColumnExists();

        if (alreadyInitializedThisJvm.get()) {
            return;
        }
        if (!isEmpty()) {
            logger.info("Database already contains data. Skipping default initialization.");
            alreadyInitializedThisJvm.set(true);
            return;
        }
        initialize();
        alreadyInitializedThisJvm.set(true);
    }

    private static void ensureTicketPriceColumnExists() {
        EntityManager manager = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = manager.getTransaction();

        try {
            if (!tx.isActive()) {
                tx.begin();
            }

            manager.createNativeQuery("ALTER TABLE Ticket ADD COLUMN PRICE DOUBLE DEFAULT 0").executeUpdate();

            tx.commit();
            logger.info("Database schema update: PRICE column added to Ticket table.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            // Expected when column already exists; keep startup resilient.
            logger.info("Database schema check: Ticket.PRICE already exists or cannot be added (" + e.getMessage() + ")");
        }
    }
 
    private void _initialize() {

        EntityManager manager = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < 2; i++) {
				Admin admin = new Admin("admin"+i, "admin"+i, null, "admin"+i+"@test.xyz", PasswordUtil.hash("password"+i));
				manager.persist(admin);
			}

			for (int i = 0; i < 5; i++) {
				Organizer organizer = new Organizer("organizer"+i, "organizer"+i, null, "organizer"+i+"@test.xyz", PasswordUtil.hash("password"+i));
				manager.persist(organizer);

				Concert concert = new Concert("Concert "+i, LocalDateTime.now(), "Description du concert "+i, organizer);
				manager.persist(concert);

				Artist artist = new Artist("Artist "+i);
				artist.addConcert(concert);
				manager.persist(artist);

				Ticket ticket = new Ticket("Ticket standard", 100, 49.99, "available", concert);
				manager.persist(ticket);
			}
			
			for (int i = 0; i < 50; i++) {
				Customer customer = new Customer("CLName"+i, "CFName"+i, null,"customer"+i+"@test.xyz", PasswordUtil.hash("password"+i));
				manager.persist(customer);
			}
        } catch (Exception e) {
            tx.rollback();
            logger.severe(e.getMessage());
            return;
        }
        tx.commit();

    }

    public static boolean isEmpty() {
        EntityManager manager = EntityManagerHelper.getEntityManager();
        Long count = manager.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        return count == 0L;
    }
}
 
