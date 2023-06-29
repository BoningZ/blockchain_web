package com.baling.repository.user;

import com.baling.models.user.Admin;
import com.baling.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Boolean existsByAid(String aid);
    Admin getAdminByUser(User user);
}
