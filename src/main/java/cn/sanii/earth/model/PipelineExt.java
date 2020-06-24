package cn.sanii.earth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: shouliang.wang
 * @create: 2020-06-15 19:11
 * @description: 管道实体扩展
 **/
@Data
@Builder
public class PipelineExt {
    private List<ByteBean> byteBeans;
}
