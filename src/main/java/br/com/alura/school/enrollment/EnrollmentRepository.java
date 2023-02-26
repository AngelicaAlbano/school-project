package br.com.alura.school.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  Optional<Enrollment> findByCourseCodeAndUsername(String courseCode, String username);
}
