package com.baling.models.log;

import javax.persistence.*;

@Entity
@Table(name = "operate")
public class Operate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EOperate name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EOperate getName() {
        return name;
    }

    public void setName(EOperate name) {
        this.name = name;
    }
}
