package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;      
import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.domain.Customer;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("customers")
@Produces({"application/json"})
public class CustomerResource {
    private final CustomerDao dao = new CustomerDao();

    @GET    
    @Path("/{id}")
    public Customer getCustomerById(Long id) {
        Customer c = dao.findOne(id);
        if (c == null) {
            throw new RuntimeException("Customer not found for id: " + id);
        }
        return c;
    }

    @GET
    @Path("/")
    public List<Customer> getAllCustomers() {
        return dao.findAll();   
    }

    @POST
    public Response createCustomer(Customer customer) {
        dao.save(customer);
        return Response.created(URI.create("/customers/"+customer.getId())).entity(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = dao.findOne(id);
        if (existingCustomer == null) {
            throw new RuntimeException("Customer not found for id: " + id);
        }
        customer.setId(id);
        dao.update(customer);
        return customer;
    }

    @DELETE
    @Path("/{id}")
    public void deleteCustomer(Long id) {
        Customer existingCustomer = dao.findOne(id);
        if (existingCustomer == null) {
            throw new RuntimeException("Customer not found for id: " + id); 
        }
        dao.delete(existingCustomer);   
    }
    
}
