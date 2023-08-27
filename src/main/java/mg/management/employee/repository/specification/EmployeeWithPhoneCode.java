package mg.management.employee.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.entity.Employee;
import mg.management.employee.repository.entity.Phone;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EmployeeWithPhoneCode implements Specification<Employee> {
  private String code;

  @Override
  public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (code == null || code.isEmpty()) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    } else {
      Join<Employee, Phone> phone = root.join("phones");
      return criteriaBuilder.equal(phone.get("code"), code);
    }
  }
}
