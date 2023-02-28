package br.com.alura.school.enrollment;

import br.com.alura.school.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Enrollment {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Size(max = 10)
  @Column(nullable = false)
  private String courseCode;

  private LocalDateTime registerDate = LocalDateTime.now();

  public Enrollment() {}

  public Enrollment(User user, String courseCode) {
    this.courseCode = courseCode;
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDateTime getRegisterDate() {
    return registerDate;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }
}
