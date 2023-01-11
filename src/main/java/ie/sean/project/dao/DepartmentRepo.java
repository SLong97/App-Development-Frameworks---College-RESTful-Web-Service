package ie.sean.project.dao;

import ie.sean.project.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepo extends JpaRepository<Department, String> {

    Optional<Department> findByDepartmentTitle(String departmentTitle);
    List<Department> findOneByDepartmentTitle(String departmentTitle);
    List<Department> findByDepartmentTitleContaining(String snippet);

    boolean existsByDepartmentTitle(String departmentTitle);

    @Query("select distinct d from Department d join fetch d.officeList")
    List<Department> findDepartmentAndCharacters();

    @Query("select d from Department d order by d.departmentTitle")
    List<Department> findAllDepartmentsAlphabetically();

    @Modifying
    @Transactional
    @Query("delete from Department where departmentTitle = :departmentTitle")
    void deleteByDepartmentTitle(@Param("departmentTitle") String departmentTitle);

    @Modifying
    @Transactional
    @Query("update Department d set d.departmentTitle = :newTitle where d.departmentTitle = :departmentTitle")
    void changeTitle(@Param("departmentTitle") String departmentTitle, @Param("newTitle") String newTitle);

}