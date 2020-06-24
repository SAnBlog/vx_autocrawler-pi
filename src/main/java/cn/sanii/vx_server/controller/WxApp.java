//package cn.sanii.vx_server.controller;
//
//import cn.sanii.vx_server.service.WeiboHandler;
//import com.mxixm.fastboot.weixin.annotation.*;
//import com.mxixm.fastboot.weixin.module.event.WxEvent;
//import com.mxixm.fastboot.weixin.module.message.WxMessage;
//import com.mxixm.fastboot.weixin.module.message.WxMessageBody;
//import com.mxixm.fastboot.weixin.module.user.WxUser;
//import com.mxixm.fastboot.weixin.module.web.WxRequest;
//import com.mxixm.fastboot.weixin.module.web.session.WxSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//
//    /**
//     * 微信公众号服务器,已废弃
//     */
//@WxApplication
//@WxController
//@Slf4j
//public class WxApp {
//
//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(WxApp.class, args);
//    }
//
//    /**
//     * 定义微信菜单
//     */
//    @WxButton(group = WxButton.Group.LEFT, main = true, name = "个人项目")
//    public void left() {
//    }
//
//
//    /**
//     * 定义微信菜单，并接受事件
//     */
//    @WxButton(type = WxButton.Type.CLICK,
//            group = WxButton.Group.LEFT,
//            order = WxButton.Order.FIRST,
//            name = "有趣项目")
//    public String leftFirst(WxRequest wxRequest, WxUser wxUser) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("博客")
//                .append("\n")
//                .append("https://sanii.cn")
//                .append("\n")
//                .append("\n")
//                .append("减压音乐")
//                .append("\n")
//                .append("https://music.sanii.cn")
//                .append("\n")
//                .append("\n")
//                .append("小黑屋")
//                .append("\n")
//                .append("http://adarkroom.sanii.cn")
//                .append("\n")
//                .append("\n")
//                .append("树莓派")
//                .append("\n")
//                .append("https://app.sanii.cn")
//                .append("\n")
//                .append("\n");
//
//        return builder.toString();
//    }
//
//    /**
//     * 定义微信菜单，并接受事件
//     */
//    @WxButton(type = WxButton.Type.VIEW,
//            group = WxButton.Group.LEFT,
//            order = WxButton.Order.SECOND,
//            url = "https://sanii.cn",
//            name = "SAn Blog")
//    @WxAsyncMessage
//    public WxMessage link() {
//        return WxMessage.newsBuilder().addItem("SAn Blog", "- 一条闲鱼的技术博客", "https://app.sanii.cn/upload/2020/05/yjtp-6c11536bb279474984ea32583f0598fc-thumbnail.png", "https://sanii.cn").build();
//    }
//
//    /**
//     * 接受微信事件
//     *
//     * @param wxRequest
//     * @param wxUser
//     */
//    @WxEventMapping(type = WxEvent.Type.UNSUBSCRIBE)
//    public void unsubscribe(WxRequest wxRequest, WxUser wxUser) {
//        log.info("退订了公众号 :{}", wxUser.getNickName());
//    }
//
//    /**
//     * 接受用户文本消息，异步返回文本消息
//     *
//     * @param content
//     * @return the result
//     */
//    @WxMessageMapping(type = WxMessage.Type.TEXT)
//    @WxAsyncMessage
//    public String text(String content) {
//        return content + "!";
//    }
//
//
//    /**
//     * 微博热搜榜
//     *
//     * @param content
//     * @return the result
//     */
//    @WxMessageMapping(type = WxMessage.Type.TEXT, wildcard = "微博*")
//    @WxAsyncMessage
//    public String weibo(String content) {
//        return WeiboHandler.build().handler(content);
//    }
//
//
//    /**
//     * 接受用户文本消息，同步返回图文消息
//     *
//     * @param content
//     * @return the result
//     */
//    @WxMessageMapping(type = WxMessage.Type.TEXT, wildcard = "媳妇*")
//    public WxMessage message(WxSession wxSession, String content) {
//        wxSession.setAttribute("last", content);
//        return WxMessage.newsBuilder()
//                .addItem(WxMessageBody.News.Item.builder().title(content).description("我最耐,最喜欢的一头山猪❤")
//                        .picUrl("https://app.sanii.cn/upload/2020/05/%E5%AA%B3%E5%A6%871-9375d49da0a24c489d471bdcccdb7eab-thumbnail.jpg")
//                        .url("http://alan.sanii.cn/").build())
//                .build();
//    }
//
//    /**
//     * 定义微信菜单
//     */
//    @WxButton(group = WxButton.Group.RIGHT,
//            type = WxButton.Type.MINI_PROGRAM,
//            main = true,
//            appId = "wx2cf4feebeccc2c5a",
//            pagePath = "pages/index/index",
//            url = "https://app.sanii.cn",
//            name = "小程序")
//    public WxMessage right() {
//        return WxMessage.miniProgramBuilder()
//                .appId("wx2cf4feebeccc2c5a")
//                .pagePath("pages/index/index")
//                .build();
//    }
//
//
//    @WxMessageMapping(type = WxMessage.Type.IMAGE)
//    public String image(WxRequest request){
//        log.info("picUrl :{}", request.getBody().getPicUrl());
//        return "get,表情包+1♡";
//    }
//}
