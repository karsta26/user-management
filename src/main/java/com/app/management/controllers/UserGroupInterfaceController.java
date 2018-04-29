package com.app.management.controllers;

import com.app.management.user.UserService;
import com.app.management.usergroup.UserGroup;
import com.app.management.usergroup.UserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserGroupInterfaceController {

    private UserService userService;
    private UserGroupService userGroupService;
    private Map<String, String> myMenu;
    private static final Logger logger = LoggerFactory
            .getLogger(UserGroupInterfaceController.class);

    @Autowired
    public UserGroupInterfaceController(UserService userService, UserGroupService userGroupService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
        myMenu = new LinkedHashMap<>();
        myMenu.put("list","List of user groups");
        myMenu.put("add", "Add user group");
        myMenu.put("edit", "Edit user group");
        myMenu.put("delete", "Delete user group");
        myMenu.put("group", "Edit user groups");
    }

    @RequestMapping("/userGroup")
    public String getMainMenuForUser(Model model) {
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "User group");
        return "main-menu";
    }

    @RequestMapping("/userGroup/list")
    public String getListOfUserGroups(Model model) {
        List<UserGroup> myList = userGroupService.getAllUserGroups();
        List<String> columnNames = new LinkedList<>();
        columnNames.add("Group name");
        columnNames.add("Users in group");
        model.addAttribute("userList", myList);
        model.addAttribute("columnNames", columnNames);
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "List of user groups");
        return "list";
    }

    @RequestMapping(value = "/userGroup/add", method = RequestMethod.GET)
    public String addUserGroup(Model model) {
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Add user group");
        return "add";
    }

    @RequestMapping(value = "/userGroup/add", method = RequestMethod.POST)
    public String addUserSubmit(@Valid @ModelAttribute("userGroup") UserGroup userGroup, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation error. Returning add page.");
            model.addAttribute("userGroup", userGroup);
            model.addAttribute("menuList", myMenu);
            model.addAttribute("prefix", "userGroup");
            model.addAttribute("title", "Add user group");
            return "add";
        }
        try {
            UserGroup userGroup1= userGroupService.getUserGroup(userGroup.getId());
            userGroup1.setUsers(userGroup1.getUsers());
            logger.info("Updating user group.");
        }
        catch (Exception e) {
            logger.info("Adding new user group.");
        }
        userGroupService.addUserGroup(userGroup);
        model.addAttribute("message", "Added");
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Add user group");
        return "add";
    }

    @RequestMapping(value = "/userGroup/edit", method = RequestMethod.GET)
    public String chooseUserGroupToEdit(Model model) {
        model.addAttribute("userGroupList", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit user group");
        return "edit";
    }

    @RequestMapping(value = "/userGroup/edit", method = RequestMethod.POST)
    public String editUser(Model model, HttpServletRequest request) {
        String userGroupId = request.getParameter("selectedValue");
        logger.info("Editing user group with id = " + userGroupId);
        UserGroup userGroup = userGroupService.getUserGroup(Integer.valueOf(userGroupId));
        model.addAttribute("userGroup", userGroup);
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit user group");
        return "add";
    }

    @RequestMapping(value = "/userGroup/delete", method = RequestMethod.GET)
    public String chooseUserGroupToDelete(Model model) {
        model.addAttribute("userGroupList", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Delete user group");
        return "delete";
    }

    @RequestMapping(value = "/userGroup/delete", method = RequestMethod.POST)
    public String deleteUser(Model model, HttpServletRequest request) {
        String userGroupId = request.getParameter("selectedValue");
        userGroupService.deleteUserGroup(Integer.valueOf(userGroupId));
        logger.info("Deleted user group with id = " + userGroupId);
        model.addAttribute("userGroupList", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit user group");
        model.addAttribute("message", "User group deleted!");
        return "delete";
    }

    @RequestMapping(value = "/userGroup/group", method = RequestMethod.GET)
    public String editUsersForm(Model model) {
        model.addAttribute("userGroupList", userGroupService.getAllUserGroups());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit users");
        model.addAttribute("showForm1", true);
        model.addAttribute("showForm2", false);
        return "edit-list";
    }

    @RequestMapping(value = "/userGroup/group", method = RequestMethod.POST)
    public String editUserGroupList(Model model, HttpServletRequest request) {
        String userId = request.getParameter("selectedValue");
        UserGroup userGroup= userGroupService.getUserGroup(Integer.valueOf(userId));
        model.addAttribute("userGroup", userGroup);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit users");
        model.addAttribute("showForm1", false);
        model.addAttribute("showForm2", true);
        return "edit-list";
    }

    @RequestMapping(value = "/userGroup/edit-user", method = RequestMethod.POST)
    public String editUserGroup(Model model, HttpServletRequest request) {
        String userGroupId = request.getParameter("selectedValue");
        UserGroup userGroup = userGroupService.getUserGroup(Integer.valueOf(userGroupId));

        Enumeration<String> parameterNames = request.getParameterNames();
        List<Integer> usersIds = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            if(!key.equals("selectedValue"))
                usersIds.add(Integer.valueOf(key));
        }

        userGroupService.addUsersForUserGroupAndDeletePrevious(userGroup.getId(), usersIds);

        model.addAttribute("userGroup", userGroup);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("menuList", myMenu);
        model.addAttribute("prefix", "userGroup");
        model.addAttribute("title", "Edit users");
        model.addAttribute("showForm1", false);
        model.addAttribute("showForm2", true);
        model.addAttribute("message", "Users edited!");
        return "edit-list";
    }
}
