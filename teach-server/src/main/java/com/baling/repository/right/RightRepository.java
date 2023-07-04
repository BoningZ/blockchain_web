package com.baling.repository.right;

import com.baling.models.right.RightType;
import com.baling.models.user.Admin;
import com.baling.models.right.ERightType;
import com.baling.models.right.Right;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RightRepository extends JpaRepository<Right,Integer> {
    List<Right> getRightsByAdminAndRightTypeAndNameLike(Admin admin, RightType rightType, String name);
    List<Right> getRightsByAdminAndNameLike(Admin admin,String name);
    List<Right> getRightsByNameLike(String name);
    void deleteById(Integer id);
}
