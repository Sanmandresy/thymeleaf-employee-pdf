package mg.management.employee.repository.validator;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import mg.management.employee.CompanyConf;
import mg.management.employee.repository.PhoneRepository;
import mg.management.employee.repository.entity.Phone;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PhoneValidator implements Consumer<Phone> {

  private final PhoneRepository repository;
  private final CompanyConf conf;

  @Override
  public void accept(Phone phone) {
    boolean alreadyExists = repository.existsByCodeAndValue(phone.getCode(), phone.getValue());
    boolean alreadyOwnedByCompany = String.join("", phone.getValue().split(" ")).equals(String.join("", conf.getPhone().split(" ")));
    Set<String> violationMessages = new HashSet<>();
    if (phone.getId() == null && alreadyExists) {
      violationMessages.add("Phone number already exists");
    }
    if (phone.getId() == null && alreadyOwnedByCompany) {
      violationMessages.add("Phone number already owned by company");
    }
    if (String.join("", phone.getValue().split(" ")).length() < 10 || String.join("", phone.getValue().split(" ")).length() > 10) {
      violationMessages.add("Value length must be equal to 10.");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages = violationMessages.stream()
          .map(String::toString)
          .collect(Collectors.joining(". "));
      throw new RuntimeException(formattedViolationMessages);
    }
  }
}
