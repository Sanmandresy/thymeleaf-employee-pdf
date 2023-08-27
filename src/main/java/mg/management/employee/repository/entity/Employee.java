package mg.management.employee.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity(name = "Employee")
public class Employee implements Serializable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private String id;

  private String registrationNumber;

  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private Instant birthDate;

  private String address;

  private String email;

  private String identity;

  private char gender;

  private String position;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Phone> phones;

  private Integer children;

  private Instant startedAt;

  private Instant departedAt;

  private String category;

  private String cnaps;
  private Integer grossSalary;

  private byte[] image;
}
