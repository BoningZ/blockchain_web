package com.baling.models.right;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(	name = "right_type")
public class RightType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @NotNull
    private ERightType value;

    private String label;

    private boolean adminOnly;

    public boolean isAdminOnly() {
        return adminOnly;
    }

    public void setAdminOnly(boolean adminOnly) {
        this.adminOnly = adminOnly;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERightType getValue() {
        return value;
    }

    public void setValue(ERightType name) {
        this.value = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
