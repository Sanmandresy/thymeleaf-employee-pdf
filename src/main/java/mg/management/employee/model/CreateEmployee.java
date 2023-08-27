package mg.management.employee.model;

import org.springframework.web.multipart.MultipartFile;


public record CreateEmployee(
    String firstName,
    String lastName,
    String birthDate,
    String address,
    MultipartFile image,
    char gender,
    String email,
    String code,
    String value,
    String serial,
    String deliveredIn,
    String deliveredAt,
    String position,
    Integer children,
    String startedAt,
    String departedAt,
    String category,
    Integer grossSalary,
    String cnaps
) {
  public CreateEmployee() {
    this(
        "",
        "",
        "",
        "",
        null,
        ' ',
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
        "",
        "",
        0,
        ""
    );
  }
}
