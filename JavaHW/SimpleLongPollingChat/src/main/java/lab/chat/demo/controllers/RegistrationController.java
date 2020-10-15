package lab.chat.demo.controllers;

import lab.chat.demo.forms.UserForm;
import lab.chat.demo.services.RegistrationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationServiceImpl service;

    @Autowired
    public RegistrationController(RegistrationServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public String signUp(@RequestBody UserForm form) {
        System.out.println("Login: " + form.getLogin() + " password " + form.getPassword());
        service.signUp(form);
        return "redirect:/login";
    }
}
