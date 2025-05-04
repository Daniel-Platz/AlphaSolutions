package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.exception.InvalidCredentialsException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        try {
            Employee employeeLoggedIn = employeeService.findByEmailAndPassword(email, password);

            session.setAttribute("employee", employeeLoggedIn);
            session.setAttribute("employeeId", employeeLoggedIn.getEmployeeId());
            session.setAttribute("role", employeeLoggedIn.getRole().toString());

            return "redirect:/projects";
        } catch (InvalidCredentialsException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/admin/employees")
    public String showEmployeeManagement(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/projects";
        }
        return "employee-management";
    }

    @PostMapping("/admin/employees/add")
    public String addEmployee(@RequestParam String firstname,
                              @RequestParam String lastname,
                              @RequestParam String email,
                              @RequestParam String role,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/projects";
        }

        Employee newEmployee = new Employee();
        newEmployee.setFirstname(firstname);
        newEmployee.setLastname(lastname);
        newEmployee.setEmail(email);
        newEmployee.setRole(Role.valueOf(role));
        newEmployee.setPassword("velkommen123");

        employeeService.addEmployee(newEmployee);

        redirectAttributes.addFlashAttribute("newPassword", "velkommen123");
        return "redirect:/admin/employees";
    }

}
