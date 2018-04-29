package com.app.management.user;

import com.app.management.usergroup.UserGroup;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(min=5, max=30)
    @NotBlank
    private String userName;

    @Size(min = 5, max = 30)
    @NotBlank
    private String password;

    @Size(min = 2, max = 30)
    @NotBlank
    private String firstName;

    @Size(min = 2, max = 30)
    @NotBlank
    private String lastName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @NotNull @Past
    private Date dateOfBirth;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    private Set<UserGroup> groups;

    public User() { }

    public User(String userName, String password, String firstName, String lastName, Date dateOfBirth) {

        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String userName, String password, String firstName, String lastName, Date dateOfBirth, Set<UserGroup> groups) {

        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public boolean participateInGroup(Integer id) {
        if(groups!=null)
            return groups.stream().anyMatch(t -> t.getId().equals(id));
        return false;
    }
}
