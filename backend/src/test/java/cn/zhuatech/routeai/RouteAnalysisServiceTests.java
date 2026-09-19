/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.routeai;

import cn.zhuatech.routeai.service.RouteAnalysisService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class RouteAnalysisServiceTests {
    private final RouteAnalysisService service = new RouteAnalysisService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void replansColdChainRouteUnderSevereConditions() {
        var result = service.plan(new RouteAnalysisService.Request("RT-01", new BigDecimal("180"), 8, new BigDecimal("0.85"), new BigDecimal("0.75"), 98, 9, true, 240));
        assertThat(result.dispatchDecision()).isEqualTo("REPLAN");
        assertThat(result.dispatcherApprovalRequired()).isTrue();
        assertThat(result.alerts()).anyMatch(item -> item.contains("冷链"));
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void dispatchesHealthyUrbanRoute() {
        var result = service.plan(new RouteAnalysisService.Request("RT-02", new BigDecimal("32"), 4, new BigDecimal("0.20"), new BigDecimal("0.10"), 72, 3, false, 300));
        assertThat(result.dispatchDecision()).isEqualTo("DISPATCH");
    }
}
