package com.baling.models.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(	name = "user_right",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id","right_id"}),
        })
public class UserRight {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @NotNull
        @JoinColumn(name = "member_id")
        private Member member;

        @ManyToOne
        @NotNull
        @JoinColumn(name = "right_id")
        private Right right;

        private Date createTime;

        private Date updateTime;

        public UserRight() {
        }

        public UserRight(Member member, Right right) {
                this.member = member;
                this.right = right;
                this.createTime=this.updateTime=new Date();
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Member getMember() {
                return member;
        }

        public void setMember(Member member) {
                this.member = member;
        }

        public Right getRight() {
                return right;
        }

        public void setRight(Right right) {
                this.right = right;
        }

        public Date getCreateTime() {
                return createTime;
        }

        public void setCreateTime(Date createTime) {
                this.createTime = createTime;
        }

        public Date getUpdateTime() {
                return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
                this.updateTime = updateTime;
        }
}
