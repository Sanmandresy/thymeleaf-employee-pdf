package mg.management.employee.model;

public record CsvEmployee(
    String registrationNumber,
    String id,
    String firstName,
    String lastName,
    String birthDate,
    String position,
    char gender,
    String startedAt,
    String departedAt
    ) {
}
