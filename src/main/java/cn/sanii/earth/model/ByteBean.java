package cn.sanii.earth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shouliang.wang
 * @create: 2020-06-15 20:03
 * @description: 字节类型处理扩展
 **/
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ByteBean {

    /**
     * 存储的文件名
     */
    private String name;

    /**
     *别名
     */
    private String alias;

    /**
     资源地址
     */
    private String url;

    /**
     * 扩展
     */
    private String json;
}
