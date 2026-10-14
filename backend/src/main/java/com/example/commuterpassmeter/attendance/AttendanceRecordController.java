package com.example.commuterpassmeter.attendance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出社記録の作成と今日の記録状態を公開するREST Controllerです。
 */
@RestController
@RequestMapping("/api/attendance-records")
public class AttendanceRecordController {

    private final AttendanceRecordService service;

    public AttendanceRecordController(AttendanceRecordService service) {
        this.service = service;
    }

    @GetMapping("/today")
    public TodayAttendanceResponse getToday() {
        return service.getToday();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceRecord create(@RequestBody AttendanceRecordRequest request) {
        return service.create(request);
    }
}
