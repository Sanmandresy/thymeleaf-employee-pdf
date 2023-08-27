package mg.management.employee.service;

import java.util.List;
import lombok.AllArgsConstructor;
import mg.management.employee.repository.PhoneRepository;
import mg.management.employee.repository.entity.Employee;
import mg.management.employee.repository.entity.Phone;
import mg.management.employee.repository.validator.PhoneValidator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PhoneService {
  private final PhoneRepository repository;
  private final PhoneValidator validator;
  private final EmployeeService employeeService;

  public void crupdatePhones(List<Phone> toSave) {
    toSave.forEach(validator);
    repository.saveAll(toSave);
  }

  public Phone registerPhone(String employeeId, String value,String code) {
    Phone phone = new Phone();
    Employee employee = employeeService.getById(employeeId);
    phone.setEmployee(employee);
    phone.setValue(value);
    phone.setCode(code);
    return phone;
  }

  public List<Phone> getByEmployeeId(String employeeId) {
    return repository.getByEmployeeId(employeeId);
  }

}
