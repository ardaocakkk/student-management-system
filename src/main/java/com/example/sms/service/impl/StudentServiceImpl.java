package com.example.sms.service.impl;

import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Department;
import com.example.sms.entity.Student;
import com.example.sms.mapper.StudentMapper;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    public StudentServiceImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.studentMapper = studentMapper;
    }
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentMapper studentMapper;


    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student;
        if (studentDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(studentDto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            student = studentMapper.mapToStudent(studentDto, department);
        } else {
            student = studentMapper.mapToStudent(studentDto);
        }
        Student savedStudent = studentRepository.save(student);
        return studentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student theStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        return studentMapper.mapToStudentDto(theStudent);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map(studentMapper::mapToStudentDto).toList();
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student theStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        theStudent.setFirstName(studentDto.getFirstName());
        theStudent.setLastName(studentDto.getLastName());
        theStudent.setEmail(studentDto.getEmail());

        Department theDepartment = departmentRepository.findById(studentDto.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));
        theStudent.setDepartment(theDepartment);

        return studentMapper.mapToStudentDto(studentRepository.save(theStudent));
    }

    @Override
    public void deleteStudent(Long id) {

        Student theStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(theStudent);

    }

    @Override
    public StudentDto addStudentToDepartment(Long studentId, Long departmentId) {
        Student theStudent = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Department theDepartment = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        theStudent.setDepartment(theDepartment);
        return studentMapper.mapToStudentDto(studentRepository.save(theStudent));
    }
}
