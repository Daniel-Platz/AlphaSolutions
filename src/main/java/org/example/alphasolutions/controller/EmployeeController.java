package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.exception.InvalidCredentialsException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    //TODO Implement enhance logout method
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}