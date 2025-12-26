package com.library.service;

import com.library.dto.EmployeeDto;
import com.library.model.Department;
import com.library.model.Employee;
import com.library.projection.EmployeeProjection;
import com.library.repository.DepartmentRepository;
import com.library.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void createEmployee_shouldSetDepartmentAndSave() {
        EmployeeDto input = new EmployeeDto();
        input.setFirstName("Иван");
        input.setSecondName("Иванов");
        input.setPosition("Dev");
        input.setSalary(BigDecimal.valueOf(100));
        input.setDepartmentId(10L);

        Department dep = new Department(10L, "IT");
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(dep));

        when(repository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        EmployeeDto result = service.createEmployee(input);

        assertEquals(1L, result.getId());

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(repository).save(captor.capture());
        assertEquals("Иван", captor.getValue().getFirstName());
        assertNotNull(captor.getValue().getDepartment());
        assertEquals(10L, captor.getValue().getDepartment().getId());
    }

    @Test
    void createEmployee_shouldThrow_whenDepartmentIdNull() {
        EmployeeDto input = new EmployeeDto();
        input.setFirstName("Иван");
        input.setSecondName("Иванов");
        input.setPosition("Dev");
        input.setSalary(BigDecimal.valueOf(100));
        input.setDepartmentId(null);

        assertThrows(IllegalArgumentException.class, () -> service.createEmployee(input));
        verify(repository, never()).save(any());
    }

    @Test
    void getById_shouldReturnDto_whenFound() {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Иван");
        e.setSecondName("Иванов");
        e.setPosition("Dev");
        e.setSalary(BigDecimal.valueOf(100));

        when(repository.findById(1L)).thenReturn(Optional.of(e));

        EmployeeDto dto = service.getById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Иван", dto.getFirstName());
    }

    @Test
    void getAllProjected_shouldReturnProjectionList() {
        EmployeeProjection p = mock(EmployeeProjection.class);
        when(repository.findAllProjectedBy()).thenReturn(List.of(p));

        List<EmployeeProjection> list = service.getAllProjected();

        assertEquals(1, list.size());
        verify(repository).findAllProjectedBy();
    }

    @Test
    void findProjectionById_shouldReturnProjection_whenFound() {
        EmployeeProjection p = mock(EmployeeProjection.class);
        when(repository.findProjectionById(1L)).thenReturn(Optional.of(p));

        EmployeeProjection result = service.findProjectionById(1L);

        assertNotNull(result);
        verify(repository).findProjectionById(1L);
    }

    @Test
    void findProjectionById_shouldThrow_whenNotFound() {
        when(repository.findProjectionById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.findProjectionById(99L));
    }
}
