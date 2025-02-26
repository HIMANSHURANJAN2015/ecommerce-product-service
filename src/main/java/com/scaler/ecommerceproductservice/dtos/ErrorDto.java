package com.scaler.ecommerceproductservice.dtos;

import lombok.Getter;
import lombok.Setter;

//To use with m-2 or m-3 of exception handler i..e using @ExceptionHandler annotation
@Getter
@Setter
public class ErrorDto {
    private String message;
    private String status;
}
