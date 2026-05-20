package org.example.it211_session06.controller;

import org.example.it211_session06.model.entity.Student;
import org.example.it211_session06.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // lay danh sach sinh vien
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    // lay sinh vien theo id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    //them sinh vien moi
    @PostMapping
    public ResponseEntity<Student> createStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.ok(savedStudent);
    }
    //cap nhat toan bo sinh vien
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        student.setId(id);
        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }
    // Cap nhat 1 phan sinh vien
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

    // Xoa sinh vien
    @DeleteMapping
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
