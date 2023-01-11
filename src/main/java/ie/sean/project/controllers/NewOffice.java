package ie.sean.project.controllers;

import javax.validation.constraints.*;

public record NewOffice(

        @NotNull
        @NotEmpty
        @NotBlank
        @Size(min = 4)
        String officeNumber,

        @NotNull
        int maximumOccupancy,

        @NotNull
        int currentOccupancy,

        @NotNull
        String departmentTitle) {



}
