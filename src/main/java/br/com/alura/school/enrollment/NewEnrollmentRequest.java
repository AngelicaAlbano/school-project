package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class NewEnrollmentRequest {

  @JsonProperty
  @Size(max = 20)
  @NotBlank
  @Column(nullable = false)
  private final String username;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
  private final LocalDateTime registerDate;

  private final String courseCode;

  public NewEnrollmentRequest(String username, LocalDateTime registerDate, String courseCode) {
    this.username = username;
    this.registerDate = LocalDateTime.now();
    this.courseCode = courseCode;
  }

  public String getUsername() {
    return username;
  }

  public LocalDateTime getRegisterDate() {
    return registerDate;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public Enrollment toEntity() {
    return new Enrollment(username, registerDate, courseCode);
  }
}
