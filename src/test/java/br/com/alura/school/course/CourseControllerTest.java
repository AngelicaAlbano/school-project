package br.com.alura.school.course;

import br.com.alura.school.enrollment.NewEnrollmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseControllerTest {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @Autowired private CourseRepository courseRepository;

  @Test
  void should_retrieve_course_by_code() throws Exception {
    courseRepository.save(
        new Course(
            "java-1",
            "Java OO",
            "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

    mockMvc
        .perform(get("/courses/java-1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code", is("java-1")))
        .andExpect(jsonPath("$.name", is("Java OO")))
        .andExpect(jsonPath("$.shortDescription", is("Java and O...")));
  }

  @Test
  void not_found_course_by_code() throws Exception {
    mockMvc
        .perform(get("/courses/java-1").accept(MediaType.APPLICATION_JSON))
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  void should_retrieve_all_courses() throws Exception {
    courseRepository.save(new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC."));
    courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));

    mockMvc
        .perform(get("/courses").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(2)))
        .andExpect(jsonPath("$[0].code", is("spring-1")))
        .andExpect(jsonPath("$[0].name", is("Spring Basics")))
        .andExpect(jsonPath("$[0].shortDescription", is("Spring Cor...")))
        .andExpect(jsonPath("$[1].code", is("spring-2")))
        .andExpect(jsonPath("$[1].name", is("Spring Boot")))
        .andExpect(jsonPath("$[1].shortDescription", is("Spring Boot")));
  }

  @Test
  void should_add_new_course() throws Exception {
    NewCourseRequest newCourseRequest =
        new NewCourseRequest(
            "java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more.");

    mockMvc
        .perform(
            post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourseRequest)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/courses/java-2"));
  }

  @Test
  @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void should_enroll_user_course() throws Exception {

    NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");

    mockMvc
        .perform(
            post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/courses/java-1/enroll"));
  }

  @Test
  void should_not_allow_enroll_non_existing_user() throws Exception {

    NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("bianca");

    mockMvc
        .perform(
            post("/courses/%s/enroll", "java-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void should_not_allow_enroll_non_existing_course() throws Exception {

    NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("ana");

    mockMvc
        .perform(
            post("/courses/python-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void bad_request_enroll_existing_enrollment() throws Exception {
    NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("ana");

    mockMvc
        .perform(
            post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void should_retrieve_enrollment_report() throws Exception {

    mockMvc
        .perform(get("/courses/enroll/report").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$[0].email", is("ana@email.com")))
        .andExpect(jsonPath("$[0].amountEnrollments", is(1)));
  }

  @Test
  void no_content_when_enrollments_amount_less_than_one() throws Exception {

    mockMvc
        .perform(get("/courses/enroll/report").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}
