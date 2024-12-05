package com.opensource.cloudnest.repository;

import com.opensource.cloudnest.entity.Profile;
import com.opensource.cloudnest.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation , Long> {
List<Relation> findByAdminId(Profile adminId);
void deleteByUserId(Profile user);
}
