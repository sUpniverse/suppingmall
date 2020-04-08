package com.supshop.suppingmall.user;

import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.BoardPageMaker;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final String sessionUser = "user";

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

    @GetMapping("")
    public String getAllUser(Model model,
                             HttpSession session,
                             BoardCriteria boardCriteria,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String searchValue) {
        if(!isLoginUser(session)) return "/user/login";
        model.addAttribute(userService.getAllUser(boardCriteria,type,searchValue));
        BoardPageMaker boardPageMaker = new BoardPageMaker();
        boardPageMaker.setBoardCriteria(boardCriteria);
        boardPageMaker.setTotalCount(userService.getBoardCount());
        model.addAttribute("boardPageMaker", boardPageMaker);
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
        UserVO user = userService.isSignedInUser(email,password);
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
        if(isLoginUser(session)) {
            return "redirect:/";
        }
        userService.createUser(user);
        return "redirect:/users/loginform";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        if(isOwner(id, sessionUser) || isAdmin(sessionUser)) {
            userService.updateUser(id, user);
            updateSession(session);
            return "redirect:/";
        }
        return "redirect:/users/loginform";
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> partialUpdateUser(@RequestBody(required = false) @Valid UserVO userVO,
                                                    @PathVariable Long id,
                                                    HttpSession session) {

        if(isAdmin(getSessionUser(session))) {
            User user = modelMapper.map(userVO, User.class);
            userService.patchUser(id,user);
            updateSession(session);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody User user, @PathVariable Long id, HttpSession session) {
        if(isAdmin(getSessionUser(session))) {
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/form")
    public String getUserPage(@PathVariable Long id, Model model, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        if(!isLoginUser(session)) {
            return "redirect:/users/loginform";
        }

        // 관리자 일 경우 관리자 메인페이지로
        if(isAdmin(sessionUser)) {
            model.addAttribute("user",sessionUser);
            return "/user/admin/main";
        }

        return "/user/main";
    }

    @GetMapping("/{id}/updateForm")
    public String getUpdateForm(@PathVariable Long id, Model model, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        // 개인화원 자격으로 자신의 회원 정보를 수정할 시 사용
        if(isOwner(id, sessionUser)) {
            model.addAttribute("user",sessionUser);
            return "/user/updateForm";
        }
        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
        if(isAdmin(sessionUser)) {
            model.addAttribute("user", sessionUser);
            return "/user/adminUpdateForm";
        }

        return "redirect:/users/loginform";
    }

    @GetMapping("/{id}/applySellerForm")
    public String getApplySellerRoleForm(@PathVariable Long id, Model model, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        if(isOwner(id, sessionUser)) {
            model.addAttribute("user", sessionUser);
            return "/user/applySellerForm";
        }
        return "redirect:/users/loginform";
    }


    @GetMapping("/seller")
    @ResponseBody
    public ResponseEntity<String> getSellerInfoByName(@RequestParam String type,
                                                      @RequestParam String value,
                                                      HttpSession session) {
        if(isLoginUser(session)) {
            List<UserVO> stores = userService.getStore(type, value);
            if(stores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
                return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/seller/{id}")
    public String getSeller(@PathVariable Long id, Model model, HttpSession session) {
        if(isOwner(id, getSessionUser(session))) {
            model.addAttribute("user", sessionUser);
            return "redirect:/user/"+id+"/form";
        }
        return "redirect:/users/loginform";
    }

    @PutMapping("/seller/{id}")
    public String applySellerRole(@PathVariable Long id,@Valid StoreVO store, HttpSession session) {
        if(isOwner(id, getSessionUser(session))) {
            userService.patchUser(id,User.builder().storeVO(store).build());
            updateSession(session);
            return "redirect:/users/"+id+"/form";
        }
        return "redirect:/users/loginform";
    }

    @PatchMapping("/seller/{id}")
    public String grantSellerRole(@PathVariable Long id,StoreVO store, HttpSession session) {
        if(isOwner(id, getSessionUser(session))) {

        }
        return "";
    }


    private boolean isLoginUser(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("user");
        if (user == null) return false;
        return true;
    }

    private UserVO getSessionUser(HttpSession session) {
        return (UserVO) session.getAttribute(sessionUser);
    }

    //세션의 유저와 주인의 아이디가 같은지 확인
    private boolean isOwner(Long id, UserVO sessionUser) {
        return sessionUser != null && sessionUser.getUserId().equals(id);
    }

    private boolean isAdmin(UserVO sessionUser) {
        return sessionUser != null && (sessionUser.getRole().equals(User.Role.ADMIN) || (sessionUser.getRole().equals(User.Role.MASTER)));
    }

    private void updateSession(HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        UserVO userVO = userService.getUserVO(sessionUser.getUserId());
        session.removeAttribute("user");
        session.setAttribute("user",userVO);
    }

}
