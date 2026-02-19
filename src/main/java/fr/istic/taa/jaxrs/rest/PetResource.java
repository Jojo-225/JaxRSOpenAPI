package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.domain.Pet;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import fr.istic.taa.jaxrs.dao.PetDao;

@Path("pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
  private final PetDao dao = new PetDao();

  @GET
  @Path("/{petId}")
  public Pet getPetById(@PathParam("petId") Long id)  {
      // return pet
      return new Pet();
  }

  @GET
  @Path("/")
  public Pet getPet(Long petId)  {
      return new Pet();
  }

  
  @POST
  @Consumes("application/json")
  public Response createPet(Pet pet){
    dao.save(pet);
    return Response.status(Response.Status.CREATED).entity(pet).build();
  }
  
}