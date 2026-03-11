package fr.istic.taa.jaxrs;
 
import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;

import fr.istic.taa.jaxrs.domain.*;

import jakarta.persistence.EntityManager;

import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;
 
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.logging.Logger;



 
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



            // // Création d'un concert + 2 tickets
            // createAdmin();
    
            // Concert concert = createConcert("Concert de rock", LocalDateTime.now().plusDays(7), "Super concert!", null);
            // concert.setTickets(List.of(

            //         createTicket("VIP",50,"libre", concert, List.of(customer1)),

            //         createTicket("VIP",50,"libre", concert, List.of(customer1))

            // ));

            // // Artistes

            // var artist = createArtist("The Rolling Stones", List.of(concert));

            // manager.persist(artist);
            
            // concert.getArtists().add(artist);

            // manager.persist(concert);
 
            // Création userisateurs

            // manager.persist(createAdmin());

            // manager.persist(createOrganizer());

            // manager.persist(createUser());


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
 
    private Artist createArtist(String name, List<Concert> concerts) {

        Artist artist = new Artist(name, concerts);

        artist.setName("The Rolling Stones");

       artist.setConcerts(concerts);

        

        return artist;

    }
 
    private User createUser() {

        User user = new User();

        user.setLastName("DUPONT");

        user.setFirstName("Michel");

        user.setDateOfBirth(LocalDate.of(1990, 1, 1));

        user.setMail("michel.dupont@yopmail.com");

        user.setPassword("password123");

        return user;

    }
 
    private Admin createAdmin() {

        // for (int i = 0; i < 2; i++) {
        //     Admin admin = new Admin("admin"+i, "admin"+i, null, "admin"+i+"@test.xyz", "password"+i);
        //     manager.persist(admin);

        // }

        Admin admin = new Admin();

        admin.setLastName("LECHEF");

        admin.setFirstName("Baptiste");

        admin.setDateOfBirth(LocalDate.of(1980, 7, 25));

        admin.setMail("baptiste.lechef@yopmail.com");
 
        admin.setPassword("adminpass456");

        return admin;

    }
 
    private Organizer createOrganizer() {

        Organizer organizer = new Organizer();

        organizer.setLastName("COMBOURG");

        organizer.setFirstName("Adeline");

        organizer.setDateOfBirth(LocalDate.of(1990, 3, 17));

        organizer.setMail("adeline.combourg2@yopmail.com");
 
        organizer.setPassword("organizerpass789");

        return organizer;
    }
 
    private Concert createConcert(String topic, LocalDateTime date, String description, Organizer organizer) {

        Concert concert = new Concert(topic, date, description, organizer);

        concert.setTopic("Concert de rock");

        concert.setDate(LocalDateTime.now().plusDays(7));

        concert.setDescription("Super concert!");

        concert.setOrganizer(organizer);

        return concert;

    }
    private Customer createCustomer(String lastName, String firstName, String mail, String password) {
       
        Customer customer = new Customer(lastName, firstName, mail, password);
        
        customer.setLastName("MARTIN");

        customer.setFirstName("Sophie");

        customer.setDateOfBirth(LocalDate.of(1995, 5, 10));

        customer.setMail("sophie.martin@yopmail.com");

        return customer;
    

    }

    private Ticket createTicket(String title, int capacity, String statut, Concert concert ){

        Ticket ticket = new Ticket("VIP",50,"libre", concert);

        ticket.setTitle(title);

        ticket.setCapacity(capacity);

        ticket.setStatut(statut);
        
        ticket.setConcert(concert);

        return ticket;

    }

}
 