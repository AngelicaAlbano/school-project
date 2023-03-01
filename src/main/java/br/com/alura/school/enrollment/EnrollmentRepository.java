package br.com.alura.school.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  boolean existsByCourseCodeAndUserUsername(String courseCode, String username);

  int countAllByUserUsername(String username);
}
