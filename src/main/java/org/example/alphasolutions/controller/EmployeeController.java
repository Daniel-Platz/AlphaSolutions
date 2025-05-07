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

import java.util.List;


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
    public String login(@RequestParam("emailPrefix") String emailPrefix,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        try {
            String email = emailPrefix + "@alphasolutions.dk";
            Employee employeeLoggedIn = employeeService.findByEmailAndPassword(email, password);

            session.setAttribute("employee", employeeLoggedIn);
            session.setAttribute("employeeId", employeeLoggedIn.getEmployeeId());
            session.setAttribute("role", employeeLoggedIn.getRole().toString());

            if ("velkommen123".equals(employeeLoggedIn.getPassword())) {
                session.setAttribute("forcePasswordChange", true);
            }

            return "redirect:/projects";
        } catch (InvalidCredentialsException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/admin/employees")
    public String showEmployeeManagement(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/projects";
        }

        List<Employee> allEmployees = employeeService.getAllEmployees();
        model.addAttribute("employees", allEmployees);

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

    @PostMapping("/admin/employees/delete")
    public String deleteEmployee(@RequestParam int employeeId, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/projects";
        }

        employeeService.deleteEmployeeById(employeeId);
        return "redirect:/admin/employees";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Kodeordene matcher ikke.");
            model.addAttribute("projects", List.of());  // tom liste for at undgå fejl
            model.addAttribute("role", session.getAttribute("role"));
            return "projects";
        }

        if (!employeeService.isValidPassword(newPassword)) {
            model.addAttribute("error", "Kodeordet skal indeholde mindst ét stort bogstav og ét tal.");
            model.addAttribute("projects", List.of());
            model.addAttribute("role", session.getAttribute("role"));
            return "projects";
        }

        employee.setPassword(newPassword);
        employeeService.updatePassword(employee);
        session.setAttribute("forcePasswordChange", false);

        return "redirect:/projects";
    }

    //TODO Implement enhance logout method
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}