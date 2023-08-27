package mg.management.employee.model;

public record Phone(
    String id,
    String employeeId,
    String code,
    String value
) {
}
