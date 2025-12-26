package com.library.dto;

import com.library.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String secondName;
    @NotBlank
    private String position;
    @NotBlank
    @PositiveOrZero
    private BigDecimal salary;
    @NotBlank
    @Positive
    private Long departmentId;

    private String departmentName;
}
