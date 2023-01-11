package ie.sean.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Office {

    @Id
    @Column(nullable = false, unique = true)
    private String officeNumber;

    @Column(nullable = false)
    private int maximumOccupancy;

    @Column(nullable = false)
    private int currentOccupancy;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Department department;

    public Office(String officeNumber, int maximumOccupancy, int currentOccupancy) {
        this.officeNumber = officeNumber;
        this.maximumOccupancy = maximumOccupancy;
        this.currentOccupancy = currentOccupancy;
    }

}
