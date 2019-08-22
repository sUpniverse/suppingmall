package com.supshop.suppingmall.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping("")
    public String createUser(@ModelAttribute User user) {
        return userService.createUser(user);
    }

    @PutMapping("")
    public String updateUser(@PathVariable String id, @ModelAttribute User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("")
    public String deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

}
