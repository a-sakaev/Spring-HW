package com.library.service;

import com.library.dto.DepartmentDto;
import com.library.model.Department;
import com.library.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public DepartmentDto createDepartment(DepartmentDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Неверные параметры отдела");
        }
        Department department = new Department();
        department.setName(dto.getName());
        Department saved = repository.save(department);
        return toDto(saved);
    }

    public DepartmentDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Некорректный id");
        }

        Department department = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Отдел с id " + id + " не найден"));
        return toDto(department);
    }

    public List<DepartmentDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public DepartmentDto update(Long id, DepartmentDto dto) {
        if (id == null || dto == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }

        Department department = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Отдел с id " + id + " не найден"));
        department.setName(dto.getName());
        Department saved = repository.save(department);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }
        Department department = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Отдел с id " + id + " не найден"));
        repository.delete(department);
    }

    private DepartmentDto toDto(Department department) {
        return new DepartmentDto(department.getId(), department.getName());
    }

}




