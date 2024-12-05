package com.opensource.cloudnest.dto.response;

import lombok.Builder;

@Builder
public record ProfileInfoResponse(long id , String name ,
                                  String password , String email  ,
                                   String role , String status ,
                                  boolean enabled) {
}
