package com.opensource.cloudnest.dto.response;

import lombok.Builder;

@Builder
public record ProfileInfoResponse(long id , String name , String userName,
                                  String password , String email , String contactNumber ,
                                  String orgname , String role , String status ,
                                  boolean enabled) {
}
