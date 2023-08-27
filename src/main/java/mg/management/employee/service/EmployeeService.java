package mg.management.employee.service;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.EmployeeRepository;
import mg.management.employee.repository.criteria.DateRangeCriteria;
import mg.management.employee.repository.entity.Employee;
import mg.management.employee.repository.specification.EmployeeWithDateRange;
import mg.management.employee.repository.specification.EmployeeWithFirstName;
import mg.management.employee.repository.specification.EmployeeWithGender;
import mg.management.employee.repository.specification.EmployeeWithLastName;
import mg.management.employee.repository.specification.EmployeeWithPhoneCode;
import mg.management.employee.repository.specification.EmployeeWithPosition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static mg.management.employee.service.util.OrderUtils.paginate;

@Service
@AllArgsConstructor
public class EmployeeService {
  private final EmployeeRepository repository;

  public Employee getById(String id) {
    return repository.findById(id).orElseThrow();
  }

  public List<Employee> getEmployees() {
    return repository.findAll();
  }

  public List<Employee> findEmployeesByIds(List<String> ids) {
    return repository.findEmployeesByIds(ids);
  }

  public List<Employee> findByCriteria(String firstName, String lastName, String gender,
                                       String position, String code, String whichDate, Instant startDate, Instant endDate, String order, Integer page, Integer pageSize) {
    EmployeeWithFirstName hasFirstName = new EmployeeWithFirstName(firstName);
    EmployeeWithLastName hasLastName = new EmployeeWithLastName(lastName);
    EmployeeWithGender hasGender = new EmployeeWithGender(gender);
    EmployeeWithPosition hasPosition = new EmployeeWithPosition(position);
    EmployeeWithPhoneCode hasPhoneCode = new EmployeeWithPhoneCode(code);
    DateRangeCriteria dateRange = new DateRangeCriteria(startDate, endDate);
    EmployeeWithDateRange hasDateBetweenRange = new EmployeeWithDateRange(dateRange, whichDate);
    Specification<Employee> specification = Specification.where(hasFirstName)
        .and(hasLastName)
        .and(hasGender)
        .and(hasPosition)
        .and(hasPhoneCode)
        .and(hasDateBetweenRange);
    Pageable pageable = paginate(order, page, pageSize);
    return repository.findAll(specification, pageable).getContent();
  }

  @Transactional
  public Employee createEmployee(Employee toCreate) {
    return repository.save(toCreate);
  }

}
