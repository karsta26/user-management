package com.app.management.usergroup;

import com.app.management.user.User;
import com.app.management.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupService {

    private UserGroupRepository userGroupRepository;
    private UserService userService;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void addUsersForUserGroupAndDeletePrevious(Integer userGroupId, List<Integer> usersIds) {
        UserGroup userGroup = getUserGroup(userGroupId);
        userGroup.getUsers().clear();

        for(User user : userService.getAllUsers()) {
            user.getGroups().removeIf(userGroup1 -> userGroup1.getId().equals(userGroupId));
        }

        for(User user : userService.getAllUsers()) {
            if (usersIds.contains(user.getId())) {
                userGroup.getUsers().add(user);
                user.getGroups().add(userGroup);
                userService.addUser(user);
            }
        }
        this.addUserGroup(userGroup);
    }

    public List<UserGroup> getAllUserGroups() {
        List<UserGroup> userGroups = new ArrayList<>();
        userGroupRepository.findAll().forEach(userGroups::add);
        return userGroups;
    }

    public UserGroup getUserGroup(Integer id) {
        return userGroupRepository.findById(id).get();
    }

    public void addUserGroup(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    public void updateUserGroup(Integer id, UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    public void deleteUserGroup(Integer id) {
        userGroupRepository.deleteById(id);
    }
}
