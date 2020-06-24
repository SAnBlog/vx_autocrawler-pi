# vx_autocrawler

#### 介绍
微信小程序抓取爬虫项目后台，只需要实现一个接口即可实现一个抓取。无需管理调度，简单强大。

#### 微信小程序
https://gitee.com/SAnBlog/vx_autohot

#### 安装教程

1.  jdk8
2.  LomBok

#### 功能
抓取调度部分使用爬虫框架,基于springboot
Earth：https://gitee.com/SAnBlog/Earth

已实现抓取热搜榜如下
百度，知乎，头条，微博，SAnBlog，妹子图

#### 示例

一个妹子图抓取的完整代码，如果想爬整站请参考Earth：https://gitee.com/SAnBlog/Earth 

```

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

}

```

