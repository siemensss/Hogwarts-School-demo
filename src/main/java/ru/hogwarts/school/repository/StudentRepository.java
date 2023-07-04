package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findStudentsByAgeBetween(int min, int max);
    Collection<Student> findStudentsByFacultyId(Long facultyId);

}
