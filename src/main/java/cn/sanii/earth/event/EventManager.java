package cn.sanii.earth.event;

import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.EventEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/15 14:34
 * @Description: Consumer管理器
 */
public class EventManager {

    private static final Map<EventEnum, List<Consumer<Request>>> CONSUMERS_REQUEST = Maps.newHashMap();
    private static final Map<EventEnum, List<Consumer<Response>>> CONSUMERS_RESPONSE = Maps.newHashMap();

    /**
     * 注册请求事件
     * @param eventEnum 事件类型枚举
     * @param consumer consumer表达式
     */
    public static void registerRequest(EventEnum eventEnum, Consumer<Request> consumer) {
        List<Consumer<Request>> consumers = CONSUMERS_REQUEST.get(eventEnum);
        if (Objects.isNull(consumers)) {
            consumers = Lists.newArrayList();
        }
        consumers.add(consumer);
        CONSUMERS_REQUEST.put(eventEnum, consumers);
    }

    /**
     * 注册响应事件
     * @param eventEnum 事件类型枚举
     * @param consumer consumer表达式
     */
    public static void registerResponse(EventEnum eventEnum, Consumer<Response> consumer) {
        List<Consumer<Response>> consumers = CONSUMERS_RESPONSE.get(eventEnum);
        if (Objects.isNull(consumers)) {
            consumers = Lists.newArrayList();
        }
        consumers.add(consumer);
        CONSUMERS_RESPONSE.put(eventEnum, consumers);
    }

    /**
     * 消费请求
     * @param eventEnum 事件类型枚举
     * @param request 表达式处理对象
     */
    public static void consumer(EventEnum eventEnum, Request request) {
        Optional.ofNullable(CONSUMERS_REQUEST.get(eventEnum)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(request)));
    }
    /**
     * 消费响应
     * @param eventEnum 事件类型枚举
     * @param response 表达式处理对象
     */
    public static void consumer(EventEnum eventEnum, Response response) {
        Optional.ofNullable(CONSUMERS_RESPONSE.get(eventEnum)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(response)));
    }
}
