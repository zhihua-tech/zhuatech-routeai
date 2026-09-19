/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.routeai.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 综合交通、天气、载重与司机工时评估配送路线。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class RouteAnalysisService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result plan(Request request) {
        BigDecimal speedFactor = BigDecimal.ONE.subtract(request.trafficIndex().multiply(new BigDecimal("0.55")))
            .subtract(request.weatherRisk().multiply(new BigDecimal("0.25"))).max(new BigDecimal("0.20"));
        BigDecimal effectiveSpeed = new BigDecimal("48").multiply(speedFactor);
        int etaMinutes = request.distanceKm().divide(effectiveSpeed, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("60")).add(BigDecimal.valueOf(request.stopCount() * 8L))
            .setScale(0, RoundingMode.CEILING).intValue();
        int riskScore = request.trafficIndex().multiply(new BigDecimal("35")).intValue()
            + request.weatherRisk().multiply(new BigDecimal("30")).intValue();
        if (request.vehicleLoadPercent() > 95) riskScore += 20;
        if (request.driverHoursToday() >= 8) riskScore += 30;
        if (request.coldChain() && etaMinutes > request.maximumColdChainMinutes()) riskScore += 35;
        riskScore = Math.min(100, riskScore);
        String decision = request.driverHoursToday() >= 10 ? "HOLD" : riskScore >= 65 ? "REPLAN" : "DISPATCH";
        List<String> alerts = new ArrayList<>();
        if (request.trafficIndex().compareTo(new BigDecimal("0.70")) > 0) alerts.add("路线拥堵指数偏高");
        if (request.weatherRisk().compareTo(new BigDecimal("0.60")) > 0) alerts.add("恶劣天气可能影响准时率");
        if (request.driverHoursToday() >= 8) alerts.add("司机当日工时接近或超过安全门槛");
        if (request.coldChain() && etaMinutes > request.maximumColdChainMinutes()) alerts.add("预计时长超过冷链交付窗口");
        if (alerts.isEmpty()) alerts.add("路线时效与安全约束均满足要求");
        return new Result(request.routeNo(), etaMinutes, riskScore, decision, alerts,
            "DISPATCH".equals(decision) ? "按计划发车" : "重新分配车辆、司机或途经节点", !"DISPATCH".equals(decision));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String routeNo,
                          @DecimalMin("0.1") BigDecimal distanceKm,
                          @Min(1) int stopCount,
                          @DecimalMin("0") @DecimalMax("1") BigDecimal trafficIndex,
                          @DecimalMin("0") @DecimalMax("1") BigDecimal weatherRisk,
                          @Min(0) @Max(120) int vehicleLoadPercent,
                          @Min(0) @Max(24) int driverHoursToday,
                          boolean coldChain, @Min(1) int maximumColdChainMinutes) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String routeNo, int estimatedMinutes, int riskScore,
                         String dispatchDecision, List<String> alerts,
                         String recommendation, boolean dispatcherApprovalRequired) {}
}
