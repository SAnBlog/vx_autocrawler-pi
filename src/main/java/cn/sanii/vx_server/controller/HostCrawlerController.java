package cn.sanii.vx_server.controller;

import cn.sanii.vx_server.bean.Result;
import cn.sanii.vx_server.service.IHandler;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: shouliang.wang
 * @create: 2020-06-14 20:32
 * @description: 热门抓取数据接口
 **/
@RestController()
@Slf4j
public class HostCrawlerController {

    /**
     * guava 缓存
     */
    public final static Cache<String, String> CACHE = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大容量
            .expireAfterWrite(1, TimeUnit.HOURS) // 设置缓存时效
            .concurrencyLevel(10) // 设置并发级别为10
            .recordStats() // 开启缓存统计
            .build();

    @Resource
    private List<IHandler> handlers;

    @GetMapping("/get")
    public String get() {
        try {
            return CACHE.get("data", () -> {
                log.info("cache invalid");
                JSONObject data = new JSONObject();
                JSONObject resultJson = new JSONObject();
                for (IHandler handler : handlers) {
                    try {
                        List<Result> resultList = (List<Result>) handler.handler("");
                        if (CollectionUtils.isNotEmpty(resultList)) {
                            data.put(resultList.get(0).getName(), resultList);
                        }
                    } catch (Exception e) {
                        log.error("handler error:{}", e);
                    }
                }
                resultJson.put("hotwords", data.keySet());
                resultJson.put("data", data);
                return resultJson.toJSONString();
            });
        } catch (ExecutionException e) {
            log.info("get cache error:{}", e.getMessage());
            return "error";
        } finally {
            log.info("get finish");
        }
    }
}
