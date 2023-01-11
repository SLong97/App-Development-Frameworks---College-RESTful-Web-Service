package ie.sean.project.controllers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record NewDepartment(@Size(min = 3, max = 36) @NotNull @NotBlank @NotEmpty String departmentTitle,
                            @Size(min = 3, max = 36) @NotNull @NotBlank @NotEmpty String departmentEmail) {
}
