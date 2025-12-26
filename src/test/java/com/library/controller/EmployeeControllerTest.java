package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.EmployeeDto;
import com.library.projection.EmployeeProjection;
import com.library.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService service;

    @Test
    void create_shouldReturn201() throws Exception {
        EmployeeDto in = new EmployeeDto(null, "Иван", "Иванов", "Dev", BigDecimal.valueOf(100), 1L, null);
        EmployeeDto out = new EmployeeDto(10L, "Иван", "Иванов", "Dev", BigDecimal.valueOf(100), 1L, "IT");
        when(service.createEmployee(any())).thenReturn(out);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.secondName").value("Иванов"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        EmployeeDto out = new EmployeeDto(10L, "Иван", "Иванов", "Dev", BigDecimal.valueOf(100), 1L, "IT");
        when(service.getById(10L)).thenReturn(out);

        mockMvc.perform(get("/employees/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(service.getAll()).thenReturn(List.of(
                new EmployeeDto(1L, "Иван", "Иванов", "Dev", BigDecimal.valueOf(1), 1L, "IT"),
                new EmployeeDto(2L, "Дмитрий", "Дмитриев", "QA", BigDecimal.valueOf(2), 1L, "IT")
        ));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        EmployeeDto in = new EmployeeDto(null, "Иван", "Иванов", "Dev", BigDecimal.valueOf(100), 1L, null);
        EmployeeDto out = new EmployeeDto(10L, "Иван", "Иванов", "Dev", BigDecimal.valueOf(100), 1L, "IT");

        when(service.update(eq(10L), any())).thenReturn(out);

        mockMvc.perform(put("/employees/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(service).delete(10L);

        mockMvc.perform(delete("/employees/10"))
                .andExpect(status().isNoContent());

        verify(service).delete(10L);
    }

    @Test
    void getAllProjected_shouldReturnList() throws Exception {
        EmployeeProjection p = mock(EmployeeProjection.class);
        when(p.getFullName()).thenReturn("Иван Иванов");
        when(p.getPosition()).thenReturn("Dev");
        when(p.getDepartmentName()).thenReturn("IT");

        when(service.getAllProjected()).thenReturn(List.of(p));

        mockMvc.perform(get("/employees/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$[0].position").value("Dev"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"));
    }
}
