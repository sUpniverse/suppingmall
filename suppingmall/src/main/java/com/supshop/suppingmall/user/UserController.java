package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.BoardPageMaker;
import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.SignUpForm;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String sessionUserName = "user";
    private static final String redirectLoginUrl = "redirect:/users/loginform";
    private static final String redirectMainUrl = "redirect:/";

    @GetMapping("/signup")
    public String signupform(HttpSession session) {
        if (isLoginUser(session)) return redirectMainUrl;
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
        if(!isLoginUser(session)) return redirectLoginUrl;
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
    public ResponseEntity<Boolean> getUserByEmail(@PathVariable String email) {
        boolean userAlreadyExist;
        userAlreadyExist = userService.isUserAlreadyExistByEmail(email);
        if(userAlreadyExist) return ResponseEntity.ok(userAlreadyExist);
        return ResponseEntity.ok(userAlreadyExist);
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpSession session) {
        UserVO user = userService.isSignedInUser(email,password);
        if(user == null) {
            return redirectLoginUrl;
        }
        session.setAttribute("user", user);
        return redirectMainUrl;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return redirectMainUrl;
    }

    @PostMapping("")
    public String createUser(@Valid SignUpForm signUpForm, HttpSession session) {
        if(isLoginUser(session)) {
            return redirectMainUrl;
        }
        User user = modelMapper.map(signUpForm, User.class);
        userService.createUser(user);
        return redirectLoginUrl;
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        if(isOwner(id, sessionUser) || isAdmin(sessionUser)) {
            userService.updateUser(id, user);
            updateSession(session);
            return redirectMainUrl;
        }
        return redirectLoginUrl;
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
            return redirectLoginUrl;
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

        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
        if(isAdmin(sessionUser)) {
            UserVO userVO = userService.getUserVO(id);
            model.addAttribute("user",userVO);
            return "/user/adminUpdateForm";
        }

        // 개인화원 자격으로 자신의 회원 정보를 수정할 시 사용
        if(isOwner(id, sessionUser)) {
            model.addAttribute("user",sessionUser);
            return "/user/updateForm";
        }

        return redirectLoginUrl;
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
        UserVO sessionUser = getSessionUser(session);
        if(isOwner(id, getSessionUser(session))) {
            model.addAttribute("user", sessionUser);
            return "redirect:/user/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applyForm")
    public String getApplySellerRoleForm(Model model, HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        if(sessionUser.getRole().equals(Role.USER) && sessionUser.getStoreVO().getStoreApplyYn().equals("N")) {
            model.addAttribute("user", sessionUser);
            return "/user/applySellerForm";
        }
        return redirectLoginUrl;
    }

    @PostMapping("/seller/{id}/apply")
    public String applySellerRole(@PathVariable Long id,
                                  @Valid ApplySellerForm applyForm,
                                  HttpSession session) {
        if(isOwner(id, getSessionUser(session))) {
            StoreVO store = modelMapper.map(applyForm, StoreVO.class);
            userService.patchUser(id,User.builder().storeVO(store).build());
            updateSession(session);
            return "redirect:/users/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applicants")
    public String getApplySeller(BoardCriteria boardCriteria,
                                 Model model,
                                 HttpSession session) {

        UserVO sessionUser = SessionUtils.getSessionUser(session);
        if(isAdmin(sessionUser)) {
            List<User> applySellerUsers = userService.getApplySellerUsers(boardCriteria);

            BoardPageMaker boardPageMaker = new BoardPageMaker();
            boardPageMaker.setBoardCriteria(boardCriteria);
            boardPageMaker.setTotalCount(applySellerUsers.size());

            model.addAttribute("userList",applySellerUsers);
            model.addAttribute("boardPageMaker", boardPageMaker);
            return "/user/admin/applySellerList";
        }
        return redirectLoginUrl;
    }

    @PatchMapping("/seller/{id}/apply")
    @ResponseBody
    public ResponseEntity grantSellerRole(@PathVariable Long id, HttpSession session) {
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        if(isAdmin(sessionUser)) {
            User user = userService.getUser(id);
            StoreVO storeVO = user.getStoreVO();
            storeVO.setStoreApplyYn("N");
            user.setStoreVO(storeVO);
            user.setRole(Role.SELLER);
            try {
                userService.patchUser(user.getUserId(), user);
            } catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    private boolean isLoginUser(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute(sessionUserName);
        if (user == null) return false;
        return true;
    }

    private UserVO getSessionUser(HttpSession session) {
        return (UserVO) session.getAttribute(sessionUserName);
    }

    //세션의 유저와 주인의 아이디가 같은지 확인
    private boolean isOwner(Long id, UserVO sessionUser) {
        return sessionUser != null && sessionUser.getUserId().equals(id);
    }

    private boolean isAdmin(UserVO sessionUser) {
        return sessionUser != null && (sessionUser.getRole().equals(Role.ADMIN) || (sessionUser.getRole().equals(Role.MASTER)));
    }

    private void updateSession(HttpSession session) {
        UserVO sessionUser = getSessionUser(session);
        UserVO userVO = userService.getUserVO(sessionUser.getUserId());
        session.removeAttribute("user");
        session.setAttribute("user",userVO);
    }

}
