package com.library.controller;

import com.library.dto.DepartmentDto;
import com.library.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

        private final DepartmentService service;

        public DepartmentController(DepartmentService service) {
            this.service = service;
        }

        @PostMapping
        public ResponseEntity<DepartmentDto> create(@Valid @RequestBody DepartmentDto dto){
            DepartmentDto created = service.createDepartment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        @GetMapping("/{id}")
        public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id){
            DepartmentDto founded = service.getById(id);
            return ResponseEntity.ok(founded);
        }

        @GetMapping
        public ResponseEntity<List<DepartmentDto>> getAll(){
            return ResponseEntity.ok(service.getAll());
        }

        @PutMapping("/{id}")
        public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto dto){
            DepartmentDto updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

}
