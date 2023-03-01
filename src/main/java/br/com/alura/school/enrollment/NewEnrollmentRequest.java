package br.com.alura.school.enrollment;

import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewEnrollmentRequest {

  @JsonProperty
  @Size(max = 20)
  @NotBlank
  private String username;

  public NewEnrollmentRequest() {}

  public NewEnrollmentRequest(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public Enrollment toEntity(User user, String courseCode) {
    return new Enrollment(user, courseCode);
  }
}
