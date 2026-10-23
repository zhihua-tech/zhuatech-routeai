/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.routeai.workbench;

import static cn.zhuatech.routeai.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "depot", "stops", "returnToDepot");
    var depot = object(r.get("depot"));
    geoPoint(depot);
    var remaining = new ArrayList<>(rows(r, "stops", 0, 80));
    Set<String> ids = new HashSet<>();
    unique(ids, id(depot, "id"));
    for (var s : remaining) {
      geoPoint(s);
      unique(ids, id(s, "id"));
    }
    boolean returnHome = flag(r, "returnToDepot");
    remaining.sort(Comparator.comparing(s -> s.get("id").toString()));
    var current = depot;
    double total = 0;
    List<Map<String, Object>> legs = new ArrayList<>();
    while (!remaining.isEmpty()) {
      int best = 0;
      double distance = Double.POSITIVE_INFINITY;
      for (int i = 0; i < remaining.size(); i++) {
        double d = geoDistance(current, remaining.get(i));
        if (d < distance) {
          best = i;
          distance = d;
        }
      }
      var next = remaining.remove(best);
      total += distance;
      legs.add(
          Map.of("from", current.get("id"), "to", next.get("id"), "distanceKm", round(distance)));
      current = next;
    }
    if (returnHome && !current.get("id").equals(depot.get("id"))) {
      double d = geoDistance(current, depot);
      total += d;
      legs.add(Map.of("from", current.get("id"), "to", depot.get("id"), "distanceKm", round(d)));
    }
    return Map.of(
        "totalDistanceKm",
        round(total),
        "legs",
        legs,
        "method",
        "HAVERSINE_NEAREST_NEIGHBOR",
        "roadNetworkOptimized",
        false);
  }
}
