package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Enrollment {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Size(max = 20)
  @NotBlank
  @Column(nullable = false)
  private String username;

  @Size(max = 10)
  @NotBlank
  @Column(nullable = false)
  private String courseCode;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date date;

  @Deprecated
  public Enrollment() {}

  public Enrollment(String username, Date date, String courseCode) {
    this.courseCode = courseCode;
    this.username = username;
    this.date = Date.from(Instant.now());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }
}
