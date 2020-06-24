package cn.sanii.vx_server.service;

import cn.sanii.earth.Earth;
import cn.sanii.earth.example.WeiboProcessor;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.pipeline.impl.ConsolePipeline;
import cn.sanii.earth.util.OkHttpUtil;
import cn.sanii.vx_server.bean.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author: shouliang.wang
 * @create: 2020-05-14 14:12
 * @description: 微博
 **/
@Slf4j
@Service
public class WeiboHandler implements IHandler<List<Result>, String> {

    /**
     * 创建对象
     *
     * @return
     */
    public static WeiboHandler build() {
        return new WeiboHandler();
    }

    @Override
    public List<Result> handler(String s) {
        log.info("input :{}", s);
        List<Response> responses = Earth.me(new WeiboProcessor())
                .addUrl("https://s.weibo.com/top/summary")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();

        return resultsByMap(responses);
    }


    private static OkHttpClient ok = OkHttpUtil.getDefault();

    private String conver(String path) {
        FormBody body = new FormBody.Builder()
                .add("longurl", path)
                .build();

        Request build = new Request.Builder()
                .post(body)
                .url("https://www.charfun.com/api/shorturl")
                .build();
        try {
            return parse(ok.newCall(build).execute().body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String parse(String string) {
        System.out.println(string);
        return JSONObject.parseObject(string).get("shorturl").toString();
    }
}
