package com.paymybudy.transfer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>User Dto for registration and login</b>
 * <p>this dto is used by spring security</p>
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
}
