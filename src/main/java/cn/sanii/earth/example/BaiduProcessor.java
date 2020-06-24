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
 * 百度热搜榜
 */
public class BaiduProcessor implements IProcessor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        HashMap<String, String> map = Maps.newLinkedHashMap();
        document.getElementsByClass("hot_new_list").first()
                .getElementsByTag("li").forEach(element -> {
            String title = element.getElementsByTag("a").first().text();
            map.put(title, "");
        });
        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "baidu";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new BaiduProcessor())
                .addUrl("http://www.ijiandao.com/hot/complex/baidu")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        responses.forEach(res -> {
            System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.TEXT)));
        });
    }
}
