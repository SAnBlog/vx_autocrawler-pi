package cn.sanii.vx_server.service;

import cn.sanii.earth.Earth;
import cn.sanii.earth.example.ZhihuProcessor;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.pipeline.impl.ConsolePipeline;
import cn.sanii.vx_server.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: shouliang.wang
 * @create: 2020年6月16日 12:29:06
 * @description: 知乎热搜
 **/
@Slf4j
@Service
public class ZhihuHandler implements IHandler<List<Result>, String> {

    /**
     * 创建对象
     *
     * @return
     */
    public static ZhihuHandler build() {
        return new ZhihuHandler();
    }

    @Override
    public List<Result> handler(String s) {
        log.info("input :{}", s);
        List<Response> responses = Earth.me(new ZhihuProcessor())
                .addUrl("https://www.zhihu.com/api/v4/search/top_search/tabs/hot/items")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();

        return resultsByMap(responses);
    }
}
