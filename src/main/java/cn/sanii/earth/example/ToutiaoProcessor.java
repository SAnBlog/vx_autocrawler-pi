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
 * 头条热搜榜
 */
public class ToutiaoProcessor implements IProcessor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        HashMap<String, String> map = Maps.newLinkedHashMap();
        document.getElementsByClass("J-share-a").forEach(element -> {
            String title = element.text();
            String href = element.attr("href");
            map.put(title, href);
        });
        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "toutiao";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new ToutiaoProcessor())
                .addUrl("https://www.resotoutiao.com/guona/list_1_1.html", "https://www.resotoutiao.com/guona/list_1_2.html")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        responses.forEach(res -> {
            System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.TEXT)));
        });
    }
}
