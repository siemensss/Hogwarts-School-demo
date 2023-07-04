package ru.hogwarts.school;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;
    @Test
    public void createStudentTest() throws Exception{
        final String name = "empty";
        final int age = 20;
        final Long id = 1L;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }
    @Test
    public void updateStudentTest() throws Exception{
        final String name = "empty";
        final int age = 20;
        final Long id = 1L;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }
    @Test
    public void findStudentByIdTest() throws Exception{
        final String name = "empty";
        final int age = 20;
        final Long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }
    @Test
    public void deleteStudentByIdTest() throws Exception{
        final String name = "empty";
        final int age = 20;
        final Long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(eq(1L))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isOk());
    }
    @Test
    public void findStudentsByAgeTest() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        List<Student> expectedStudentList = List.of(
                new Student(1L, "111", 20, null),
                new Student(2L, "222", 20, null),
                new Student(3L, "333", 20, null)
        );
        when(studentRepository.findAll()).thenReturn(expectedStudentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age/20"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> actualStudentList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(actualStudentList)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(actualStudentList.size())
                            .forEach(index -> {
                                Student actualStudent = actualStudentList.get(index);
                                Student expectedStudent = expectedStudentList.get(index);
                                assertThat(actualStudent.getId()).isEqualTo(expectedStudent.getId());
                                assertThat(actualStudent.getName()).isEqualTo(expectedStudent.getName());
                                assertThat(actualStudent.getAge()).isEqualTo(expectedStudent.getAge());
                            });
                });
    }
    @Test
    public void findAllStudentsTest() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        List<Student> expectedStudentList = List.of(
                new Student(1L, "111", 16, null),
                new Student(2L, "222", 20, null),
                new Student(3L, "333", 26, null)
        );
        when(studentRepository.findAll()).thenReturn(expectedStudentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> actualStudentList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(actualStudentList)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(actualStudentList.size())
                            .forEach(index -> {
                                Student actualStudent = actualStudentList.get(index);
                                Student expectedStudent = expectedStudentList.get(index);
                                assertThat(actualStudent.getId()).isEqualTo(expectedStudent.getId());
                                assertThat(actualStudent.getName()).isEqualTo(expectedStudent.getName());
                                assertThat(actualStudent.getAge()).isEqualTo(expectedStudent.getAge());
                            });
                });
    }
    @Test
    public void findStudentsAgeBetweenTest() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        List<Student> expectedStudentList = List.of(
                new Student(1L, "111", 17, null),
                new Student(2L, "222", 18, null),
                new Student(3L, "333", 19, null)
        );
        int min = 16;
        int max = 20;
        when(studentRepository.findStudentsByAgeBetween(min, max)).thenReturn(expectedStudentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?min=16&max=20"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> actualStudentList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(actualStudentList)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(actualStudentList.size())
                            .forEach(index -> {
                                Student actualStudent = actualStudentList.get(index);
                                Student expectedStudent = expectedStudentList.get(index);
                                assertThat(actualStudent.getId()).isEqualTo(expectedStudent.getId());
                                assertThat(actualStudent.getName()).isEqualTo(expectedStudent.getName());
                                assertThat(actualStudent.getAge()).isEqualTo(expectedStudent.getAge());
                            });
                });
    }

    @Test
    public void findFacultyTest() throws Exception {
        final String name = "empty";
        final int age = 20;
        final Long id = 1L;
        final Faculty faculty = new Faculty(1L, "faculty1", "color1", null);
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        when(studentRepository.findById(eq(1L))).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1/faculty"))
                .andExpect(status().isOk());
        assertThat(student.getFaculty()).isEqualTo(faculty);
    }
}
