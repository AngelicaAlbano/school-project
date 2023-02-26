package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;

public class NewEnrollmentRequest {

  @JsonProperty
  @Size(max = 20)
  @NotBlank
  @Column(nullable = false)
  private final String username;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private final Date date;

  private final String courseCode;

  public NewEnrollmentRequest(String username, Date date, String courseCode) {
    this.username = username;
    this.date = Date.from(Instant.now());
    this.courseCode = courseCode;
  }

  public String getUsername() {
    return username;
  }

  public Date getDate() {
    return date;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public Enrollment toEntity() {
    return new Enrollment(username, date, courseCode);
  }
}
