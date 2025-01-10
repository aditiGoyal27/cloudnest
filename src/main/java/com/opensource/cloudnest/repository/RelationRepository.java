package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation , Long> {
List<Relation> findByAdminId(Profile adminId);
void deleteByUserId(Profile user);
   // List<Relation> findByProfile(Profile profile);
    @Modifying
    @Query("DELETE FROM Relation r WHERE r.adminId = :profile OR r.userId = :profile")
    void deleteByProfile(@Param("profile") Profile profile);

}
