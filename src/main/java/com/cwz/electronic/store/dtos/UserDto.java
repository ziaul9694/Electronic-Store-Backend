package com.cwz.electronic.store.dtos;


import com.cwz.electronic.store.entities.Cart;
import com.cwz.electronic.store.entities.Order;
import com.cwz.electronic.store.entities.Role;
import com.cwz.electronic.store.validation.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;
    @Size(min = 3, max = 20, message = "Name must between 3 to 20 characters")
    @NotBlank(message = "name is required")
    private String name;
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid Email")
//    @Email(message = "This must be an email")
    @NotBlank(message = "email is required")
    private String email;
    @Size(min = 6, max = 15, message = "Password must be between 6 to 15 characters")
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "tell your gender")
    private String gender;
    @Size(max = 1000)
    private String about;
    @ImageNameValid
    private String imageName;
//    private Cart cart;
//    private List<OrderDto> orders = new ArrayList<>();
    private Set<RoleDto> roles = new HashSet<>();
}
