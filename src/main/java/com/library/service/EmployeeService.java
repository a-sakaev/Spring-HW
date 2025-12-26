package com.library.service;

import com.library.dto.DepartmentDto;
import com.library.dto.EmployeeDto;
import com.library.model.Department;
import com.library.model.Employee;
import com.library.projection.EmployeeProjection;
import com.library.repository.DepartmentRepository;
import com.library.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository repository;

    public EmployeeService(DepartmentRepository departmentRepository, EmployeeRepository repository) {
        this.departmentRepository = departmentRepository;
        this.repository = repository;
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Неверные параметры сотрудника");
        }
        if (dto.getDepartmentId() == null){
            throw new IllegalArgumentException("id отдела обязателен");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Отдел с id " + dto.getDepartmentId() + " не найден"));

        Employee employee = toEntity(dto);
        employee.setDepartment(department);

        Employee saved = repository.save(employee);
        return toDto(saved);
    }

    public EmployeeDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Некорректный id");
        }

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Работник с id " + id + " не найден"));
        return toDto(employee);
    }

    public List<EmployeeDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto) {
        if (id == null || dto == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Работник с id " + id + " не найден"));

        employee.setSalary(dto.getSalary());
        employee.setPosition(dto.getPosition());
        employee.setFirstName(dto.getFirstName());
        employee.setSecondName(dto.getSecondName());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Отдел с id " + dto.getDepartmentId() + " не найден"));
            employee.setDepartment(department);
        }

        Employee saved = repository.save(employee);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник с id " + id + " не найден"));
        repository.delete(employee);
    }

    public List<EmployeeProjection> getAllProjected(){
        return repository.findAllProjectedBy();
    }

    public EmployeeProjection findProjectionById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Некорректный id");
        }
        return repository.findProjectionById(id)
                .orElseThrow(()  -> new IllegalStateException("Сотрудник с id " + id + " не найден"));
    }

    private EmployeeDto toDto(Employee employee){
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setSalary(employee.getSalary());
        dto.setPosition(employee.getPosition());
        dto.setFirstName(employee.getFirstName());
        dto.setSecondName(employee.getSecondName());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }

        return dto;
    }

    private Employee toEntity(EmployeeDto dto){
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setSalary(dto.getSalary());
        employee.setPosition(dto.getPosition());
        employee.setFirstName(dto.getFirstName());
        employee.setSecondName(dto.getSecondName());
        return employee;
    }

}
