package mg.management.employee.endpoint.mapper;

import java.util.List;
import mg.management.employee.model.CreateEmployee;
import mg.management.employee.model.CsvEmployee;
import mg.management.employee.model.Employee;
import mg.management.employee.model.Phone;
import org.springframework.stereotype.Component;

import static mg.management.employee.service.util.DataFormatterUtils.byteAToBase64;
import static mg.management.employee.service.util.DataFormatterUtils.instantToCommonDate;
import static mg.management.employee.service.util.DataFormatterUtils.multipartFileToByte;
import static mg.management.employee.service.util.DataFormatterUtils.stringToInstant;

@Component
public class EmployeeMapper {
  private String formatIdentity(CreateEmployee toCreate) {
    return toCreate.serial() +
        "|" +
        toCreate.deliveredIn()+
        "|" +
        toCreate.deliveredAt();
  }

  public Employee toView(mg.management.employee.repository.entity.Employee entity, List<Phone> phones) {
    return new Employee(
        entity.getRegistrationNumber(),
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName(),
        instantToCommonDate(entity.getBirthDate()),
        entity.getAddress(),
        byteAToBase64(entity.getImage()),
        entity.getGender(),
        entity.getEmail(),
        phones,
        entity.getIdentity(),
        entity.getPosition(),
        entity.getChildren(),
        instantToCommonDate(entity.getStartedAt()),
        instantToCommonDate(entity.getDepartedAt()),
        entity.getCategory(),
        entity.getGrossSalary(),
        entity.getCnaps()
    );
  }

  public mg.management.employee.repository.entity.Employee toEntity(CreateEmployee view) {
    return mg.management.employee.repository.entity.Employee.builder()
        .firstName(view.firstName())
        .lastName(view.lastName())
        .cnaps(view.cnaps())
        .email(view.email())
        .birthDate(stringToInstant(view.birthDate()))
        .image(multipartFileToByte(view.image()))
        .address(view.address())
        .gender(view.gender())
        .category(view.category())
        .children(view.children())
        .departedAt(stringToInstant(view.departedAt()))
        .startedAt(stringToInstant(view.startedAt()))
        .position(view.position())
        .grossSalary(view.grossSalary())
        .identity(formatIdentity(view))
        .build();
  }

  public CsvEmployee toCSV(mg.management.employee.repository.entity.Employee entity) {
    return new CsvEmployee(
        entity.getRegistrationNumber(),
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName(),
        instantToCommonDate(entity.getBirthDate()),
        entity.getPosition(),
        entity.getGender(),
        instantToCommonDate(entity.getStartedAt()),
        instantToCommonDate(entity.getDepartedAt())
    );
  }

}
