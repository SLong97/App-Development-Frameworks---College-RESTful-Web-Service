package ie.sean.project.dao;

import ie.sean.project.entities.TheUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<TheUser, String> {

}
