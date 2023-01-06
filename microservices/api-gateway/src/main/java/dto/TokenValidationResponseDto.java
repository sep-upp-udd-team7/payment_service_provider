package dto;

import lombok.Data;

@Data
public class TokenValidationResponseDto {
    private String clientId;
    private String clientSecret;
}
