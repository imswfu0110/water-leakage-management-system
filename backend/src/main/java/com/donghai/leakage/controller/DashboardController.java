package com.donghai.leakage.controller;

import com.donghai.leakage.common.Result;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.donghai.leakage.security.RequirePermission;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("/overview")
    @RequirePermission("dashboard:view")
    public Result<Map<String, Object>> overview() {
        return Result.success(Map.of("deviceTotal", 58835, "onlineDevice", 57705, "offlineDevice", 1130,
                "onlineRate", 98.1, "alarmCount", 12));
    }

    @GetMapping("/leakage-trend")
    @RequirePermission("dashboard:view")
    public Result<List<Map<String, Object>>> trend() {
        List<Map<String, Object>> data = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        double[] values = {8.6, 8.1, 7.9, 8.4, 7.5, 7.2, 6.8};
        for (int i = 0; i < days.length; i++) data.add(Map.of("name", days[i], "value", values[i]));
        return Result.success(data);
    }

    @GetMapping("/area-ranking")
    @RequirePermission("dashboard:view")
    public Result<List<Map<String, Object>>> ranking() {
        return Result.success(List.of(Map.of("name", "东部片区", "value", 9.2), Map.of("name", "南部片区", "value", 8.4),
                Map.of("name", "中心片区", "value", 7.8), Map.of("name", "北部片区", "value", 6.9), Map.of("name", "西部片区", "value", 5.7)));
    }
}
