package fr.istic.taa.jaxrs.dto.response;

import java.util.ArrayList;
import java.util.List;

public class TicketResponseDto {
    private Long id;
    private String title;
    private int capacity;
    private double price;
    private String statut;
    private Long concertId;
    private List<Long> customerIds = new ArrayList<>();

    public TicketResponseDto() {
    }

    public TicketResponseDto(Long id, String title, int capacity, double price, String statut, Long concertId, List<Long> customerIds) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.price = price;
        this.statut = statut;
        this.concertId = concertId;
        if (customerIds != null) {
            this.customerIds = customerIds;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }
}
