package com.example.demo.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees/")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("signup")
    public String showSignUpForm(Employee employee) {
        return "add-employee";
    }

    @GetMapping("list")
    public String showUpdateForm(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "index";
    }

    @PostMapping("add")
    public String addEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-employee";
        }

        employeeRepository.save(employee);
        return "redirect:list";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
    	Optional<Employee> employeeOptional = employeeRepository.findById(id);
        employeeOptional.orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        model.addAttribute("employee", employeeOptional.get());
        return "update-employee";
    }

    @PostMapping("update/{id}")
    public String updateEmployee(@PathVariable("id") long id, @Valid Employee employee, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "update-employee";
        }

        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model) {
    	Optional<Employee> employeeOptional = employeeRepository.findById(id);
        employeeOptional.orElseThrow(() -> new IllegalArgumentException("Invalid Employee Id:" + id));
        
        employeeRepository.delete(employeeOptional.get()); // get the Employee from the optional Employee object
        model.addAttribute("employees", employeeRepository.findAll());
        return "index";
    }
}