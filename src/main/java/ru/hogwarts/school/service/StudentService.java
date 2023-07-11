package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(Integer age) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            if (Objects.equals(student.getAge(), age)) {
                result.add(student);
            }
        }
        return result;

    }
    public Collection<Student> findStudentsByAgeBetween(int min, int max) {
        return studentRepository.findStudentsByAgeBetween(min, max);
    }
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Faculty findFaculty(Long id) {
       return studentRepository.findById(id)
               .map(Student::getFaculty)
               .get();
    }

    public Integer getCountStudents() {
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }
}
