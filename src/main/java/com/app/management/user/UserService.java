package com.app.management.user;

import com.app.management.usergroup.UserGroup;
import com.app.management.usergroup.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserGroupService userGroupService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public void addGroupsForUserAndDeletePrevious(Integer userId, List<Integer> userGroupIds) {
        User user = getUser(userId);
        user.getGroups().clear();

        for(UserGroup userGroup : userGroupService.getAllUserGroups()) {
            userGroup.getUsers().removeIf(userGroup1 -> userGroup1.getId().equals(userId));
        }

        for(UserGroup userGroup : userGroupService.getAllUserGroups()) {
            if (userGroupIds.contains(userGroup.getId())) {
                userGroup.getUsers().add(user);
                user.getGroups().add(userGroup);
                userGroupService.addUserGroup(userGroup);
            }
        }
        this.addUser(user);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Integer id, User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
