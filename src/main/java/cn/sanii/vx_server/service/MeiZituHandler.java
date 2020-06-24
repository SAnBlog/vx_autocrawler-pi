package cn.sanii.vx_server.service;

import cn.sanii.earth.Earth;
import cn.sanii.earth.example.Mzitu2Processor;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.vx_server.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: shouliang.wang
 * @create: 2020-05-14 14:12
 * @description: 妹子图
 **/
@Slf4j
//@Service
public class MeiZituHandler implements IHandler<List<Result>, String> {


    /**
     * 创建对象
     *
     * @return
     */
    public static MeiZituHandler build() {
        return new MeiZituHandler();
    }

    @Override
    public List<Result> handler(String s) {
        log.info("input :{}", s);
        List<Response> responses = Earth.me(new Mzitu2Processor())
                .addUrl("https://www.mzitu.com")
                .setPipelines(new SaveFilePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        return resultsByPipelineExt(responses);
    }


}
