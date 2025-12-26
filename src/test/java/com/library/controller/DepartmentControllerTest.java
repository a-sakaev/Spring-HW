package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.DepartmentDto;
import com.library.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService service;

    @Test
    void create_shouldReturn201() throws Exception {
        when(service.createDepartment(any())).thenReturn(new DepartmentDto(1L, "IT"));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DepartmentDto(null, "IT"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        when(service.getById(1L)).thenReturn(new DepartmentDto(1L, "HR"));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(service.getAll()).thenReturn(List.of(
                new DepartmentDto(1L, "HR"),
                new DepartmentDto(2L, "IT")
        ));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(new DepartmentDto(1L, "New"));

        mockMvc.perform(put("/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DepartmentDto(null, "New"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
