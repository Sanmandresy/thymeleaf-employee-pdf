package mg.management.employee.endpoint.mapper;

import mg.management.employee.model.Phone;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {
    public Phone toView(mg.management.employee.repository.entity.Phone entity) {
        return new Phone(
                entity.getId(),
                entity.getEmployee().getId(),
                entity.getCode(),
                entity.getValue());
    }

    public mg.management.employee.repository.entity.Phone toEntity(Phone view) {
        return mg.management.employee.repository.entity.Phone.builder()
                .value(view.value())
                .code(view.code())
                .build();
    }

}
