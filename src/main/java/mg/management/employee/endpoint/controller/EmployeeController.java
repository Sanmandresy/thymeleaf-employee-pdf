package mg.management.employee.endpoint.controller;

import com.lowagie.text.DocumentException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import mg.management.employee.CompanyConf;
import mg.management.employee.endpoint.mapper.EmployeeMapper;
import mg.management.employee.endpoint.mapper.PhoneMapper;
import mg.management.employee.model.AgeCriteria;
import mg.management.employee.model.CreateEmployee;
import mg.management.employee.model.CsvEmployee;
import mg.management.employee.model.Employee;
import mg.management.employee.model.Phone;
import mg.management.employee.service.EmployeeService;
import mg.management.employee.service.PhoneService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static mg.management.employee.service.util.DataFormatterUtils.stringToInstant;

@Controller
@AllArgsConstructor
public class EmployeeController {
  private final EmployeeService service;
  private final EmployeeMapper mapper;
  private final PhoneService phoneService;
  private final PhoneMapper phoneMapper;
  private final CompanyConf conf;

  @GetMapping("/employees")
  public String getEmployees(Model model) {
    List<Employee> employees = service.getEmployees().stream()
        .map(employee -> mapper.toView(employee, phoneService.getByEmployeeId(employee.getId()).stream()
            .map(phoneMapper::toView)
            .collect(Collectors.toUnmodifiableList())))
        .collect(Collectors.toUnmodifiableList());
    model.addAttribute("employees", employees);
    model.addAttribute("conf", conf);
    return "employees";
  }

  @GetMapping("/employees/filter")
  public String getFilteredEmployees(@RequestParam(value = "lastName", required = false) String lastName,
                                     @RequestParam(value = "firstName", required = false) String firstName,
                                     @RequestParam(value = "gender", required = false) String gender,
                                     @RequestParam(value = "position", required = false) String position,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "start", required = false) String start,
                                     @RequestParam(value = "end", required = false) String end,
                                     @RequestParam(value = "field", required = false) String field,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "order", required = false) String order,
                                     Model model) {
    List<Employee> employees = service
        .findByCriteria(
            firstName, lastName, gender, position, code, field, stringToInstant(start), stringToInstant(end), order, page, pageSize)
        .stream()
        .map(employee -> mapper.toView(employee, phoneService.getByEmployeeId(employee.getId()).stream()
            .map(phoneMapper::toView)
            .collect(Collectors.toUnmodifiableList())))
        .collect(Collectors.toUnmodifiableList());
    model.addAttribute("employees", employees);
    model.addAttribute("conf", conf);
    return "employees";
  }

  @GetMapping("/employee")
  public String getEmployeeById(Model model, @RequestParam("id") String id) {
    Employee employee = mapper.toView(service.getById(id), service.getById(id).getPhones().stream()
        .map(phoneMapper::toView)
        .collect(Collectors.toUnmodifiableList()));
    model.addAttribute("employee", employee);
    model.addAttribute("conf", conf);
    return "employee";
  }

  @GetMapping("/add-employee")
  public String newEmployee(Model model) {
    model.addAttribute("employee", new CreateEmployee());
    model.addAttribute("conf", conf);
    return "create-employee";
  }


  @PostMapping("/save")
  public String saveEmployee(@ModelAttribute
                             CreateEmployee employee) {
    mg.management.employee.repository.entity.Employee saved = service.createEmployee(mapper.toEntity(employee));
    List<Phone> phones = new ArrayList<>();
    phones.add(phoneMapper.toView(phoneService.registerPhone(saved.getId(), employee.value(), employee.code())));
    phoneService.crupdatePhones(phones.stream()
        .map(phoneMapper::toEntity)
        .toList());
    return "redirect:/employees";
  }

  @GetMapping(value = "/card", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> getEmployeeCard(@RequestParam("id") String employeeId, @RequestParam(value = "age_criteria", required = false) AgeCriteria criteria) throws IOException, DocumentException {
    byte[] contents = service.generateCard(employeeId, criteria);
    return ResponseEntity.ok(contents);
  }

  @PostMapping("/csv")
  public void exportToCSV(HttpServletResponse response,
                          @RequestParam("employeeIds") String employeeIdsStr)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=employees.csv");

    List<String> employeeIds = Arrays.asList(employeeIdsStr.split(","));

    StatefulBeanToCsv<CsvEmployee> writer = new StatefulBeanToCsvBuilder<CsvEmployee>(response.getWriter())
        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
        .withOrderedResults(false)
        .build();

    List<CsvEmployee> employees = service.findEmployeesByIds(employeeIds).stream()
        .map(mapper::toCSV)
        .collect(Collectors.toList());

    writer.write(employees);
  }

}
