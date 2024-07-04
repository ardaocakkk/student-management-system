package com.example.sms.mapper;

import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Department;
import com.example.sms.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }
    private final DepartmentMapper departmentMapper;
    public StudentDto mapToStudentDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDepartment() != null ? student.getDepartment().getId() : null,
                student.getDepartment() != null ? departmentMapper.mapToDepartmentDto(student.getDepartment()) : null
        );
    }

    public Student mapToStudent(StudentDto studentDto) {
        return new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getEmail(),
                null
        );
    }

    public Student mapToStudent(StudentDto studentDto, Department department) {
        return new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getEmail(),
                department
        );
    }
}
