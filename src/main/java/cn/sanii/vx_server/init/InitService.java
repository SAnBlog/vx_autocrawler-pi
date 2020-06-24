package cn.sanii.vx_server.init;

import cn.sanii.vx_server.bean.Result;
import cn.sanii.vx_server.service.IHandler;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: Administrator
 * @create: 2020/6/22
 * Description:
 */
@Component
public class InitService {

    /**
     * guava 缓存
     */
    public final static Cache<String, Map<String, List<String>>> INIT_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大容量
            .expireAfterWrite(1, TimeUnit.HOURS) // 设置缓存时效
            .concurrencyLevel(10) // 设置并发级别为10
            .recordStats() // 开启缓存统计
            .build();

    private long s1 = 6000;
    private long s2 = 10000;

    private static String STYLE="7m";

    @Resource
    private List<IHandler> handlers;


    @PostConstruct
    public void init() {
        sleepTime();

        print("service start");
        while (true) {
            try {
                Map<String, List<String>> cache = INIT_CACHE.get("cache", () -> {
                    print("loading cache");
                    HashMap<String, List<String>> map = Maps.newHashMap();
                    for (IHandler handler : handlers) {
                        try {
                            List<Result> resultList = (List<Result>) handler.handler("");
                            if (CollectionUtils.isNotEmpty(resultList)) {
                                List<String> titles = resultList.stream().map(Result::getTitle).collect(Collectors.toList());
                                map.put(resultList.get(0).getName(), titles);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return map;
                });

                AtomicInteger count = new AtomicInteger(0);

                //打印
                cache.forEach((k, v) -> {
                    print("本轮播数据： "+k);
                    for (int i = 0; i < v.size(); i++) {
                        int andIncrement = count.getAndIncrement();
                        if (andIncrement == 10) {
                            count.set(0);
                            print("------下一页------");
                            sleep(6000);
                        }
                        print(v.get(i));
                    }
                    count.set(0);
                    print("本轮播数据： "+k);
                    sleep(10000);
                });


            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * java -jar -Ds1=xxx -Ds2=xxx class.jar
     */
    private void sleepTime() {
        String property1 = System.getProperty("s1");
        String property2 = System.getProperty("s2");
        if (StringUtils.isNotBlank(property1)){
            s1=Long.parseLong(property1);
        }
        if (StringUtils.isNotBlank(property2)){
            s2=Long.parseLong(property2);
        }
        print("s1:"+s1+",s2:"+s2);
    }

    private static void sleep(int i2) {
        //休眠
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> RAMDOM = Arrays.asList(31, 32, 33, 34, 35, 36, 37);
    private static ArrayBlockingQueue QUEUE = new ArrayBlockingQueue<Integer>(RAMDOM.size());

    private static void print(String str) {
        String style = System.getProperty("style");
        if (StringUtils.isNotBlank(style)){
            STYLE = style;
        }
        print(str,STYLE);
    }

    private static void print(String str,String tyle) {
        if (QUEUE.size() == 0) {
            Collections.shuffle(RAMDOM);
            QUEUE.addAll(RAMDOM);
        }
        System.out.println("\033[" + QUEUE.poll() + "m " + str + " \033["+tyle);
    }
}
