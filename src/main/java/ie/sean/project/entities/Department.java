package ie.sean.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Department {

    @Id
    @Column(nullable = false, unique = true)
    private String departmentTitle;

    @Column(nullable = false, unique = true)
    private String departmentEmail;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Office> officeList;

    public Department(String departmentTitle, String departmentEmail) {
        this.departmentTitle = departmentTitle;
        this.departmentEmail = departmentEmail;
    }
}
