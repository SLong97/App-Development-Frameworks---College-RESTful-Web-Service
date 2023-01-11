package ie.sean.project.dto;

import ie.sean.project.controllers.WebService;
import ie.sean.project.entities.Office;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OfficeDtoMapper extends RepresentationModelAssemblerSupport<Office, OfficeDto> {

    public OfficeDtoMapper() {
        super(WebService.class, OfficeDto.class);
    }

    @Override
    public OfficeDto toModel(Office entity) {
        OfficeDto officeDto = new OfficeDto(entity.getOfficeNumber(), entity.getMaximumOccupancy(), entity.getCurrentOccupancy());
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getAOfficeById(entity.getOfficeNumber())).withSelfRel());
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getADepartmentByTitle(entity.getDepartment().getDepartmentTitle())).withRel("Department"));
        return officeDto;
    }

    @Override
    public CollectionModel<OfficeDto> toCollectionModel(Iterable<? extends Office> entities){
        return super.toCollectionModel(entities);
    }

}