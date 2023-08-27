package mg.management.employee.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.criteria.DateRangeCriteria;
import mg.management.employee.repository.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EmployeeWithDateRange implements Specification<Employee> {
  private DateRangeCriteria dateRange;
  private String dateField;

  @Override
  public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (dateRange == null || dateField.isEmpty()) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    } else if (dateRange.getStartDate() != null && dateRange.getEndDate() != null && dateField != null) {
      return criteriaBuilder.between(
          root.get(dateField),
          dateRange.getStartDate(),
          dateRange.getEndDate()
      );
    } else {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
  }
}

