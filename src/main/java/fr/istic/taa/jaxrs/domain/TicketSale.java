package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class TicketSale implements Serializable {

    private Long id;
    private Ticket ticket;
    private Customer customer;
    private Concert concert;
    private LocalDateTime purchaseDate;

    // Prix unitaire au moment de l'achat
    private double priceAtPurchase;

    // Nouveaux champs
    private int quantity;
    private double totalPrice;
    private String reference;
    private String status = "PURCHASED";

    public TicketSale() {
    }

    public TicketSale(
            Ticket ticket,
            Customer customer,
            Concert concert,
            LocalDateTime purchaseDate,
            double priceAtPurchase,
            int quantity,
            double totalPrice,
            String reference,
            String status
    ) {
        this.ticket = ticket;
        this.customer = customer;
        this.concert = concert;
        this.purchaseDate = purchaseDate;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.reference = reference;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Column(unique = true)
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}