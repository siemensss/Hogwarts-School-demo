package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-age/{age}")
    public ResponseEntity<Collection<Student>> findStudentsByAge(@PathVariable int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    @GetMapping("/age-between")
    public ResponseEntity<Collection<Student>> findStudentsAgeBetween(@RequestParam (required = false) int min,
                                                                      @RequestParam (required = false) int max) {
        if (min > 0 && max > min) {
            return ResponseEntity.ok(studentService.findStudentsByAgeBetween(min, max));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/faculty")
    public Faculty findFaculty(@PathVariable Long id) {
        return studentService.findFaculty(id);
    }

    @GetMapping("/count")
    public Integer countStudents(){
        return studentService.getCountStudents();
    }
    @GetMapping("/average-age")
    public Double averageAgeStudents(){
        return studentService.getAverageAgeStudents();
    }
    @GetMapping("/last-five-students")
    public List<Student> lastFiveStudents(){
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/get-names")
    public ResponseEntity<Collection<String>> findNamesByFirstLetter(@RequestParam (required = false) char letter) {
        return ResponseEntity.ok(studentService.findNamesByFirstLetter(letter));
    }
    @GetMapping("/get-average-age-stream")
    public ResponseEntity<Double> getAverageAgeStudentsWithStream() {
        return ResponseEntity.ok(studentService.getAverageAgeStudentsWithStream());
    }

    @GetMapping("/get-int")
    public ResponseEntity<Integer> getNumber() {
        return ResponseEntity.ok(studentService.getNumber());
    }
   /* @GetMapping("/get-int-modify")
    public ResponseEntity<Integer> getNumberModify() {
        return ResponseEntity.ok(studentService.getNumberModify());
    }*/

}

