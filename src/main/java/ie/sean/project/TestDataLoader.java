package ie.sean.project;

import ie.sean.project.dao.DepartmentRepo;
import ie.sean.project.dao.OfficeRepo;
import ie.sean.project.dao.UserDao;
import ie.sean.project.entities.Department;
import ie.sean.project.entities.Office;
import ie.sean.project.entities.TheUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    OfficeRepo officeRepo;

    @Override
    public void run(String... args) throws Exception {

        Department one = departmentRepo.save(new Department("Computer Science", "ComputerScience@mtu.ie"));
        Department two = departmentRepo.save(new Department("Humanities", "Humanities@mtu.ie"));
        Department three = departmentRepo.save(new Department("Business", "Business@mtu.ie"));
        Department four = departmentRepo.save(new Department("Energy", "Energy@mtu.ie"));

        officeRepo.save(new Office("A123",4,2, one));
        officeRepo.save(new Office("A124",4,4, one));
        officeRepo.save(new Office("A125",4,3, one));

        officeRepo.save(new Office("B212",10,10, two));
        officeRepo.save(new Office("B213",10,9, two));
        officeRepo.save(new Office("B215",10,0, two));

        officeRepo.save(new Office("C311",1,0, three));
        officeRepo.save(new Office("C312",1,1, three));
        officeRepo.save(new Office("C313",1,1, three));

        officeRepo.save(new Office("E444",10,3, four));
        officeRepo.save(new Office("E455",10,2, four));
        officeRepo.save(new Office("E466",10,1, four));


    }



}
