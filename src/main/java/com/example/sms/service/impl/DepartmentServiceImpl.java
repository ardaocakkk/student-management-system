package com.example.sms.service.impl;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.entity.Department;
import com.example.sms.mapper.DepartmentMapper;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.service.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = departmentMapper.mapToDepartment(departmentDto);
        return departmentMapper.mapToDepartmentDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Department theDepartment = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        return departmentMapper.mapToDepartmentDto(theDepartment);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
//        departmentRepository.findAll().stream().map(item -> departmentMapper.mapToDepartmentDto(item)).toList(); without lambda expression
        return departmentRepository.findAll().stream().map(departmentMapper::mapToDepartmentDto).toList();
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Department theDepartment = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        theDepartment.setDepartmentName(departmentDto.getDepartmentName());
        theDepartment.setDepartmentDescription(departmentDto.getDepartmentDescription());
        Department updatedDepartment = departmentRepository.save(theDepartment);
        return departmentMapper.mapToDepartmentDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department theDepartment = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        departmentRepository.delete(theDepartment);
    }
}
