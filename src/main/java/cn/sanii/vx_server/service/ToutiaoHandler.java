package cn.sanii.vx_server.service;

import cn.sanii.earth.Earth;
import cn.sanii.earth.example.ToutiaoProcessor;
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
 * @description: 头条热搜
 **/
@Slf4j
@Service
public class ToutiaoHandler implements IHandler<List<Result>, String> {

    /**
     * 创建对象
     *
     * @return
     */
    public static ToutiaoHandler build() {
        return new ToutiaoHandler();
    }

    @Override
    public List<Result> handler(String s) {
        log.info("input :{}", s);
        List<Response> responses = Earth.me(new ToutiaoProcessor())
                .addUrl("https://www.resotoutiao.com/guona/list_1_1.html", "https://www.resotoutiao.com/guona/list_1_2.html")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();

        return resultsByMap(responses);
    }
}
