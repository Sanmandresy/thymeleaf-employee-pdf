package mg.management.employee.endpoint.controller;

import lombok.AllArgsConstructor;
import mg.management.employee.CompanyConf;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/")
public class WebController {
  private final CompanyConf conf;
  @GetMapping
  public String index(Model model) {
    model.addAttribute("conf", conf);
    return "index";
  }
}
