package cn.sanii.vx_server.service;

import cn.sanii.earth.model.PipelineExt;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.vx_server.Utils.ImageUtils;
import cn.sanii.vx_server.bean.Result;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author: shouliang.wang
 * @create: 2020-05-14 14:07
 * @description: 处理器
 **/
public interface IHandler<T, P> {

    /**
     * 处理方法
     *
     * @param p
     * @return
     */
    T handler(P p);

    /**
     * PipelineExt类型封装统一处理返回值
     * @param responses
     * @return
     */
    default List<Result> resultsByPipelineExt(List<Response> responses) {
        List<Result> list = Lists.newArrayList();
        responses.forEach(res -> {
            PipelineExt pipelineExt = (PipelineExt) res.getResultField().getFields().get(FieldEnum.BYTE);
            if (CollectionUtils.isNotEmpty(pipelineExt.getByteBeans())) {
                pipelineExt.getByteBeans().forEach(byteBean -> {
                    String url = ImageUtils.urlConver(res.getName(), byteBean.getName(), byteBean.getUrl());
                    list.add(Result.builder().name(res.getName()).title(byteBean.getAlias()).img(url).build());
                });
            }
        });
        return list;
    }
    /**
     * Map类型封装统一处理返回值
     * @param responses
     * @return
     */
    default List<Result> resultsByMap(List<Response> responses) {
        List<Result> list = Lists.newArrayList();
        responses.forEach(res -> {
            Map<String, String> map = (Map<String, String>) res.getResultField().getFields().get(FieldEnum.TEXT);
            map.forEach((k, v) -> {
                list.add(Result.builder().name(res.getName()).title(k).content(v).build());
            });
        });
        return list;
    }
}
