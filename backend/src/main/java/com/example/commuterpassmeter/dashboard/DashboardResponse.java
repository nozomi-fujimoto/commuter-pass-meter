package com.example.commuterpassmeter.dashboard;

import java.math.BigDecimal;
import java.util.List;

/** Dashboard表示用の集計済みレスポンスです。 */
public record DashboardResponse(
        CommuterPassResponse commuterPass,
        int attendanceCount,
        long monthlyAttendanceCount,
        long normalFareTotal,
        long difference,
        BigDecimal recoveryRate,
        long breakEvenAttendanceCount,
        long remainingAttendanceCount,
        boolean hasAttendedToday,
        List<RecentAttendanceRecordResponse> recentAttendanceRecords) {
}
