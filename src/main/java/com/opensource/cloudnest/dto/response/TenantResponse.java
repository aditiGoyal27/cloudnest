package com.opensource.cloudnest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TenantResponse(Long id , LocalDateTime createdAt , LocalDateTime updatedAt , String tenantName , String orgName , String orgAdminName , String orgAdminEmail) {
}
