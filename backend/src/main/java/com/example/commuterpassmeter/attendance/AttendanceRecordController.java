package com.example.commuterpassmeter.attendance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出社履歴の取得、記録の作成と今日の記録状態を公開するREST Controllerです。
 */
@RestController
@RequestMapping("/api/attendance-records")
public class AttendanceRecordController {

    private final AttendanceRecordService service;

    public AttendanceRecordController(AttendanceRecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<AttendanceRecordResponse> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getHistory(from, to);
    }

    @GetMapping("/monthly-summary")
    public List<MonthlyAttendanceResponse> getMonthlySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getMonthlySummary(from, to);
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
