package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.net.URI;
import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.dto.user.CreateUserDto;
import fr.istic.taa.jaxrs.dto.user.UpdateUserDto;
import fr.istic.taa.jaxrs.service.CustomerService;
import fr.istic.taa.jaxrs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("/api/customers")
@Produces({"application/json"})
public class CustomerResource {
    private CustomerDao dao = new CustomerDao();
    private CustomerService customerService =new CustomerService();
    private UserService userService = new UserService();
   
    @GET    
    @Path("/{id}")
      @Operation(summary = "Get customer by ID", description = "Returns a single customer", responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval of customer"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public Response getCustomerById(@PathParam("id") Long id) {
        try{
            Customer customer = customerService.findOne(id);
            return Response.ok(customer).build();

        }catch(RuntimeException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found for id: " + id).build();
        }
    }

    @GET
    @Path("/")
    @Operation(summary = "Get all customers", description = "Returns a list of all customers", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of organizers")
    })
    public Response getAllCustomers() {
        return Response.ok(customerService.findAll()).build();
    }

    @POST
    @Path("/")
     @Operation(summary = "Create a new customer", description = "Creates a new customer", responses = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Response createCustomer(CreateUserDto createCustomerDto) {
        if (userService.findByEmail(createCustomerDto.getEmail()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An organizer with this email already exists").build();
        }
        try {
            Customer customer = customerService.createCustomer(createCustomerDto);
            return Response.created(URI.create("/customer/"+customer.getId())).entity(customer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create customer").build();
        }
    }

    @PUT
    @Path("/{id}")
     @Operation(summary = "Update an existing customer", description = "Updates an existing customer", responses = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public Response updateCustomer(@PathParam("id") Long id, UpdateUserDto updateCustomerDto) {
        Customer existingCustomer = customerService.findOne(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found for id: " + id).build();
        }
        try {
            Customer updatedCustomer = customerService.update(updateCustomerDto,existingCustomer);
            return Response.ok(updatedCustomer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update customer").build();
        }
    }

    @DELETE
    @Path("/{id}")
      @Operation(summary = "Delete an customer", description = "Deletes an customer if there are no associated tickets", responses = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete customer with associated tickets"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public Response deleteCustomer(@PathParam("id")Long id) {
        Customer existingCustomer = customerService.findOne(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!dao.findTicketsByCustomerId(id).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot delete customer with associated tickets. Please remove or reassign the tickets first.").build();
        }
        try {
            customerService.delete(existingCustomer);
            return Response.noContent().entity("Customer deleted successfully").build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete customer").build();
        }
    }
    
}
