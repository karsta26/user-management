package com.app.management.controllers;

import com.app.management.user.User;
import com.app.management.user.UserService;
import com.app.management.usergroup.UserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserInterfaceController {

    private UserService userService;
    private UserGroupService userGroupService;
    private Map<String, String> myMenu;
    private static final Logger logger = LoggerFactory
            .getLogger(UserInterfaceController.class);

    @Autowired
    public UserInterfaceController(UserService userService, UserGroupService userGroupService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
        myMenu = new LinkedHashMap<>();
        myMenu.put("list","List of users");
        myMenu.put("add", "Add user");
        myMenu.put("edit", "Edit user");
        myMenu.put("delete", "Delete user");
        myMenu.put("group", "Edit user groups");
    }

    @RequestMapping("/user")
    public String getMainMenuForUser(Model model) {
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "User");
        return "main-menu";
    }

    @RequestMapping("/user/list")
    public String getListOfUsers(Model model) {
        List<User> myList = userService.getAllUsers();
        List<String> columnNames = new LinkedList<>();
        columnNames.add("First Name");
        columnNames.add("Last Name");
        columnNames.add("User Name");
        columnNames.add("Password");
        columnNames.add("Date of birth");
        columnNames.add("User groups");
        model.addAttribute("userList", myList);
        model.addAttribute("columnNames", columnNames);
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "List of users");
        return "list";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Add user");
        return "add";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String addUserSubmit(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation error. Returning add page.");
            model.addAttribute("user", user);
            model.addAttribute("menuList", myMenu);
            model.addAttribute("prefix", "user");
            model.addAttribute("title", "Add user");
            return "add";
        }
        try {
            User user1 = userService.getUser(user.getId());
            user.setGroups(user1.getGroups());
            logger.info("Updating user.");
        }
        catch (Exception e) {
            logger.info("Adding new user.");
        }
        userService.addUser(user);
        model.addAttribute("message", "Added");
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Add user");
        return "add";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public String chooseUserToEdit(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("user", new User());
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user");
        return "edit";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String editUser(Model model, HttpServletRequest request) {
        String userId = request.getParameter("selectedValue");
        logger.info("Editing user with id = " + userId);
        User user = userService.getUser(Integer.valueOf(userId));
        model.addAttribute("user", user);
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user");
        return "add";
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public String chooseUserToDelete(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("user", new User());
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Delete user");
        return "delete";
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String deleteUser(Model model, HttpServletRequest request) {
        String userId = request.getParameter("selectedValue");
        userService.deleteUser(Integer.valueOf(userId));
        logger.info("Deleted user with id = " + userId);
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("user", new User());
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user");
        model.addAttribute("message", "User deleted!");
        return "delete";
    }

    @RequestMapping(value = "/user/group", method = RequestMethod.GET)
    public String editUserGroupForm(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("user", new User());
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user groups");
        model.addAttribute("showForm1", true);
        model.addAttribute("showForm2", false);
        return "edit-list";
    }

    @RequestMapping(value = "/user/group", method = RequestMethod.POST)
    public String editUserGroupList(Model model, HttpServletRequest request) {
        String userId = request.getParameter("selectedValue");
        User user = userService.getUser(Integer.valueOf(userId));
        model.addAttribute("user", user);
        model.addAttribute("userGroups", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user groups");
        model.addAttribute("showForm1", false);
        model.addAttribute("showForm2", true);
        return "edit-list";
    }

    @RequestMapping(value = "/user/edit-group", method = RequestMethod.POST)
    public String editUserGroup(Model model, HttpServletRequest request) {
        String userId = request.getParameter("selectedValue");
        User user = userService.getUser(Integer.valueOf(userId));

        Enumeration<String> parameterNames = request.getParameterNames();
        List<Integer> userGroupsIds = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            if(!key.equals("selectedValue"))
                userGroupsIds.add(Integer.valueOf(key));
        }
        userService.addGroupsForUserAndDeletePrevious(user.getId(), userGroupsIds);

        model.addAttribute("user", user);
        model.addAttribute("userGroups", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "user");
        model.addAttribute("title", "Edit user groups");
        model.addAttribute("showForm1", false);
        model.addAttribute("showForm2", true);
        model.addAttribute("message", "Group edited!");
        return "edit-list";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
