package com.xuannguyen.product_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PRODUCT_EXITED(1101, "Pproduct exitsted", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXITS(1103, "Pproduct not exitst", HttpStatus.BAD_REQUEST),
    NAME_PRDUCT_NOTNULL(1102, "Product name must not be null", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXITS(1104, "Category not exitst", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_EXITS(1105, "Image not exitst", HttpStatus.BAD_REQUEST),
    BRAND_NOT_EXITS(1106, "Brand not exitst", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED(1107, "Brand existed", HttpStatus.BAD_REQUEST),
    FILE_NOT_SUPPORT(1108, "File not support", HttpStatus.BAD_REQUEST),
    FILE_NOT_UPLOAD(1109, "File not upload", HttpStatus.BAD_REQUEST),
    INVALID_FILE(1110, "Invalid file", HttpStatus.BAD_REQUEST),
    CATEGORY_EXITS(1111, "Category exitsted", HttpStatus.BAD_REQUEST),
    NAME_EXITSTED(1112, "Name exitsted", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
