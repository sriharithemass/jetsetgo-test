package com.training.payload;

import com.training.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
