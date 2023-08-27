package mg.management.employee.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EmployeeWithFirstName implements Specification<Employee> {
  private String firstName;

  @Override
  public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (firstName == null || firstName.isEmpty()) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + this.firstName.toLowerCase() + "%");
  }
}
