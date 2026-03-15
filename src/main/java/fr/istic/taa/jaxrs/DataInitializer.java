package fr.istic.taa.jaxrs;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;



 
public class DataInitializer {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
 
    private DataInitializer() {}
 
    public static void initialize() {
        new DataInitializer()._initialize();
    }
 
    private void _initialize() {

        EntityManager manager = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < 2; i++) {
				Admin admin = new Admin("admin"+i, "admin"+i, null, "admin"+i+"@test.xyz", "password"+i);
				manager.persist(admin);
			}

			for (int i = 0; i < 5; i++) {
				Organizer organizer = new Organizer("organizer"+i, "organizer"+i, null, "organizer"+i+"@test.xyz", "password"+i);
				manager.persist(organizer);

				Concert concert = new Concert("Concert "+i, LocalDateTime.now(), "Description du concert "+i, organizer);
				manager.persist(concert);

				Artist artist = new Artist("Artist "+i);
				artist.addConcert(concert);
				manager.persist(artist);

				Ticket ticket = new Ticket("Ticket standard", 100, "available", concert);
				manager.persist(ticket);
			}
			
			for (int i = 0; i < 50; i++) {
				Customer customer = new Customer("CLName"+i, "CFName"+i, "customer"+i+"@test.xyz", "password");
				manager.persist(customer);
			}
        } catch (Exception e) {
            tx.rollback();
            logger.severe(e.getMessage());
        }
        tx.commit();

    }

    public static boolean isEmpty() {
        EntityManager manager = EntityManagerHelper.getEntityManager();
        Integer count = manager.createQuery("SELECT COUNT(a) FROM Admin a", Long.class).getSingleResult().intValue();
        System.out.println("Admin count: " + count);
        return count == 0;
    }
}
 