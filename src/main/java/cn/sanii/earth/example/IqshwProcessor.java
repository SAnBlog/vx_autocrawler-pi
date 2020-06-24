package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.ByteBean;
import cn.sanii.earth.model.PipelineExt;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 微博热搜榜
 */
public class IqshwProcessor implements IProcessor {

    private static final String PREFIX = "https://iqshw.com";

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        List<ByteBean> resultList = Lists.newArrayList();
        document.getElementsByClass("placeholder").forEach(element -> {
            Element a = element.getElementsByTag("a").first();
            String imgUls = a.getElementsByClass("plc-image").first().getElementsByTag("img").first().attr("src");
            String text = a.getElementsByClass("plc-con").first().getElementsByTag("p").first().text();
            ByteBean byteBean = ByteBean.builder().name(UUID.randomUUID().toString().replace("-", "")).url(imgUls).alias(text).build();
            resultList.add(byteBean);
        });

        response.getResultField().getFields().put(FieldEnum.BYTE, PipelineExt.builder().byteBeans(resultList).build());
    }

    @Override
    public String name() {
        return "iqshw";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new IqshwProcessor())
                .addUrl(Charset.forName("GB2312"),"https://wap.iqshw.com/")
                .setPipelines(new SaveFilePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        responses.forEach(res -> {
            System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.BYTE)));
            ;
        });
    }
}
