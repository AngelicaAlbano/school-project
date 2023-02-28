package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewEnrollmentRequest {

  @JsonProperty
  @Size(max = 20)
  @NotBlank
  @Column(nullable = false)
  private final String username;

  private final String courseCode;

  public NewEnrollmentRequest(String username, String courseCode) {
    this.username = username;
    this.courseCode = courseCode;
  }

  public String getUsername() {
    return username;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public Enrollment toEntity() {
    return new Enrollment(username, courseCode);
  }
}
