package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.ByteBean;
import cn.sanii.earth.model.PipelineExt;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-21 21:09:25
 * @Description: https://www.mzitu.com/xinggan/
 */
public class Mzitu2Processor implements IProcessor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        /**
         * 图片地址提取规则
         */
        List<ByteBean> resultList = Lists.newArrayList();
        document.getElementsByTag("img").forEach(element -> {
            String img = element.attr("data-original");
            if (StringUtils.isNotEmpty(img)) {
                String alt = element.attr("alt");
                ByteBean byteBean = ByteBean.builder().name(UUID.randomUUID().toString().replace("-", "")).url(img).alias(alt).build();
                resultList.add(byteBean);
            }
        });


        response.getResultField().getFields().put(FieldEnum.BYTE, PipelineExt.builder().byteBeans(resultList).build());
    }

    @Override
    public String name() {
        return "mzitu";
    }

    public static void main(String[] args) {
        Earth.me(new Mzitu2Processor())
                .addUrl("https://www.mzitu.com/mm/")
                .setPipelines(new SaveFilePipeline())
                .addEventRequest(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .start();
    }
}
