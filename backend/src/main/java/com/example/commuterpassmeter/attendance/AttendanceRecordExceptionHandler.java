package com.example.commuterpassmeter.attendance;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.commuterpassmeter.commuterpass.ApiErrorResponse;

/**
 * 出社記録APIで発生した例外をHTTPレスポンスへ変換します。
 */
@RestControllerAdvice
public class AttendanceRecordExceptionHandler {

    @ExceptionHandler(InvalidAttendanceRecordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleInvalidAttendanceRecord(InvalidAttendanceRecordException exception) {
        return new ApiErrorResponse(exception.getMessage(), List.of());
    }

    @ExceptionHandler(DuplicateAttendanceRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleDuplicateAttendanceRecord(DuplicateAttendanceRecordException exception) {
        return new ApiErrorResponse(exception.getMessage(), List.of());
    }
}
