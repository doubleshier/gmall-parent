package com.atguigu.gmall.common.config.threadpool;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {


    @Autowired
    AppThreadPoolProperties threadPoolProperties;

    @Value("${spring.application.name}")
    String applicationName;

    @Bean
    public ThreadPoolExecutor coreExecutor(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadPoolProperties.getCore(),
                threadPoolProperties.getMax(),
                threadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(threadPoolProperties.getQueueSize()), //队列的大小由项目最终能占的最大内存决定
                new ThreadFactory() { //负责给线程池创建线程
                    int i = 0; //记录线程自增id
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName+"[core-thread-"+ i++ +"]");
                        return thread;
                    }
                },
                //生产环境用 CallerRuns，保证就算线程池满了，
                // 不能提交的任务，由当前线程自己以同步的方式执行
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        //每个线程的线程名都是默认的。
        return executor;
    }


}
