package br.com.alura.school.course;

import br.com.alura.school.enrollment.NewEnrollmentRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseControllerTest {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;

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
  void should_enroll_user_course() throws Exception {
    should_retrieve_course_by_code();
    should_add_new_course();
    userRepository.save(new User("ana", "ana@email.com"));

    NewEnrollmentRequest newEnrollmentRequest =
        new NewEnrollmentRequest("ana", Date.from(Instant.now()), "java-1");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    String toParse = df.format(newEnrollmentRequest.getDate());
    Date date = df.parse(toParse);
    String result = jsonMapper.writeValueAsString(newEnrollmentRequest);

    mockMvc
        .perform(
            post("/courses/java-1/enroll").contentType(MediaType.APPLICATION_JSON).content(result))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/courses/java-1/enroll"));
  }

  @Test
  void should_not_allow_enroll_non_existing_user() throws Exception {

    NewEnrollmentRequest newEnrollmentRequest =
        new NewEnrollmentRequest("bianca", Date.from(Instant.now()), "java-1");

    mockMvc
        .perform(
            post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void should_not_allow_enroll_non_existing_course() throws Exception {

    NewEnrollmentRequest newEnrollmentRequest =
        new NewEnrollmentRequest("ana", Date.from(Instant.now()), "python-1");

    mockMvc
        .perform(
            post("/courses/python-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void should_retrieve_enrollment_report() throws Exception {
    should_enroll_user_course();

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
