package com.app.management.usergroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class UserGroupController {

    private UserGroupService userGroupService;

    @Autowired
    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @RequestMapping("/userGroups")
    public List<UserGroup> getAllUserGroups() {
        return userGroupService.getAllUserGroups();
    }

    @RequestMapping("/userGroups/{id}")
    public UserGroup getUserGroup(@PathVariable Integer id) {
        return userGroupService.getUserGroup(id);
    }

    @RequestMapping(value = "/userGroups", method = RequestMethod.POST)
    public void addUserGroup(@Valid @RequestBody UserGroup userGroup) {
        userGroupService.addUserGroup(userGroup);
    }

    @RequestMapping(value = "/userGroups/{id}", method = RequestMethod.PUT)
    public void updateUserGroup(@Valid @RequestBody UserGroup userGroup, @PathVariable Integer id) {
        userGroupService.updateUserGroup(id, userGroup);
    }

    @RequestMapping(value = "/userGroups/{id}", method = RequestMethod.DELETE)
    public void deleteUserGroup(@PathVariable Integer id) {
        userGroupService.deleteUserGroup(id);
    }
}
