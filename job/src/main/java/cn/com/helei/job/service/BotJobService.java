package cn.com.helei.job.service;


import cn.com.helei.common.dto.BotACJobResult;
import cn.com.helei.job.constants.JobStatus;
import cn.com.helei.job.core.AutoBotJobInvoker;
import cn.com.helei.common.dto.job.AutoBotJobParam;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Collection;
import java.util.List;

public interface BotJobService {

    /**
     * 注册Job的调用者
     *
     * @param jobKey  jobKey
     * @param invoker invoker
     */
    void registerJobInvoker(JobKey jobKey, AutoBotJobInvoker invoker);

    /**
     * 获取job的调用者
     *
     * @param jobKey jobKey
     * @return invoker
     */
    AutoBotJobInvoker getJobInvoker(JobKey jobKey);

    /**
     * 批量注册job
     *
     * @param botKey           botKey
     * @param autoBotJobParams autoBotJobParams
     * @return Result
     */
    List<BotACJobResult> startJobList(
            String botKey,
            String jobName,
            Collection<AutoBotJobParam> autoBotJobParams,
            AutoBotJobInvoker invoker
    );


    /**
     * 注册job，开始定时执行
     *
     * @param group           group
     * @param jobName         jobName
     * @param autoBotJobParam autoBotJobParam
     * @param invoker         invoker
     * @param refreshTrigger  refreshTrigger
     * @return BotACJobResult
     */
    BotACJobResult startJob(
            String group,
            String jobName,
            AutoBotJobParam autoBotJobParam,
            AutoBotJobInvoker invoker,
            boolean refreshTrigger
    );

    /**
     * 注册job，开始定时执行
     *
     * @param group           group
     * @param jobName         jobName
     * @param autoBotJobParam autoBotJobParam
     * @param invoker         invoker
     * @return BotACJobResult
     */
    BotACJobResult startJob(
            String group,
            String jobName,
            AutoBotJobParam autoBotJobParam,
            AutoBotJobInvoker invoker
    );

    void parseJob(JobKey jobKey) throws SchedulerException;

    /**
     * 暂停Job
     *
     * @param botKey  botKey
     * @param jobName jobName
     */
    void parseJob(String botKey, String jobName) throws SchedulerException;

    /**
     * 暂停Bot的全部任务
     *
     * @param botKey botKey
     * @throws SchedulerException SchedulerException
     */
    void parseGroupJob(String botKey) throws SchedulerException;


    /**
     * 重新启动Job
     *
     * @param botKey  botKey
     * @param jobName jobName
     * @throws SchedulerException SchedulerException
     */
    void resumeJob(String botKey, String jobName) throws SchedulerException;

    /**
     * 重新启动Job
     *
     * @param jobKey jobKey
     * @throws SchedulerException SchedulerException
     */
    void resumeJob(JobKey jobKey) throws SchedulerException;


    /**
     * 查询job状态
     *
     * @param botKey  botKey
     * @param jobName jobName
     * @return JobStatus
     */
    JobStatus queryJobStatus(String botKey, String jobName) throws SchedulerException;

    /**
     * 查询Job状态
     *
     * @param jobKey jobKey
     * @return JobStatus
     * @throws SchedulerException SchedulerException
     */
    JobStatus queryJobStatus(JobKey jobKey) throws SchedulerException;

}
