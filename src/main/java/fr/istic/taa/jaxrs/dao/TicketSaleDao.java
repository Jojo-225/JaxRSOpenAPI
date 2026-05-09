package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.TicketSale;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TicketSaleDao extends AbstractJpaDao<Long, TicketSale> {

    public TicketSaleDao() {
        super();
        this.setClazz(TicketSale.class);
    }

    public TicketSale buyTicket(Long customerId, Long ticketId, int quantity) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();

            Customer customer = this.entityManager.find(Customer.class, customerId);
            if (customer == null) {
                throw new IllegalArgumentException("CUSTOMER_NOT_FOUND");
            }

            Ticket ticket = this.entityManager.find(Ticket.class, ticketId);
            if (ticket == null) {
                throw new IllegalArgumentException("TICKET_NOT_FOUND");
            }

            if (quantity <= 0) {
                throw new IllegalArgumentException("INVALID_QUANTITY");
            }

            if (!"available".equalsIgnoreCase(ticket.getStatut()) || ticket.getCapacity() < quantity) {
                throw new IllegalStateException("TICKET_UNAVAILABLE");
            }

            // Diminuer la capacité selon la quantité achetée
            ticket.setCapacity(ticket.getCapacity() - quantity);

            if (ticket.getCapacity() <= 0) {
                ticket.setStatut("soldout");
            }

            /*
             * On garde la relation ManyToMany uniquement pour dire :
             * "ce client a déjà acheté au moins une fois ce type de ticket".
             * Mais la vraie quantité est stockée dans TicketSale.
             */
            boolean alreadyLinked = ticket.getCustomers()
                    .stream()
                    .anyMatch(c -> c.getId().equals(customer.getId()));

            if (!alreadyLinked) {
                ticket.addCustomer(customer);
                customer.addTicketAchete(ticket);
            }

            LocalDateTime now = LocalDateTime.now();

            double unitPrice = ticket.getPrice();
            double totalPrice = unitPrice * quantity;

            TicketSale sale = new TicketSale();
            sale.setTicket(ticket);
            sale.setCustomer(customer);
            sale.setConcert(ticket.getConcert());
            sale.setPurchaseDate(now);
            sale.setPriceAtPurchase(unitPrice);
            sale.setQuantity(quantity);
            sale.setTotalPrice(totalPrice);
            sale.setStatus("PURCHASED");
            sale.setReference(generateReference(ticket, customer, now));

            this.entityManager.persist(sale);

            transaction.commit();

            return sale;

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public List<TicketSale> findByCustomerId(Long customerId) {
        return this.entityManager
                .createQuery(
                        "SELECT s FROM TicketSale s WHERE s.customer.id = :customerId ORDER BY s.purchaseDate DESC",
                        TicketSale.class
                )
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public List<TicketSale> findByOrganizerId(Long organizerId) {
        return this.entityManager
                .createQuery(
                        "SELECT s FROM TicketSale s WHERE s.concert.organizer.id = :organizerId ORDER BY s.purchaseDate DESC",
                        TicketSale.class
                )
                .setParameter("organizerId", organizerId)
                .getResultList();
    }

    public List<TicketSale> findLatestByOrganizerId(Long organizerId, int limit) {
        return this.entityManager
                .createQuery(
                        "SELECT s FROM TicketSale s WHERE s.concert.organizer.id = :organizerId ORDER BY s.purchaseDate DESC",
                        TicketSale.class
                )
                .setParameter("organizerId", organizerId)
                .setMaxResults(Math.max(limit, 0))
                .getResultList();
    }

    private String generateReference(Ticket ticket, Customer customer, LocalDateTime now) {
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return "TICKET-" + datePart + "-" + ticket.getId() + "-" + customer.getId() + "-" + randomPart;
    }
}
