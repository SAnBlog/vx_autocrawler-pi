package cn.sanii.vx_server.service;

import cn.sanii.earth.Earth;
import cn.sanii.earth.example.IqshwProcessor;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.vx_server.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * @author: shouliang.wang
 * @create: 2020-05-14 14:12
 * @description: 爱Q
 **/
@Slf4j
@Service
public class IqshwHandler implements IHandler<List<Result>, String> {


    /**
     * 创建对象
     *
     * @return
     */
    public static IqshwHandler build() {
        return new IqshwHandler();
    }

    @Override
    public List<Result> handler(String s) {
        log.info("input :{}", s);
        List<Response> responses = Earth.me(new IqshwProcessor())
                .addUrl(Charset.forName("GB2312"), "https://wap.iqshw.com")
                .setPipelines(new SaveFilePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        return resultsByPipelineExt(responses);
    }

}
