package ie.sean.project.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "offices", itemRelation = "office")
public class OfficeDto extends RepresentationModel<OfficeDto> {

    private String officeNumber;
    private int maximumOccupancy;
    private int currentOccupancy;

}
