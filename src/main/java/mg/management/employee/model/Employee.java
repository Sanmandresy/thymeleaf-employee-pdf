package mg.management.employee.model;

import java.util.List;

public record Employee(
    String registrationNumber,
    String id,
    String firstName,
    String lastName,
    String birthDate,
    String address,
    String image,
    char gender,
    String email,
    List<Phone> phones,
    String identity,
    String position,
    Integer children,
    String startedAt,
    String departedAt,
    String category,
    Integer grossSalary,
    String cnaps
    ) {
}
