/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.routeai.controller;

import cn.zhuatech.routeai.common.ApiResponse;
import cn.zhuatech.routeai.service.RouteAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/ai/route")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class RouteAnalysisController {
    private final RouteAnalysisService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public RouteAnalysisController(RouteAnalysisService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/plan")
    public ApiResponse<RouteAnalysisService.Result> plan(@Valid @RequestBody RouteAnalysisService.Request request) {
        return ApiResponse.ok("配送路线风险评估完成", service.plan(request));
    }
}
