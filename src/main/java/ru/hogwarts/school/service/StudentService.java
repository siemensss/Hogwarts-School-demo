package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;


@Service
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        LOG.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        LOG.info("Was invoked method for find student");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        LOG.info("Was invoked method for find student");
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        LOG.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(Integer age) {
        LOG.info("Was invoked method for find by age student");
        ArrayList<Student> result = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            if (Objects.equals(student.getAge(), age)) {
                result.add(student);
            }
        }
        return result;

    }
    public Collection<Student> findStudentsByAgeBetween(int min, int max) {
        LOG.info("Was invoked method for find students by age between student");
        return studentRepository.findStudentsByAgeBetween(min, max);
    }
    public Collection<Student> getAllStudents() {
        LOG.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Faculty findFaculty(Long id) {
        LOG.info("Was invoked method for find faculty");
       return studentRepository.findById(id)
               .map(Student::getFaculty)
               .get();
    }

    public Integer getCountStudents() {
        LOG.info("Was invoked method for get count of students");
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        LOG.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> getLastFiveStudents() {
        LOG.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }
}
