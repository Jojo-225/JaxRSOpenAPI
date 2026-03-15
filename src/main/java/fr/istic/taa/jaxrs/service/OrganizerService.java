package fr.istic.taa.jaxrs.service;
import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.dto.CreateUserDto;
import fr.istic.taa.jaxrs.dto.UpdateUserDto;

public class OrganizerService {
    private final OrganizerDao organizerDao = new OrganizerDao();

    public Organizer createOrganizer(CreateUserDto createOrganizerDto) {
        Organizer organizer = new Organizer();
        organizer.setLastName(createOrganizerDto.getLastname());
        organizer.setFirstName(createOrganizerDto.getFirstname());
        organizer.setDateOfBirth(createOrganizerDto.getBirthdate());
        organizer.setMail(createOrganizerDto.getEmail());
        organizer.setPassword(createOrganizerDto.getPass());

        if (organizerDao.getByEmail(organizer.getMail()) != null) {
            throw new RuntimeException("An organizer with this email already exists");
        }
        organizerDao.save(organizer);
        return organizer;
    }

    public Organizer findOne(Long id) {
        return organizerDao.findOne(id);
    }

    public java.util.List<Organizer> findAll() {
        return organizerDao.findAll();
    }

    public Organizer update(UpdateUserDto updateOrganizerDto, Organizer organizer) {
        organizer.setLastName(updateOrganizerDto.getLastname());
        organizer.setFirstName(updateOrganizerDto.getFirstname());
        organizer.setDateOfBirth(updateOrganizerDto.getBirthdate());
        organizer.setPassword(updateOrganizerDto.getPass());

        return organizerDao.update(organizer);
    }

    public Organizer updateEmail(String newEmail, Organizer organizer) {
        if (organizerDao.getByEmail(newEmail) != null) {
            throw new RuntimeException("An organizer with this email already exists");
        }
        organizer.setMail(newEmail);
        return organizerDao.update(organizer);
    }

    public void delete(Organizer organizer) {
        organizerDao.delete(organizer);
    }

}
