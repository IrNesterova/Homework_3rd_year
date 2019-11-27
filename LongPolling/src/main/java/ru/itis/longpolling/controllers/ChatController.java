package ru.itis.longpolling.controllers;

        import org.springframework.security.access.prepost.PreAuthorize;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.CrossOrigin;
        import org.springframework.web.bind.annotation.GetMapping;

        import java.util.UUID;

@Controller
public class ChatController {

    @CrossOrigin
    @GetMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public String getChatPage(Model model) {
        model.addAttribute("pageId", UUID.randomUUID().toString());
        return "chat";
    }
}