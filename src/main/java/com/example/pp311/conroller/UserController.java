package com.example.pp311.conroller;


import com.example.pp311.model.Role;
import com.example.pp311.model.User;
import com.example.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    public UserController() {
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String getUserProfile(Authentication authentication){
        return "redirect:/user/" + authentication.getName();
    }

    @GetMapping("/user/{userName}")
    public String viewUserProfile(@PathVariable("userName") String username,
                                  Model model, Authentication authentication) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if(roles.contains("ROLE_ADMIN") || authentication.getName().equals(username)){
            User user = userService.getUserByUserName(username);
            model.addAttribute("user",user);
            model.addAttribute("roles",roles);
            return "user";
        }
        return "redirect:/user/" + authentication.getName();
    }

    @GetMapping("/admin/users")
    public String listAllUsers(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "admin/allUsers";
    }
    @RequestMapping(value = "/admin/user-create", method = RequestMethod.GET)
    public String createUserForm(Model model){
        User user = new User();
        user.setPassword("100");//default password
        List<Role> allRoles = userService.getAllRoles();
        model.addAttribute("allRoles",allRoles);
        model.addAttribute("user", user);
        return "admin/user-create";
    }

    @PostMapping("/admin/user-create")
    public String createUser(User user,@RequestParam(value = "select_role",required = false) String [] role,
                             @RequestParam("password") String password){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));

        List <Role> roleList = new ArrayList<>();
        for (String r: role){
            roleList.add((userService.getAllRoles()
                    .stream()
                    .filter(role1 -> role1.getRole().equals(r)).findFirst().get()));
        }
        user.setRoles(roleList);
        userService.add(user);

        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/user-update/{id}", method = RequestMethod.GET)
    public  String updateUserForm (@PathVariable("id") Long id, Model model) {
        model.addAttribute("allRoles",userService.getAllRoles());
        model.addAttribute("user",userService.getUserById(id));
        return "admin/user-update";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(User user,
          @RequestParam(value = "select_roles",required = false) String [] role) {
        List <Role> roleList = new ArrayList<>();
        for (String r: role){
            roleList.add(userService.getAllRoles()
                    .stream()
                    .filter(role1 -> role1.getRole().equals(r)).findFirst().get());
        }
        user.setRoles(roleList);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
