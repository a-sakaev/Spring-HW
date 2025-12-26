package com.library.controller;

import com.library.dto.EmployeeDto;
import com.library.projection.EmployeeProjection;
import com.library.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto dto){
        EmployeeDto created = service.createEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id){
        EmployeeDto founded = service.getById(id);
        return ResponseEntity.ok(founded);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto){
        EmployeeDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projections")
    public ResponseEntity<List<EmployeeProjection>> getAllProjected(){
        return ResponseEntity.ok(service.getAllProjected());
    }

    @GetMapping("/projections/{id}")
    public ResponseEntity<EmployeeProjection> findProjectionById(@PathVariable Long id){
        return ResponseEntity.ok(service.findProjectionById(id));
    }
}
