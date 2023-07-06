package com.baling.repository.right;

import com.baling.models.right.RightType;
import com.baling.models.user.Admin;
import com.baling.models.right.ERightType;
import com.baling.models.right.Right;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RightRepository extends JpaRepository<Right,Integer> {
    List<Right> getRightsByAdminAndRightTypeAndNameLike(Admin admin, RightType rightType, String name);
    Page<Right> getRightPageByAdminAndRightTypeAndNameLike(Admin admin, RightType rightType, String name, Pageable pageable);
    List<Right> getRightsByAdminAndNameLike(Admin admin,String name);
    Page<Right> getRightPageByAdminAndNameLike(Admin admin,String name,Pageable pageable);
    List<Right> getRightsByNameLike(String name);
    void deleteById(Integer id);
}
