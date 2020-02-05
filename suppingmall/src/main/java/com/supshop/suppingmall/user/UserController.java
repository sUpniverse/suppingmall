package com.supshop.suppingmall.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/users")
@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupform(HttpSession session) {
        if (isLoginUser(session)) return "redirect:/";
        return "/user/signup";
    }

    @GetMapping("/loginform")
    public String loginform(HttpSession session) {
        if (isLoginUser(session)) return "redirect:/";
        return "/user/login";
    }

    private boolean isLoginUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return false;
        return true;
    }

    @GetMapping("")
    public String getAllUser(Model model) {
        model.addAttribute(userService.getAllUser());
        return "/user/list";
    }

    /* 나중에 MSA 전환 시 사용 */
    @ResponseBody
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @ResponseBody
    @GetMapping(value = "/emails/{email}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {

        try {
            userService.getUserByEmail(email);
            return ResponseEntity.ok("false");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.ok("true");
        }
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpSession session) {
        User user = userService.getUserByEmail(email);
        if(user == null) {
            return "redirect:/users/loginform";
        }
        if(!password.equals(user.getPassword())) {
            return "redirect:/users/loginform";
        }

        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @PostMapping("")
    public String createUser(@Valid User user, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser != null) {
            return "redirect:/";
        }
        userService.createUser(user);
        return "redirect:/users/loginform";
    }

    @GetMapping("/{id}/form")
    public String update_form(@PathVariable Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(isOwner(id, sessionUser)) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", userService.getUser(id));
        return "/user/updateform";
    }

    private boolean isOwner(@PathVariable Long id, User sessionUser) {
        return sessionUser == null || !sessionUser.getUserId().equals(id);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(isOwner(id, sessionUser)) {
            return "redirect:/users/login";
        }
        userService.updateUser(id, user);
        return "redirect:/";
    }

    @DeleteMapping("")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(isOwner(id, sessionUser)) {
            return "redirect:/users/login";
        }
        userService.deleteUser(id);
        return "redirect:/";
    }

}
