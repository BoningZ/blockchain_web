package com.baling.models.log;

import com.baling.models.right.RightType;
import com.baling.models.user.User;
import com.baling.util.CommonMethod;
import com.sansec.MobileShieldEncInfo;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="right_type_id")
    private RightType rightType;

    @Size(max = 300)
    private String description;

    @NotNull
    private Date operateTime;

    private int operateState;

    private String hash;

    public Log() {
    }

    public Log(User user, RightType rightType, String description) {
        this.user = user;
        this.rightType = rightType;
        this.description = description;
        this.operateTime=new Date();
    }

    public boolean audit(){
        return this.hash.equals(CommonMethod.dataHash16(getInfo()));
    }

    public void generateHash(){
        this.hash= CommonMethod.dataHash16(getInfo());
    }

    private String getInfo(){
        return this.id+this.user.getUserId()+this.rightType.getId()+this.description+this.operateTime.getTime()+this.operateState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public int getOperateState() {
        return operateState;
    }

    public void setOperateState(int operateState) {
        this.operateState = operateState;
        generateHash();
    }

    public String getHash() {
        return hash;
    }
}
