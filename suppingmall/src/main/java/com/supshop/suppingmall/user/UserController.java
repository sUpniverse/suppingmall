package com.supshop.suppingmall.user;

import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.BoardPageMaker;
import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private static final String redirectLoginUrl = "redirect:/users/loginform";
    private static final String redirectMainUrl = "redirect:/";
    private static final String requestReferer = "Referer";
    private static final String prevPage = "prevPage";

    @GetMapping("/signup")
    public String signupform(@AuthenticationPrincipal SessionUser user) {
        if (isLoginUser(user))
            return redirectMainUrl;
        return "/user/signup";
    }

    @GetMapping("/loginform")
    public String loginform(@AuthenticationPrincipal SessionUser user, HttpServletRequest request) {
        if (isLoginUser(user)) return "redirect:/";

        String uri = request.getHeader(requestReferer);
        if (!uri.contains("/loginform")) {
            request.getSession().setAttribute(prevPage,
                    request.getHeader(requestReferer));
        }

        return "/user/login";
    }

    @GetMapping("")
    public String getAllUser(Model model,
                             BoardCriteria boardCriteria,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String searchValue) {
//        if(!isLoginUser(user)) return redirectLoginUrl;
        model.addAttribute(userService.getAllUser(boardCriteria,type,searchValue));
        BoardPageMaker boardPageMaker = new BoardPageMaker();
        boardPageMaker.setBoardCriteria(boardCriteria);
        boardPageMaker.setTotalCount(userService.getBoardCount());
        model.addAttribute("boardPageMaker", boardPageMaker);
        return "/user/list";
    }

    /* 나중에 MSA 전환 시 사용 */
//    @ResponseBody
//    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public User getUser(@PathVariable Long id) {
//        return userService.getUser(id);
//    }

    @ResponseBody
    @GetMapping(value = "/emails/{email}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> getUserByEmail(@PathVariable String email) {
        boolean userAlreadyExist;
        userAlreadyExist = userService.isUserAlreadyExistByEmail(email);
        if(userAlreadyExist) return ResponseEntity.ok(userAlreadyExist);
        return ResponseEntity.ok(userAlreadyExist);
    }

//    @PostMapping("/login")
//    public String login(String email, String password, HttpSession session) {
//        UserVO user = userService.isSignedInUser(email,password);
//        if(user == null) {
//            return redirectLoginUrl;
//        }
//        session.setAttribute("user", user);
//        return redirectMainUrl;
//    }

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("user");
//        return redirectMainUrl;
//    }

    @PostMapping("")
    public String createUser(@Valid SignUpForm signUpForm, @AuthenticationPrincipal SessionUser user) {
        if(isLoginUser(user)) {
            return redirectMainUrl;
        }
        User createdUser = modelMapper.map(signUpForm, User.class);
        userService.createUser(createdUser);

        return redirectLoginUrl;
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user, @AuthenticationPrincipal SessionUser sessionUser) {
        if(isOwner(id, sessionUser) || isAdmin(sessionUser)) {
            userService.updateUser(id, user);
//            updateSession(session); Todo : 업데이트시 시큐리티상에 갱신 필요
            return redirectMainUrl;
        }
        return redirectLoginUrl;
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> partialUpdateUser(@RequestBody(required = false) @Valid SessionUser sessionUser, @PathVariable Long id) {

//        if(isAdmin(sessionUser)) {
//            User user = modelMapper.map(userVO, User.class);
//            userService.patchUser(id,user);
//            return ResponseEntity.ok().build();
//        }
        try {
            User user = modelMapper.map(sessionUser, User.class);
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody User user,
                                             @PathVariable Long id) {
//        if(isAdmin(sessionUser)) {
//            userService.patchUser(id,user);
//            return ResponseEntity.ok().build();
//        }
        try {
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/form")
    public String getUserPage(@PathVariable Long id, Model model,@AuthenticationPrincipal SessionUser sessionUser) {

        // 관리자 일 경우 관리자 메인페이지로
        if(isAdmin(sessionUser)) {
            model.addAttribute("user",sessionUser);
            return "/user/admin/main";
        }

        return "/user/main";
    }

    @GetMapping("/{id}/updateForm")
    public String getUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
        if(isAdmin(sessionUser)) {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/adminUpdateForm";
        }

        // 개인화원 자격으로 자신의 회원 정보를 수정할 시 사용
        model.addAttribute("user",sessionUser);
        return "/user/updateForm";
    }

    @GetMapping("/seller")
    @ResponseBody
    public ResponseEntity<String> getSellerInfoByName(@RequestParam String type,
                                                      @RequestParam String value) {

        List<SessionUser> stores = userService.getStore(type, value);
        if(stores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();

    }

    @GetMapping("/seller/{id}")
    public String getSeller(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        if(isOwner(id, sessionUser)) {
            model.addAttribute("user", sessionUser);
            return "redirect:/user/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applyForm")
    public String getApplySellerRoleForm(Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if(sessionUser.getRole().equals(Role.USER) && sessionUser.getStoreVO().getStoreApplyYn().equals("N")) {
        //Todo : session User 이용해서 user 정보 가져온 뒤 비교
            model.addAttribute("user", sessionUser);
            return "/user/applySellerForm";
        }
        return redirectLoginUrl;
    }

    @PostMapping("/seller/{id}/apply")
    public String applySellerRole(@PathVariable Long id,
                                  @Valid ApplySellerForm applyForm,
                                  @AuthenticationPrincipal SessionUser sessionUser) {
        if(isOwner(id, sessionUser)) {
            StoreVO store = modelMapper.map(applyForm, StoreVO.class);
            userService.patchUser(id,User.builder().storeVO(store).build());
            return "redirect:/users/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applicants")
    public String getApplySeller(BoardCriteria boardCriteria,
                                 Model model,
                                 @AuthenticationPrincipal SessionUser sessionUser) {

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
    public ResponseEntity grantSellerRole(@PathVariable Long id, @AuthenticationPrincipal SessionUser sessionUser) {
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

    @GetMapping("/confirm")
    public String confirmUser(@PathVariable String token,
                              RedirectAttributes redirAttrs,
                              @AuthenticationPrincipal SessionUser sessionUser) {

        User user = userService.getUserWithConfirmationByEmail(sessionUser.getEmail()).get();
        if(user.getEmailConfirmYn().equals("Y")) {
            redirAttrs.addFlashAttribute("message","이미 인증된 회원입니다.");
        } else {
            redirAttrs.addFlashAttribute("message","인증 되었습니다.");
            user.setEmailConfirmYn("Y");
            userService.patchUser(user.getUserId(), user);
        }

        return "";
    }


    private boolean isLoginUser(SessionUser sessionUser) {
        if(sessionUser != null) return true;
        return false;
    }

    //세션의 유저와 해당 목표물의 주인의 아이디가 같은지 확인
    private boolean isOwner(Long id, SessionUser sessionUser) {
        return sessionUser != null && sessionUser.getUserId().equals(id);
    }

    private boolean isAdmin(SessionUser sessionUser) {
        return sessionUser != null && (sessionUser.getRole().equals(Role.ADMIN) || (sessionUser.getRole().equals(Role.MASTER)));
    }

}
