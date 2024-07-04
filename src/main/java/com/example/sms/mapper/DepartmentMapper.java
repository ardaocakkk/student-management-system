package com.example.sms.mapper;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.entity.Department;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDto mapToDepartmentDto(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentDescription()
        );
    }

    public Department mapToDepartment(DepartmentDto departmentDto) {
        return new Department(
                departmentDto.getId(),
                departmentDto.getDepartmentName(),
                departmentDto.getDepartmentDescription()
        );
    }
}
