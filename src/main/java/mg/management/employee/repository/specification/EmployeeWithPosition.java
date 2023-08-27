package mg.management.employee.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EmployeeWithPosition implements Specification<Employee> {
  private String position;
  @Override
  public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (position == null || position.isEmpty()) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    return criteriaBuilder.like(root.get("position"), "%" + this.position + "%");
  }
}
