package com.opensource.cloudnest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
public record DashBoardResponse(Long id , String orgName ,
                                String orgUnitName , String orgLocation ,
                                String orgEmail , String orgContactNumber ,
                                String status  , Date createdAt ,
                                Date updatedAt , String adminName ,
                                List<ProfileInfoResponse> profileInfoResponseList ,
                                Long billingAmount) {
}
