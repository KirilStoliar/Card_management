package com.stoliar.cardManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stoliar.cardManagement.model.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private User.Role role;
    
    // For admin only
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean accountNonExpired;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean accountNonLocked;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean credentialsNonExpired;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean enabled;
}