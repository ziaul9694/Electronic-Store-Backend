package com.cwz.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String jwtToken;
    private UserDto user;
}
