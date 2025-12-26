package com.library.service;

import com.library.dto.DepartmentDto;
import com.library.model.Department;
import com.library.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService service;

    @Test
    void createDepartment_shouldSaveAndReturnDto() {
        DepartmentDto input = new DepartmentDto(null, "IT");

        when(repository.save(any(Department.class))).thenAnswer(invocation -> {
            Department d = invocation.getArgument(0);
            d.setId(1L);
            return d;
        });

        DepartmentDto result = service.createDepartment(input);

        assertNotNull(result.getId());
        assertEquals("IT", result.getName());

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(repository).save(captor.capture());
        assertEquals("IT", captor.getValue().getName());
    }

    @Test
    void getById_shouldReturnDto_whenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Department(1L, "HR")));

        DepartmentDto dto = service.getById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("HR", dto.getName());
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getById(99L));
    }

    @Test
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(
                new Department(1L, "HR"),
                new Department(2L, "IT")
        ));

        List<DepartmentDto> list = service.getAll();

        assertEquals(2, list.size());
        assertEquals("HR", list.get(0).getName());
        assertEquals("IT", list.get(1).getName());
    }

    @Test
    void update_shouldSaveNewName() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Department(1L, "Старое Название")));
        when(repository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentDto updated = service.update(1L, new DepartmentDto(null, "Новое название"));

        assertEquals(1L, updated.getId());
        assertEquals("Новое название", updated.getName());
        verify(repository).save(any(Department.class));
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        Department dep = new Department(1L, "HR");
        when(repository.findById(1L)).thenReturn(Optional.of(dep));

        service.delete(1L);

        verify(repository).delete(dep);
    }
}
