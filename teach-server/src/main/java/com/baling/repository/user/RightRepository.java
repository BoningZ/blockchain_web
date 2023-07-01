package com.baling.repository.user;

import com.baling.models.user.Admin;
import com.baling.models.user.ERightType;
import com.baling.models.user.Right;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RightRepository extends JpaRepository<Right,Integer> {
    List<Right> getRightsByAdminAndTypeAndNameLike(Admin admin,ERightType type,String name);
    void deleteById(Integer id);
}
