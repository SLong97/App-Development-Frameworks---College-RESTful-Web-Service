package ie.sean.project.dto;

import ie.sean.project.controllers.WebService;
import ie.sean.project.entities.Department;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentDtoMapper extends RepresentationModelAssemblerSupport<Department, DepartmentDto> {

    public DepartmentDtoMapper() {
        super(WebService.class, DepartmentDto.class);
    }

    @Override
    public DepartmentDto toModel(Department entity) {
        DepartmentDto departmentDto = new DepartmentDto(entity.getDepartmentTitle(), entity.getDepartmentEmail());
        departmentDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getADepartmentByTitle(entity.getDepartmentTitle())).withSelfRel());
        departmentDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getOfficesInDepartment(entity.getDepartmentTitle())).withRel("Offices"));
        return departmentDto;
    }

    @Override
    public CollectionModel<DepartmentDto> toCollectionModel(Iterable<? extends Department> entities){
        return super.toCollectionModel(entities);
    }


}
