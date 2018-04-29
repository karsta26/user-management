package com.app.management.usergroup;

import com.app.management.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(min=5, max=30)
    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

    public UserGroup() { }

    public UserGroup(String name) {
        this.name = name;
    }

    public UserGroup(String name, Set<User> users) {
        this.name = name;
        this.users = users;
    }

    @PreRemove
    private void removeGroupsFromUsers() {
        for (User user : users) {
            user.getGroups().remove(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean haveUser(Integer id) {
        if(users!=null)
            return users.stream().anyMatch(t -> t.getId().equals(id));
        return false;
    }
}
