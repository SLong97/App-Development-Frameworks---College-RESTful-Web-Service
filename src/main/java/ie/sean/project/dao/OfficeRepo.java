package ie.sean.project.dao;

import ie.sean.project.entities.Department;
import ie.sean.project.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface OfficeRepo extends JpaRepository<Office, String> {

    Optional<Office> findOneByOfficeNumber(String officeNumber);
    List<Office> findByOfficeNumber(String officeNumber);
    List<Office> findAllByDepartment_DepartmentTitle(String departmentTitle);

    boolean existsByOfficeNumber(String officeNumber);


    @Query("select o from Office o where o.department.departmentTitle = ?1")
    List<Office> findAllByDepartmentTitle(String departmentTitle);

    @Query("select o from Office o where o.currentOccupancy = 0")
    List<Office> findAllEmptyOffices();

    @Query("select o from Office o where o.currentOccupancy < o.maximumOccupancy")
    List<Office> findOfficesWithSpace();

    @Modifying
    @Transactional
    @Query("delete from Office where officeNumber = :officeNumber")
    void deleteByOfficeNumber(@Param("officeNumber") String officeNumber);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.department.departmentTitle = :departmentTitle where o.officeNumber = :officeNumber")
    int moveDepartment(@Param("officeNumber") String officeNumber, @Param("departmentTitle") String departmentTitle);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.currentOccupancy = :newOccupancy where o.officeNumber = :officeNumber and :newOccupancy <= o.maximumOccupancy and :newOccupancy >= 0")
    int updateOccupancy(@Param("officeNumber") String officeNumber, @Param("newOccupancy") int newOccupancy);







}
