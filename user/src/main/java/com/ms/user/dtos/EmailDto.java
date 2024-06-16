package com.ms.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private UUID userId;
    private String emailTo;
    private String subject;
    private String text;
}
