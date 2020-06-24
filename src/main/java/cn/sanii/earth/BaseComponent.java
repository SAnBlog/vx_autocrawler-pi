package cn.sanii.earth;

import cn.sanii.earth.download.IDownloader;
import cn.sanii.earth.event.EventManager;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.EventEnum;
import cn.sanii.earth.pipeline.IPipeline;
import cn.sanii.earth.process.BaseBeforeProcessor;
import cn.sanii.earth.process.IAfterProcessor;
import cn.sanii.earth.process.IProcessor;
import cn.sanii.earth.schedule.IScheduler;
import cn.sanii.earth.schedule.SchedulerName;
import cn.sanii.earth.util.GuavaThreadPoolUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 公共组件抽象类
 */
public abstract class BaseComponent {

    /**
     * 下载器组件
     */
    protected IDownloader downloader;

    /**
     * 管道组件
     */
    protected List<IPipeline> pipelines = Lists.newArrayList();

    /**
     * 处理页面组件
     */
    protected IProcessor processor;

    /**
     * 任务正式抓取前操作组件
     */
    protected BaseBeforeProcessor beforeProcessor;

    /**
     * 任务抓取完成后操作组件
     */
    protected IAfterProcessor afterProcessor;

    /**
     * 种子列表
     */
    protected List<Request> startRequests = Lists.newArrayList();

    /**
     * cookie
     */
    protected Map<String, String> cookies = Maps.newHashMap();

    /**
     * 调度器组件
     */
    protected IScheduler scheduler;


    protected Optional<Predicate<Request>> requestPredicate = Optional.empty();
    protected Optional<Predicate<Response>> responsePredicate = Optional.empty();

    /**
     * 运行状态
     */
    protected boolean isRun;

    /**
     * 线程数
     */
    protected int threadCount = Runtime.getRuntime().availableProcessors();

    /**
     * 线程空闲等待持续时间
     */
    protected long waitTime;

    /**
     * 任务计数器
     */
    protected AtomicInteger statistics = new AtomicInteger(0);

    /**
     * 允许线程空闲最大等待时间，大于则终止任务。
     */
    protected long allowWaitTime = 30000L;

    /**
     * 线程池 全局共享
     */
    protected static ExecutorService EXECUTOR_SERVICE = GuavaThreadPoolUtils.getGuavaExecutor(Runtime.getRuntime().availableProcessors()+1);

    /**
     * 去重 默认不去重
     */
    private boolean unique=false;

    /**
     * 布隆过滤器
     * 100w种子,1%误报率
     */
    private static final BloomFilter<CharSequence> CHAR_SEQUENCE_BLOOM_FILTER = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000_0000, 0.001);

    protected void consumer(Request request) {
        requestPredicate.ifPresent(predicate -> {
            if (predicate.test(request)) {
                EventManager.consumer(EventEnum.GLOBAL_STARTED, request);
            }
        });
    }

    protected BaseComponent addEventRequest(Predicate<Request> predicate, Consumer<Request> consumer) {
        this.requestPredicate = Optional.ofNullable(predicate);
        EventManager.registerRequest(EventEnum.GLOBAL_STARTED, consumer);
        return this;
    }

    protected void consumer(Response response) {
        responsePredicate.ifPresent(predicate -> {
            if (predicate.test(response)) {
                EventManager.consumer(EventEnum.GLOBAL_END, response);
            }
        });
    }

    protected BaseComponent addEventResponse(Predicate<Response> predicate, Consumer<Response> consumer) {
        this.responsePredicate = Optional.ofNullable(predicate);
        EventManager.registerResponse(EventEnum.GLOBAL_END, consumer);
        return this;
    }

    protected BaseComponent thread(int theadCount) {
        this.threadCount = theadCount;
        return this;
    }

    protected BaseComponent setAllowWaitTime(long waitTime) {
        this.allowWaitTime = waitTime;
        return this;
    }

    protected BaseComponent addUrl(String... url) {
        addUrl(Charset.defaultCharset(), url);
        return this;
    }

    protected BaseComponent addUrl(Charset charset, String... url) {
        for (String s : url) {
            if (this.unique){
                if (!isContain(s)) {
                    CHAR_SEQUENCE_BLOOM_FILTER.put(s);
                    this.startRequests.add(new Request(s, this.processor.name(), charset));
                }
            }else {
                this.startRequests.add(new Request(s, this.processor.name(), charset));
            }
        }
        return this;
    }

    protected BaseComponent addUrlAll(List<Request> startRequests) {
        startRequests.forEach(request -> addUrl(request.getCharset(), request.getUrl()));
        return this;
    }

    protected BaseComponent setBeforeProcessor(BaseBeforeProcessor beforeProcessor) {
        this.beforeProcessor = beforeProcessor;
        return this;
    }

    protected BaseComponent setProcessor(IProcessor processor) {
        this.processor = processor;
        return this;
    }

    protected BaseComponent setDownloader(IDownloader downloader) {
        this.downloader = downloader;
        return this;
    }

    protected BaseComponent setScheduler(IScheduler scheduler) {
        if (scheduler instanceof SchedulerName) {
            ((SchedulerName) scheduler).setFieldName(this.processor.name());
        }
        this.scheduler = scheduler;
        return this;
    }

    protected BaseComponent setPipelines(List<IPipeline> pipelines) {
        this.pipelines.addAll(pipelines);
        return this;
    }

    protected BaseComponent setPipelines(IPipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    protected boolean isContain(String url) {
        return CHAR_SEQUENCE_BLOOM_FILTER.mightContain(url);
    }
}
