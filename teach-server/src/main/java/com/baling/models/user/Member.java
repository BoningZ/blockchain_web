package com.baling.models.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "mid"),
        })
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Size(max = 20)
    private String mid;
    @Size(max = 20)
    private String name;

    @OneToOne
    @NotNull
    @JoinColumn(name="user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer studentId) {
        this.id = studentId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String sid) {
        this.mid = sid;
    }





}
