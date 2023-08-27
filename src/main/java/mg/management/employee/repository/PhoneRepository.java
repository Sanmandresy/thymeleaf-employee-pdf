package mg.management.employee.repository;

import java.util.List;
import mg.management.employee.repository.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {
  boolean existsByCodeAndValue(String code, String value);

  List<Phone> getByEmployeeId(String employeeId);
}
