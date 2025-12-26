package com.library.repository;

import com.library.model.Employee;
import com.library.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<EmployeeProjection> findAllProjectedBy();

    Optional<EmployeeProjection> findProjectionById(Long id);

}
