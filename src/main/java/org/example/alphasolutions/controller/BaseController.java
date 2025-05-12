package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {

    protected boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("employee") != null;
    }
}
