package fr.istic.taa.jaxrs.service;
import java.util.List;
import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.dto.user.CreateUserDto;
import fr.istic.taa.jaxrs.dto.user.UpdateUserDto;

public class CustomerService {
    private final CustomerDao customerDao =new CustomerDao();

    public Customer createCustomer(CreateUserDto createUserDto){
        Customer customer = new Customer();
        customer.setLastName(createUserDto.getLastname());
        customer.setFirstName(createUserDto.getLastname());
        customer.setDateOfBirth(createUserDto.getBirthdate());
        customer.setMail(createUserDto.getEmail());
        customer.setPassword(createUserDto.getPass());

       customerDao.save(customer);
       return customer;
    }

    public Customer findOne(Long id){
        return customerDao.findOne(id);
    }

    public List<Customer> findAll(){
        return customerDao.findAll();
    }
    
    public Customer update(UpdateUserDto updateUserDto, Customer customer){
        customer.setLastName(updateUserDto.getLastname());
        customer.setFirstName(updateUserDto.getLastname());
        customer.setDateOfBirth(updateUserDto.getBirthdate());
        customer.setPassword(updateUserDto.getPass());

        return customerDao.update(customer);
    }


    public void delete(Customer customer){
        customerDao.delete(customer);
    }
}
