package ie.sean.project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TheUser {

    @Id
    private String userEmail;

    @Column
    private String userPassword;

    @Column
    private String userRole;

    @Column
    private boolean disabled, locked;


}