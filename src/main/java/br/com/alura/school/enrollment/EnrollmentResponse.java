package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentResponse {
  @JsonProperty private final String email;

  @JsonProperty private final int amountEnrollments;

  public EnrollmentResponse(String email, int amountEnrollments) {
    this.email = email;
    this.amountEnrollments = amountEnrollments;
  }

  public String getEmail() {
    return email;
  }

  public int getAmountEnrollments() {
    return amountEnrollments;
  }
}
