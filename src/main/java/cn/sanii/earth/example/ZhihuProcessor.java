package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.ConsolePipeline;
import cn.sanii.earth.process.IProcessor;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 知乎热搜榜
 */
public class ZhihuProcessor implements IProcessor {

    @Override
    public void process(Response response) {
        HashMap<String, String> map = Maps.newHashMap();
        JSONObject data = JSONObject.parseObject(response.getHtml());
        JSONArray array = data.getJSONArray("data");
        array.forEach(o -> {
            JSONObject obj = JSONObject.parseObject(o.toString());
            String title = obj.getString("query_display");
            String description = obj.getString("query_description");
            map.put(title, description);
        });
        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "zhihu";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new ZhihuProcessor())
                .addUrl("https://www.zhihu.com/api/v4/search/top_search/tabs/hot/items")
                .setPipelines(new ConsolePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        responses.forEach(res -> {
            System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.TEXT)));
        });
    }
}
