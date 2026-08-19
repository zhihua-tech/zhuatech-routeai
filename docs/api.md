# RouteAI API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 智能配送调度中心 |
| GET | `/api/admin/work-orders` | 配送路线任务 |
| GET | `/api/shopfloor/dashboard` | 调度员工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交调度反馈 |
| POST | `/api/ai/route/plan` | ETA、路线风险、发车结论与提醒 |
| POST | `/api/shopfloor/ai-risk-assessment` | AI 功能上线风险初筛 |

除登录外均需 `Authorization: Bearer <token>`。
