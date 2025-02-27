package com.opensource.cloudnest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileInfoResponse(long id , String name ,
                                   String email  ,
                                   String role , String status , long tenantId ,long createdOn ,long updatedOn,
                                  boolean enabled) {
}
