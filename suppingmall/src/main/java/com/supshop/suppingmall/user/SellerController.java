package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.PasswordCheckForm;
import com.supshop.suppingmall.user.Form.UpdateSellerForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users/seller")
@Controller
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String redirectLoginUrl = "redirect:/users/loginform";
    private static final String redirectMainUrl = "redirect:/";
    private static final int pagingCount = 15;



    @GetMapping("/{id}/main")
    public String getSellerInfoPage(@PathVariable Long id,
                                    @AuthenticationPrincipal SessionUser sessionUser,
                                    Model model) {


        if(!UserUtils.isOwner(id, sessionUser)) {
            return redirectMainUrl;
        }
        User user = userService.getUser(id);
        model.addAttribute("store",user.getStoreVO());

        return "/user/seller/main";

    }

    @GetMapping("/{id}/updateForm")
    public String getSellerUpdateForm(@PathVariable Long id,
                                    @AuthenticationPrincipal SessionUser sessionUser,
                                    Model model) {


        if(!UserUtils.isOwner(id, sessionUser)) {
            return redirectMainUrl;
        }
        User user = userService.getUser(id);
        model.addAttribute("store",user.getStoreVO());

        return "/user/seller/updateForm";

    }


    @GetMapping("")
    @ResponseBody
    public ResponseEntity<String> getSellers(@RequestParam(required = false) String type,
                                             @RequestParam(required = false) String value) {

        List<SessionUser> stores = userService.getStore(type, value);
        if(stores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{id}")
    public String getSeller(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isOwner(id, sessionUser)) {
            model.addAttribute("user", sessionUser);
            return "redirect:/user/"+id+"/form";
        }
        return redirectLoginUrl;
    }


    @GetMapping("/applyForm")
    public String getApplySellerRoleForm(Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if (sessionUser.getRole().equals(Role.USER) && sessionUser.getStoreVO().getStoreApplyYn().equals("N")) {
            model.addAttribute("user", sessionUser);
            return "/user/seller/applySellerForm";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/applicants")
    public String getApplySeller(ThirtyItemsCriteria thirtyItemsCriteria,
                                 Model model,
                                 @AuthenticationPrincipal SessionUser sessionUser) {

        if(UserUtils.isAdmin(sessionUser)) {

            List<User> applySellerUsers = userService.getApplySellerUsers(thirtyItemsCriteria);
            PageMaker pageMaker = new PageMaker(applySellerUsers.size(),pagingCount, thirtyItemsCriteria);

            model.addAttribute("userList",applySellerUsers);
            model.addAttribute("pageMaker", pageMaker);
            return "/user/admin/applySellerList";
        }
        return redirectLoginUrl;
    }

    @PostMapping("/{id}/apply")
    public String applySellerRole(@PathVariable Long id,
                                  @Valid ApplySellerForm applyForm,
                                  @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isOwner(id, sessionUser)) {
            StoreVO store = modelMapper.map(applyForm, StoreVO.class);
            userService.patchUser(id,User.builder().storeVO(store).build());
            return "redirect:/users/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @PatchMapping("/{id}/apply")
    @ResponseBody
    public ResponseEntity grantSellerRole(@PathVariable Long id, @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isAdmin(sessionUser)) {
            try {
                User user = userService.getUser(id);
                StoreVO storeVO = user.getStoreVO();
                storeVO.setStoreApplyYn("N");
                user.setStoreVO(storeVO);
                user.setRole(Role.SELLER);
                userService.patchUser(user.getUserId(), user);
            } catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity updateUser(@PathVariable Long id,
                                     @RequestBody @Valid UpdateSellerForm form,
                                     @AuthenticationPrincipal SessionUser sessionUser) {

        if(!UserUtils.isOwner(id, sessionUser) || !"Y".equals(sessionUser.getStoreVO().getStoreApplyYn())) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User originUser = userService.getUser(id);

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 패스워드 입니다.");
        }

        StoreVO storeVO = modelMapper.map(form, StoreVO.class);
        try {
            userService.patchUser(id, User.builder().storeVO(storeVO).build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("회원변경 실패");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/transfer")
    public String getTransferForm(@PathVariable Long id,
                                  @AuthenticationPrincipal SessionUser sessionUser,
                                  Model model){

        if(!UserUtils.isOwner(id, sessionUser) || !"Y".equals(sessionUser.getStoreVO().getStoreApplyYn())) {
            return redirectMainUrl;
        }

        User user = userService.getUser(id);

        model.addAttribute("user",user);

        return "/user/seller/transferForm";
    }


    @PatchMapping("/{id}/transfer")
    @ResponseBody
    public ResponseEntity transferSellerToUser(@PathVariable Long id,
                                               @RequestBody PasswordCheckForm form,
                                               @AuthenticationPrincipal SessionUser sessionUser){

        if(!UserUtils.isOwner(id, sessionUser) || !"Y".equals(sessionUser.getStoreVO().getStoreApplyYn())) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User originUser = userService.getUser(id);

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 패스워드 입니다.");
        }

        try {
            User user = userService.getUser(id);
            user.setRole(Role.USER);
            userService.patchUser(user.getUserId(), user);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
