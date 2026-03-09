package fr.istic.taa.jaxrs;
 
import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;

import fr.istic.taa.jaxrs.domain.*;

import jakarta.persistence.EntityManager;

import jakarta.persistence.EntityTransaction;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.HashSet;

import java.util.List;

import java.util.Set;

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

            // Création d'un concert + 2 tickets

            Concert concert = createConcert();

            concert.setTickets(List.of(

                    createTicket("A001", 42.0d, concert),

                    createTicket("E450", 37.5d, concert)

            ));

            // Artistes

            var artistes = createArtistes();

            artistes.forEach(manager::persist);

            concert.getArtistes().addAll(artistes);

            manager.persist(concert);
 
            // Création utilisateurs

            manager.persist(createAdministrateur());

            manager.persist(createOrganisateur());

            manager.persist(createUtilisateur());

        } catch (Exception e) {

            tx.rollback();

            logger.severe(e.getMessage());

        }

        tx.commit();

    }
 
    private Set<Artiste> createArtistes() {

        Set<Artiste> artistes = new HashSet<>();

        {

            Artiste artiste = new Artiste();

            artiste.setNomScene("Machin");

            artiste.setNationalite("française");

            artiste.setPopularite(3);

            artiste.setSiteWeb("https://machin.co/tournee");

            artiste.setDateNaissance(LocalDate.of(2001, 3, 6));

            artistes.add(artiste);

        }

        {

            Artiste artiste = new Artiste();

            artiste.setNomScene("Truc");

            artiste.setNationalite("anglaise");

            artiste.setPopularite(4);

            artiste.setDateNaissance(LocalDate.of(1997, 9, 16));

            artistes.add(artiste);

        }

        return artistes;

    }
 
    private Utilisateur createUtilisateur() {

        Utilisateur util = new Utilisateur();

        util.setNom("DUPONT");

        util.setPrenom("Michel");

        util.setDateNaissance(LocalDate.of(1990, 1, 1));

        util.setEmail("michel.dupont@yopmail.com");

        util.setDateInscription(LocalDate.now());

        util.setCreditCompte(45.0d);

        util.setPreferenceNotificationEmail(false);

        util.setPreferenceNotificationPush(true);

        return util;

    }
 
    private Administrateur createAdministrateur() {

        Administrateur admin = new Administrateur();

        admin.setNom("LECHEF");

        admin.setPrenom("Baptiste");

        admin.setDateNaissance(LocalDate.of(1980, 7, 25));

        admin.setEmail("baptiste.lechef@yopmail.com");
 
        admin.setActif(true);

        admin.setDateNomination(LocalDate.of(2025, 12, 31));

        return admin;

    }
 
    private Organisateur createOrganisateur() {

        Organisateur orga = new Organisateur();

        orga.setNom("COMBOURG");

        orga.setPrenom("Adeline");

        orga.setDateNaissance(LocalDate.of(1990, 3, 17));

        orga.setEmail("adeline.combourg2@yopmail.com");
 
        orga.setActif(true);

        orga.setNomStructure("Rock en scène");

        orga.setNumeroSiret("523 299 410 00531");

        orga.setAdresseSiege("18, chemin de Faivre, 89731 AUBERT");

        return orga;

    }
 
    private Concert createConcert() {

        Concert concert = new Concert();

        concert.setCapacite(50L);

        concert.setDate(LocalDateTime.now().plusDays(7));

        concert.setPopularite(3.5f);

        concert.setGenre("VARIETE");

        concert.setDescription("Super concert!");

        return concert;

    }
 
    private Ticket createTicket(String numeroPlace, Double prixUnitaire, Concert concert) {

        Ticket ticket = new Ticket("A001", 42.0d, concert);

        ticket.setNumeroPlace(numeroPlace);

        ticket.setPrixUnitaire(prixUnitaire);

        ticket.setStatut(StatutTicketEnum.ACHETE);

        ticket.setDateAchat(LocalDateTime.now());

        return ticket;

    }

}
 