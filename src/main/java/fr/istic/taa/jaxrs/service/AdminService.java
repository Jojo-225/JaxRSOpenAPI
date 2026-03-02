package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;

public class AdminService {
    public AdminDao dao = new AdminDao();

    public Admin getById(Long id) {
        Admin a = dao.findOne(id);
        return a;
    }
    public void create(Admin admin) {
        dao.save(admin);
    }
}
