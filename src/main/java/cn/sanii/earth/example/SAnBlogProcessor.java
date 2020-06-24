package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.ConsolePipeline;
import cn.sanii.earth.process.IProcessor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 个人博客最新文章~哈哈
 */
public class SAnBlogProcessor implements IProcessor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        HashMap<String, String> map = Maps.newLinkedHashMap();
        document.getElementsByClass("item-title").forEach(element -> {
            String title = element.getElementsByTag("a").first().text();
            String href = element.getElementsByTag("a").first().attr("href");
            map.put(title, href);
        });
        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "sanblog";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new SAnBlogProcessor())
                .addUrl("https://app.sanii.cn/archives/page/1", "https://app.sanii.cn/archives/page/2")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .addEventResponse(response -> Objects.nonNull(response),response -> System.out.println("响应体：" + response) )
                .run();
        responses.forEach(res -> {
            System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.TEXT)));
        });
    }
}
