package com.supshop.suppingmall.user;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.PageMaker;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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
    public String getAllUser(Model model,
                             HttpSession session,
                             Criteria criteria,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String searchValue) {
        if(!isLoginUser(session)) return "/user/login";
        model.addAttribute(userService.getAllUser(criteria,type,searchValue));
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        pageMaker.setTotalCount(userService.getBoardCount());
        model.addAttribute("pageMaker",pageMaker);
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
        User user = userService.isSignInedUser(email,password);
        if(user == null) {
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
            model.addAttribute("user", userService.getUser(id));
            return "/user/updateform";
        }
        if(isAdmin(sessionUser)) {
            model.addAttribute("user", userService.getUser(id));
            return "/user/adminUpdateForm";
        }

        return "redirect:/users/loginform";
    }

    private boolean isOwner(Long id, User sessionUser) {
        return sessionUser != null && sessionUser.getUserId().equals(id);
    }

    private boolean isAdmin(User sessionUser) {
        return sessionUser != null && (sessionUser.getRole().equals(User.Role.ADMIN) || (sessionUser.getRole().equals(User.Role.MASTER)));
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(isOwner(id, sessionUser) || isAdmin(sessionUser)) {
            userService.updateUser(id, user);
            return "redirect:/";
        }
        return "redirect:/users/loginform";
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> partialUpdateUser(@RequestBody(required = false) @Valid UserVO userVO,
                                                    @PathVariable Long id,
                                                    HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        if(isAdmin(sessionUser)) {
            User user = modelMapper.map(userVO, User.class);
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }



    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody User user, @PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(isAdmin(sessionUser)) {
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
