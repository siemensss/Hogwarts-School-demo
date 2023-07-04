package ru.hogwarts.school;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyControllerTests {
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
    private FacultyController facultyController;

    private final Faker faker = new Faker();

    @Test
    public void createFacultyTest() throws Exception {
        final String name = "empty";
        final String color = "empty";
        final Long id = 1L;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void updateFacultyTest() throws Exception {
        final String name = "empty";
        final String color = "empty";
        final Long id = 1L;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void findFacultyByIdTest() throws Exception {
        final String name = "empty";
        final String color = "empty";
        final Long id = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        when(facultyRepository.findById(eq(id))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void deleteFacultyByIdTest() throws Exception {
        final String name = "empty";
        final String color = "empty";
        final Long id = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findFacultyByNameOrColorTest() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        List<Faculty> expectedFacultyList = List.of(
                new Faculty(1L, "111", "color1", null),
                new Faculty(2L, "222", "color1", null)
        );
        String color = "color1";
        when(facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(null, color)).thenReturn(expectedFacultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=color1"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Faculty> actualFacultyList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(actualFacultyList)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(actualFacultyList.size())
                            .forEach(index -> {
                                Faculty actualFaculty = actualFacultyList.get(index);
                                Faculty expectedFaculty = expectedFacultyList.get(index);
                                assertThat(actualFaculty.getId()).isEqualTo(expectedFaculty.getId());
                                assertThat(actualFaculty.getName()).isEqualTo(expectedFaculty.getName());
                                assertThat(actualFaculty.getColor()).isEqualTo(expectedFaculty.getColor());
                            });
                });
    }

    @Test
    public void findStudentsByFacultyIdTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final String name = "empty";
        final String color = "empty";
        final Long id = 1L;
        final List<Student> expectedStudentsList = List.of(
                new Student(1L, "111", 17, null),
                new Student(2L, "222", 18, null),
                new Student(3L, "333", 19, null)
        );
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setStudents(expectedStudentsList);
        when(studentRepository.findStudentsByFacultyId(id)).thenReturn(expectedStudentsList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> actualStudentsList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(actualStudentsList)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(actualStudentsList.size())
                            .forEach(index -> {
                                Student actualStudent = actualStudentsList.get(index);
                                Student expectedStudent = expectedStudentsList.get(index);
                                assertThat(actualStudent.getId()).isEqualTo(expectedStudent.getId());
                                assertThat(actualStudent.getName()).isEqualTo(expectedStudent.getName());
                                assertThat(actualStudent.getAge()).isEqualTo(expectedStudent.getAge());
                            });
                });

    }


}
