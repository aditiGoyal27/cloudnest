package com.opensource.cloudnest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
public record DashBoardResponse(Long id , String tenantName , String orgName , String orgAdminName , String orgAdminEmail ,
                                List<ProfileInfoResponse> profileInfoResponseList ,
                                Long billingAmount) {
}
