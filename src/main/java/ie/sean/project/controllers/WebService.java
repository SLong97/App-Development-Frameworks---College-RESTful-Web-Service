package ie.sean.project.controllers;

import ie.sean.project.dao.DepartmentRepo;
import ie.sean.project.dao.OfficeRepo;
import ie.sean.project.dto.DepartmentDto;
import ie.sean.project.dto.DepartmentDtoMapper;
import ie.sean.project.dto.OfficeDto;
import ie.sean.project.dto.OfficeDtoMapper;
import ie.sean.project.entities.Department;
import ie.sean.project.entities.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class WebService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private DepartmentDtoMapper departmentDtoMapper;

    @Autowired
    private OfficeRepo officeRepo;

    @Autowired
    private OfficeDtoMapper officeDtoMapper;



    /*localhost:8080/api/departments

    {
        "departmentTitle" : "Construction",
            "departmentEmail" : "Construction@mtu.ie"
    }*/

    @PostMapping({"api/departments", "api/departments/"})
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto addDepartment(@RequestBody @Valid NewDepartment payload, BindingResult bindingResult){

        try {

            if(bindingResult.hasErrors())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON was malformed or there are errors");

            Department newDepartment = new Department(payload.departmentTitle(), payload.departmentEmail());
            return departmentDtoMapper.toModel(departmentRepo.save(newDepartment));
        }

        catch (DataIntegrityViolationException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department with departmentTitle " + payload.departmentTitle() + " already exists.");
        }

    }



    /*localhost:8080/api/departments*/

    @GetMapping("api/departments")
    public CollectionModel<DepartmentDto> getAllDepartments(){
        return departmentDtoMapper.toCollectionModel(departmentRepo.findAll());
    }



    /*localhost:8080/api/departments/Technology*/

    @GetMapping("api/departments/{title}")
    public CollectionModel<DepartmentDto> getADepartmentByTitle(@PathVariable("title") String departmentTitle){
        if(departmentRepo.existsById(departmentTitle))
            return departmentDtoMapper.toCollectionModel(departmentRepo.findOneByDepartmentTitle(departmentTitle));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + departmentTitle + " was not found");
    }



    /*localhost:8080/api/departments/Technology/offices*/

    @GetMapping("api/departments/{title}/offices")
    public CollectionModel<OfficeDto> getOfficesInDepartment(@PathVariable("title") String departmentTitle){
        if(departmentRepo.existsById(departmentTitle))
            return officeDtoMapper.toCollectionModel(officeRepo.findAllByDepartment_DepartmentTitle(departmentTitle));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + departmentTitle + " was not found");
    }



    /*localhost:8080/api/offices

    {
        "officeNumber" : "A000",
        "maximumOccupancy" : 5,
        "currentOccupancy" : 3,
        "departmentTitle" : "Technology"
    }*/

    @PostMapping({"api/offices", "api/offices/"})
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeDto addOffice(@Valid @RequestBody NewOffice payload, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing data or does not obey data requirements");

        Optional<Department> departmentOptional = departmentRepo.findById(payload.departmentTitle());

        if (departmentOptional.isPresent()){
            Office newOffice = new Office(payload.officeNumber(), payload.maximumOccupancy(), payload.currentOccupancy(), departmentOptional.get());
            return officeDtoMapper.toModel(officeRepo.save(newOffice));
        }

        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with departmentTitle " + payload.departmentTitle() + " already exists.");
        }

    }



    /*localhost:8080/api/offices*/

    @GetMapping("api/offices")
    public CollectionModel<OfficeDto> getAllOffices(){
        CollectionModel<OfficeDto> officeDto = officeDtoMapper.toCollectionModel(officeRepo.findAll());
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getAllEmptyOffices()).withRel("Empty Offices"));
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getOfficesWithSpace()).withRel("Offices With Space"));
        return officeDto;
    }



    /*localhost:8080/api/offices/A123*/

    @GetMapping("api/offices/{id}")
    public CollectionModel<OfficeDto> getAOfficeById(@PathVariable("id") String officeNumber){
        if(officeRepo.existsById(officeNumber))
            return officeDtoMapper.toCollectionModel(officeRepo.findByOfficeNumber(officeNumber));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with number " + officeNumber + " was not found");
    }



    /*localhost:8080/api/offices/A125

    {
        "departmentTitle" : "Business"
    }*/

    @PatchMapping("api/offices/{number}")
    public OfficeDto moveDepartment(@PathVariable("number") String officeNumber, @Valid @RequestBody MoveDepartment payload, BindingResult bindingResult){
        try{

            if (bindingResult.hasErrors())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing data or does not obey data requirements");
            officeRepo.moveDepartment(officeNumber, payload.departmentTitle());
            return officeDtoMapper.toModel(officeRepo.findById(officeNumber).get());

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + payload.departmentTitle() + " was not found.");
        }

    }



    /*localhost:8080/api/offices/A125/2 <-- will change from currentOccupancy to 2
    localhost:8080/api/offices/A125/-1 <-- wont change since its negative number
    localhost:8080/api/offices/A125/5 <-- wont change since greater than max occupancy */

    @PatchMapping("api/offices/{number}/{newOccupancy}")
    public OfficeDto updateCurrentOccupancy(@PathVariable("number") String officeNumber, @PathVariable("newOccupancy") int newOccupancy){
        try{
            officeRepo.updateOccupancy(officeNumber, newOccupancy);
            return officeDtoMapper.toModel(officeRepo.findById(officeNumber).get());

        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with number " + officeNumber + " was not found.");
        }

    }



    /*localhost:8080/api/offices/A125*/

    @DeleteMapping("api/offices/{number}")
    public void deleteOffice(@PathVariable("number") String officeNumber){
        try {
            officeRepo.deleteById(officeNumber);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with number " + officeNumber + " was not found");
        }
    }



    /*Get Empty Offices and Offices with Space are
    both shown at the end of the results when endpoint
    localhost:8080/api/offices is called*/

    @GetMapping("api/offices/empty")
    public CollectionModel<OfficeDto> getAllEmptyOffices(){
        return officeDtoMapper.toCollectionModel(officeRepo.findAllEmptyOffices());
    }

    @GetMapping("api/offices/space")
    public CollectionModel<OfficeDto> getOfficesWithSpace(){
        return officeDtoMapper.toCollectionModel(officeRepo.findOfficesWithSpace());
    }




}
