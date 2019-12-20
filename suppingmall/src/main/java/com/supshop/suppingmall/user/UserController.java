package com.supshop.suppingmall.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/users")
@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupform() {
        return "/user/signup";
    }

    @GetMapping("/loginform")
    public String loginform() {
        return "/user/login";
    }

    @GetMapping("")
    public String getAllUser(Model model) {
        model.addAttribute(userService.getAllUser());
        return "user/list";
    }

    /* 나중에 MSA 전환 시 사용 */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
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
    public String createUser(User user) {
        System.out.println(user.toString());
        userService.createUser(user);
        return "redirect:/users/loginform";
    }

    @GetMapping("/{id}/form")
    public String update_form(@PathVariable Long id, Model model, HttpSession session) {
        User sessionedUser = (User) session.getAttribute("user");
        if(!(sessionedUser.getUserId() == id)) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", userService.getUser(id));
        return "/user/updateform";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, HttpSession session) {
        System.out.println(user.toString());
        userService.updateUser(id, user);
        return "redirect:/";
    }

    @DeleteMapping("")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

}
