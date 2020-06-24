package cn.sanii.vx_server.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shouliang.wang
 * @create: 2020-06-14 21:15
 * @description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result {

    private String title;
    private String content;
    private String img;

    //抓取执行器name
    private String name;
}
