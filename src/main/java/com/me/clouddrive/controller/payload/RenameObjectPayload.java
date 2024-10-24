package com.me.clouddrive.controller.payload;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RenameObjectPayload {
    private String location;
    @Pattern(regexp = "[a-z0-9.-]+")
    private String newName;
    private String oldName;
}
