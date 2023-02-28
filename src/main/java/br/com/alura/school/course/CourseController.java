package br.com.alura.school.course;

import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.enrollment.EnrollmentResponse;
import br.com.alura.school.enrollment.NewEnrollmentRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {

  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final UserRepository userRepository;

  CourseController(
      CourseRepository courseRepository,
      EnrollmentRepository enrollRepository,
      UserRepository userRepository) {
    this.courseRepository = courseRepository;
    this.enrollmentRepository = enrollRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/courses")
  ResponseEntity<List<CourseResponse>> allCourses() {
    List<CourseResponse> courses = new ArrayList<>();
    courseRepository
        .findAll()
        .forEach(
            course -> {
              CourseResponse courseResponse = new CourseResponse(course);
              courses.add(courseResponse);
            });
    return ResponseEntity.ok().body(courses);
  }

  @GetMapping("/courses/{code}")
  ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
    Course course =
        courseRepository
            .findByCode(code)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        NOT_FOUND, format("Course with code %s not found", code)));
    return ResponseEntity.ok(new CourseResponse(course));
  }

  @GetMapping("/courses/enroll/report")
  public ResponseEntity<List<EnrollmentResponse>> enrollmentReport() {

    if (enrollmentRepository.findAll().isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    List<EnrollmentResponse> enrollments = new ArrayList<>();
    List<User> users = userRepository.findAll();
    users.forEach(
        user -> {
          int amountEnrollments = enrollmentRepository.countAllByUserUsername(user.getUsername());
          if (amountEnrollments >= 1) {
            enrollments.add(new EnrollmentResponse(user.getEmail(), amountEnrollments));
          }
        });

    return ResponseEntity.ok().body(enrollments);
  }

  @PostMapping("/courses")
  ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
    courseRepository.save(newCourseRequest.toEntity());
    URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
    return ResponseEntity.created(location).build();
  }

  @PostMapping("/courses/{courseCode}/enroll")
  ResponseEntity<Enrollment> enroll(
      @PathVariable("courseCode") String courseCode,
      @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {

    Optional<User> user = userRepository.findByUsername(newEnrollmentRequest.getUsername());
    if (enrollmentRepository.existsByCourseCodeAndUserUsername(
        courseCode, newEnrollmentRequest.getUsername())) {
      return ResponseEntity.badRequest().build();
    }
    if (user.isEmpty() || courseRepository.findByCode(courseCode).isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Enrollment enrollment = newEnrollmentRequest.toEntity(user.get(), courseCode);
    enrollmentRepository.save(enrollment);

    URI location = URI.create(format("/courses/%s/enroll", courseCode));

    return ResponseEntity.created(location).body(enrollment);
  }
}
