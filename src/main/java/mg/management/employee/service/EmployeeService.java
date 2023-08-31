package mg.management.employee.service;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import mg.management.employee.CompanyConf;
import mg.management.employee.endpoint.mapper.EmployeeMapper;
import mg.management.employee.model.AgeCriteria;
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
import org.thymeleaf.context.Context;

import static mg.management.employee.service.util.DataFormatterUtils.getAge;
import static mg.management.employee.service.util.OrderUtils.paginate;
import static mg.management.employee.service.util.PdfUtils.generatePdf;
import static mg.management.employee.service.util.TemplateUtils.htmlToString;

@Service
@AllArgsConstructor
public class EmployeeService {
  private final EmployeeRepository repository;
  private final CompanyConf conf;
  private final EmployeeMapper mapper;

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

  private Context getEmployeeCardContext(mg.management.employee.model.Employee employee, CompanyConf conf, AgeCriteria criteria) {
    Context initial = new Context();
    initial.setVariable("profile", employee.image());
    initial.setVariable("matricule", employee.registrationNumber());
    initial.setVariable("nom", employee.lastName());
    initial.setVariable("prénoms", employee.firstName());
    initial.setVariable("âge", getAge(employee.birthDate(), criteria));
    initial.setVariable("embauche", employee.startedAt());
    initial.setVariable("départ", employee.departedAt());
    initial.setVariable("cnaps", employee.cnaps());
    initial.setVariable("salaire", employee.grossSalary());
    initial.setVariable("entreprise", conf);
    return initial;
  }

  public byte[] generateCard(String employeeId, AgeCriteria criteria) throws IOException, DocumentException {
    Employee toGenerate = repository.findById(employeeId).orElseThrow();
    String cardContent = htmlToString("employee-card", getEmployeeCardContext(mapper.toView(toGenerate, null), conf, criteria));
    return generatePdf(cardContent);
  }

}
