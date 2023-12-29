package vn.winter.usercenter.util.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private ArrayList<String> errors;
    private int status;
    private String details;
}
