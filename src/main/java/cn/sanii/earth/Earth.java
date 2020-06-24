package cn.sanii.earth;

import cn.sanii.earth.event.EventBusCenter;
import cn.sanii.earth.event.EventConfig;
import cn.sanii.earth.process.IProcessor;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:23
 * @Description: 地球
 */
public class Earth {

    private Earth() {
    }


    /**
     * 同步执行
     *
     * @param processor
     * @return
     */
    public static Wandering me(IProcessor processor) {
        return new Wandering(processor);
    }

    /**
     * 异步执行
     *
     * @param component
     * @return
     */
    public static void asyn(BaseComponent component) {
        EventConfig.getEventListener().forEach(EventBusCenter::register);
        EventBusCenter.postAsync(component);
    }

}
