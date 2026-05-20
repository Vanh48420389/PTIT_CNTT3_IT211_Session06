package org.example.it211_session06.controller;

import org.example.it211_session06.model.entity.Student;
import org.example.it211_session06.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. Lấy danh sách sinh viên
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    // 2. Lấy sinh viên theo id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        student.setId(id);
        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student existingStudent = studentOptional.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "fullName":
                    existingStudent.setFullName((String) value);
                    break;
                case "email":
                    existingStudent.setEmail((String) value);
                    break;
                case "gpa":
                    existingStudent.setGpa(Double.valueOf(value.toString()));
                    break;
            }
        });

        Student patchedStudent = studentRepository.save(existingStudent);
        return ResponseEntity.ok(patchedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}