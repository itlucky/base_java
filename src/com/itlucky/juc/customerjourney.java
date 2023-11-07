//package com.atta.customerjourney.task.service.impl;
//
//import com.atta.customerjourney.common.config.CustomerJourneyConfig;
//import com.atta.customerjourney.customer.dal.dao.CjCustomersDAO;
//import com.atta.customerjourney.customer.dal.dataobject.CjCustomersDO;
//import com.atta.customerjourney.task.convert.TaskConvert;
//import com.atta.customerjourney.task.dal.dao.*;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskActionInstanceDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskInstanceDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskLogDO;
//import com.atta.customerjourney.task.dto.CjActionDTO;
//import com.atta.customerjourney.task.service.CjTaskInstanceService;
//import com.atta.customerjourney.task.strategy.ActionExecuteStrategy;
//import com.atta.customerjourney.task.strategy.HandleMapping;
//import com.atta.customerjourney.utils.AttaIdUtils;
//import com.atta.infra.boss.api.client.MessageCenterClient;
//import com.atta.infra.boss.api.model.messagecenter.MessageChannel;
//import com.atta.infra.boss.api.model.messagecenter.MessageDomain;
//import com.atta.infra.boss.api.model.messagecenter.SendMessageRequest;
//import com.atta.infra.common.api.base.FacadeResponse;
//import com.atta.infra.customerjourney.api.enums.*;
//import com.atta.infra.strategy.execute.api.client.ExecuteClient;
//import com.atta.infra.strategy.execute.api.request.ExecuteReq;
//import com.atta.infra.strategy.execute.api.response.ExecuteRes;
//import com.atta.infra.user.selector.execute.request.ExecuteRequest;
//import com.atta.infra.utils.JsonUtils;
//import com.google.common.collect.Sets;
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;
//import org.springframework.util.Assert;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.temporal.TemporalAdjusters;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static com.atta.infra.customerjourney.api.enums.CJTaskExecutePeriod.valueOf;
//
///**
// * @auther yang.bai
// * @date 2022/9/24 18:09
// **/
//@Service
//@Slf4j
//public class CjTaskInstanceServiceImpl implements CjTaskInstanceService {
//
//    private static final int RETRY_ACTION_SIZE = 5;
//
//    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("cj-task-instance-thread-%d").build();
//
//    ExecutorService executorService = new ThreadPoolExecutor(4,
//            8,
//            10L, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(30), namedThreadFactory);
//
//    @Autowired
//    private CustomerJourneyConfig customerJourneyConfig;
//
//    @Autowired
//    private CjTaskDAO taskDAO;
//
//    @Autowired
//    private CjTaskLogDAO taskLogDAO;
//
//    @Autowired
//    private CjTaskInstanceDAO taskInstanceDAO;
//
//    @Autowired
//    private CjCustomersDAO cjCustomersDAO;
//
//    @Autowired
//    private CjTaskActionInstanceDAO actionInstanceDAO;
//
//    @Autowired
//    private TransactionTemplate transactionTemplate;
//
//    @Autowired
//    private HandleMapping handleMapping;
//
//    @Autowired
//    private MessageCenterClient messageCenterClient;
//
//    @Autowired
//    private ExecuteClient executeClient;
//
//    @Autowired
//    private CjTaskInstanceDetailDAO instanceDetailDAO;
//
//    /**
//     * 根据当日需要执行的任务生成任务实例
//     * @return 生成任务实例数量
//     */
//    @Override
//    public Integer createTaskInstance() {
//        //1，取当前时间，今天凌晨、明天凌晨
//        Date nowTime = new Date();
//        LocalDate todayLocalDate = LocalDate.now();
//        LocalDate tomorrowLocalDate = todayLocalDate.plusDays(1);
//        ZoneId zoneId = ZoneId.systemDefault();
//        Date today = Date.from(todayLocalDate.atStartOfDay().atZone(zoneId).toInstant());
//        Date tomorrow = Date.from(tomorrowLocalDate.atStartOfDay().atZone(zoneId).toInstant());
//
//        //2，查询今天需要执行的任务
//        AtomicInteger count = new AtomicInteger();
//        //todo finish_time，start_time创建索引
//        List<CjTaskDO> tasks = taskDAO.queryInProgressTasks(nowTime, CJTaskTriggerMethod.PERIODIC.name());
//        if (CollectionUtils.isEmpty(tasks)){
//            log.info("没有进行中的经营任务。。。。");
//            return count.get();
//        }
//
//
//        tasks.forEach(task -> {
//            try {
//                //3，判断是否到达任务执行时间
//                if (!inProcess(task,nowTime)){
//                    return;
//                }
//
//                //4，判断当天是否创建过任务实例
//                List<CjTaskInstanceDO> taskInstanceDOS = taskInstanceDAO.queryTaskInstancesByTaskId(task.getTaskId(),null,today,tomorrow);
//                if (CollectionUtils.isNotEmpty(taskInstanceDOS)){
//                    log.info("任务：{}，{},今天已经创建过实例，跳过",task.getTaskId(),task.getTaskName());
//                    return;
//                }
//
//                //5，创建任务实例并计数
//                createTaskInstance(task);
//                count.addAndGet(1);
//            }catch (Exception e){
//                log.error("insert task instance fail");
//            }
//        });
//        return count.get();
//    }
//
//    /**
//     * 生成任务实例，更新任务状态
//     * @param task
//     */
//    private void createTaskInstance(CjTaskDO task){
//
//        //todo 唯一索引是否有必要添加 taskId + batchId
//        //1，组装任务实例对象
//        CjTaskInstanceDO taskInstanceDO = TaskConvert.convertInstance(task,null);
//        taskInstanceDO.setTaskInstanceId(getInstanceId());
//        taskInstanceDO.setBatchId(getBatch(task.getUserGroupId()));
//
//        //2，插入任务实例记录
//        taskInstanceDAO.insert(taskInstanceDO);
//    }
//
//    /**
//     * 执行任务实例
//     * @return
//     */
//    @Override
//    public Integer executeTaskInstances() {
//        //1,查询新建状态的任务实例
//        AtomicInteger count = new AtomicInteger();
//        List<CjTaskInstanceDO> taskInstances = taskInstanceDAO.queryTaskInstancesByStatus(CJTaskInstanceStatus.NEW.name());
//        if (CollectionUtils.isEmpty(taskInstances)){
//            log.info("没有待执行的经营任务实例。。。。");
//            return count.get();
//        }
//        taskInstances.forEach(this::executeInstance);
//        return taskInstances.size();
//    }
//
//    /**
//     * 经营动作补偿JOB
//     * 补偿范围：动作同步请求调用失败的场景
//     * 用户同步失败，但是动作同步成功的场景，不补偿
//     */
//    @Override
//    public void taskActionRetry() {
//        //1，查询动作同步失败的action
//        int skip = 0;
//        List<CjTaskActionInstanceDO> actions = actionInstanceDAO.queryUnSuccessAction(RETRY_ACTION_SIZE,0,customerJourneyConfig.getMaxExecuteTimes());
//        while (CollectionUtils.isNotEmpty(actions)){
//            //2，查询动作同步失败的action关联的同步用户action,如果存在失败，则先补偿用户同步action
//            actions.forEach(action -> {
//                List<CjTaskActionInstanceDO> actionList = actionInstanceDAO.queryActionByTaskInstanceId(action.getTaskInstanceId(),RETRY_ACTION_SIZE,0,customerJourneyConfig.getMaxExecuteTimes());
//                if (CollectionUtils.isEmpty(actionList)){
//                    //没有关联同步用户失败的调用，直接执行经营动作
//                    executeAction(action.getTaskActionInstanceId());
//                }else {
//                    //存在同步用户失败的调用，先同步用户再执行动作
//                    AtomicReference<Integer> successTotal = new AtomicReference<>(0);
//                    actionList.forEach(actionDo -> {
//                        if (executeAction(actionDo.getTaskActionInstanceId())){
//                            successTotal.set(successTotal.get() + 1);
//                        }
//                    });
//                    if (successTotal.get() == actionList.size()){
//                        executeAction(action.getTaskActionInstanceId());
//                    }
//                }
//            });
//            skip = skip + RETRY_ACTION_SIZE;
//            actions = actionInstanceDAO.queryUnSuccessAction(RETRY_ACTION_SIZE,skip,customerJourneyConfig.getMaxExecuteTimes());
//        }
//    }
//
//    @Override
//    public Integer changeTaskStatus() {
//        //1，查询进行中的任务
//        int page = 0;
//        AtomicInteger total = new AtomicInteger();
//        Date nowTime = new Date();
//        List<CjTaskDO> tasks = taskDAO.queryNotInFinishedTasks(page,100);
//        while (CollectionUtils.isNotEmpty(tasks)){
//            tasks.forEach(task -> {
//                CJTaskStatus taskStatus = CJTaskStatus.valueOf(task.getStatus());
//                switch (taskStatus){
//                    case UNPUBLISHED:case UNDER_REVIEW:case COMPILING:case REJECTED:case PROCESSING:
//                        //未发布、审核中、编译中、已拒绝、进行中，当前时间超过任务结束时间，更新任务状态为FINISHED
//                        if (task.getFinishTime().before(nowTime)){
//                            task.setStatus(CJTaskStatus.FINISHED.name());
//                        }
//                        break;
//                    case PUBLISHED:
//                        if (task.getStartTime().before(nowTime) && nowTime.before(task.getFinishTime())){
//                            //大于开始时间，小于结束时间，设置状态为PROCESSING
//                            task.setStatus(CJTaskStatus.PROCESSING.name());
//                        }else if (task.getFinishTime().before(nowTime)){
//                            //超过结束时间，设置状态为FINISHED
//                            task.setStatus(CJTaskStatus.FINISHED.name());
//                        }
//                        break;
//                    default:
//                        return;
//                }
//
//                //状态没有变化不更新数据
//                if (taskStatus.name().equals(task.getStatus())){
//                    return;
//                }
//
//                boolean isOk = Boolean.TRUE.equals(transactionTemplate.execute(transactionStatus -> {
//                    try {
//                        task.setModifiedExplanation("根据当前时间变更工单状态为：" + task.getStatus());
//                        CjTaskLogDO taskLogDO = TaskConvert.convertTaskLog(task);
//                        Assert.isTrue(null != taskLogDAO.insert(taskLogDO), "insert task log fail");
//                        Assert.isTrue(1 == taskDAO.update(task), "update task status fail");
//                    } catch (Exception e) {
//                        return false;
//                    }
//                    return true;
//                }));
//                total.set(isOk?total.get() + 1:0);
//
//            });
//            page = 100 + page;
//            tasks = taskDAO.queryNotInFinishedTasks(page,100);
//        }
//        return total.get();
//    }
//
//    /**
//     * 执行任务实例
//     * @param taskInstance
//     */
//    private void executeInstance(CjTaskInstanceDO taskInstance) {
//
//        int total = 0;
//        taskInstance.setExecutedBeginTime(new Date());
//        //1,判断任务状态是否正常
//        if (taskNotInProcess(taskInstance.getTaskId())){
//            //发送任务通知失败消息
//            sendMessageToCreator(taskInstance.getTaskId(),CancelType.TASK_CANCEL);
//
//            String executeMessage = "任务:"+taskInstance.getTaskId()+" 已暂停,任务实例："+taskInstance.getTaskInstanceId()+",取消执行";
//            log.info(executeMessage);
//            taskInstance.setStatus(CJTaskInstanceStatus.CANCEL.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            taskInstance.setExecuteMessage(executeMessage);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"cancel task : update task instance fail");
//            return;
//        }
//
//        //2，判断是否命中策略
//        Set<CjActionDTO> actions = executeStrategy(taskInstance);
//        if (CollectionUtils.isEmpty(actions)){
//            String executeMessage = "任务实例："+taskInstance.getTaskInstanceId()+"，未命中策略，跳过执行";
//            log.info(executeMessage);
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            taskInstance.setExecuteMessage(executeMessage);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"miss strategy : update task instance fail");
//            return;
//        }
//
//        //3，判断用户群组是否有数据
//        int totalCustomer = cjCustomersDAO.countByBatchId(taskInstance.getBatchId());
//        if (0 == totalCustomer){
//            //发送任务通知失败消息
//            sendMessageToCreator(taskInstance.getTaskId(),CancelType.NOT_FIND_CUSTOMER);
//
//            String executeMessage = "任务实例："+taskInstance.getTaskInstanceId()+" 关联批次号："+taskInstance.getBatchId()+",没有查询到用户群组数据";
//            log.info(executeMessage);
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            taskInstance.setExecuteMessage(executeMessage);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"not exist customers : update task instance fail");
//            return;
//        }
//
//        //4,执行任务实例，更新任务状态
//        total = actions.stream().mapToInt(action -> executeInstanceOneAction(totalCustomer, taskInstance, action)).sum();
//        taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//        taskInstance.setUserTotal(total);
//        taskInstance.setExecuteMessage("执行成功");
//        taskInstance.setExecutedEndTime(new Date());
//        Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"execute action success : update task instance fail");
//    }
//
//    /**
//     * 任务执行失败时发消息给任务创建人
//     * @param taskId 任务ID
//     */
//    private void sendMessageToCreator(String taskId,CancelType type){
//        CjTaskDO taskDO = taskDAO.selectByTaskId(taskId);
//        if (null == taskDO){
//            log.info("根据任务ID：{},未查询到任务",taskId);
//            return;
//        }
//
//        StringBuilder message = new StringBuilder("经营平台任务：" + taskDO.getTaskName());
//                switch (type){
//            case TASK_CANCEL:
//                message.append("，已取消执行");
//                break;
//            case NOT_FIND_CUSTOMER:
//                message.append("，执行失败，原因：未获取到人群包数据");
//                break;
//            default:
//                message.append("，执行失败");
//        }
//
//        try {
//            SendMessageRequest request = new SendMessageRequest();
//            request.setBizId(taskId + LocalDateTime.now());
//            request.setBossUserId(taskDO.getCreatedBy());
//            request.setDomain(MessageDomain.boss);
//            request.setChannels(Sets.newHashSet(MessageChannel.ding_ding));
//            request.setMessageContent(message.toString());
//            request.setSenderId("system");
//            request.setSendTime(new Date());
//            messageCenterClient.sengMessage(request);
//        }catch (Exception e){
//            log.error("发送任务执行失败消息通知失败");
//        }
//    }
//
//    public enum CancelType{
//        TASK_CANCEL,
//        NOT_FIND_CUSTOMER
//    }
//
//    /**
//     * 保存经营动作详情
//     * @param instance 任务实例
//     * @param action 动作
//     * @return
//     */
//    private Integer saveInstanceDetail(CjTaskInstanceDO instance,CjActionDTO action){
//        List<String> actionIds = new ArrayList<>();
//        long minId = 0L;
//        int nowCount = 0;
//
//        List<CjCustomersDO> cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//        while (CollectionUtils.isNotEmpty(cjCustomersDOS)){
//
//            actionIds.add(saveActionInstance(cjCustomersDOS,instance,action,false));
//            minId = cjCustomersDOS.get(cjCustomersDOS.size() -1).getId() + 1;
//            cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//            nowCount = nowCount + 1;
//        }
//
//
//        return 0;
//    }
//
//
//    /**
//     * 执行经营动作
//     * @param total 客户总量
//     * @param instance 任务实例
//     * @param action 经营动作
//     * @return
//     */
//    private Integer executeInstanceOneAction(Integer total,CjTaskInstanceDO instance,CjActionDTO action){
//
//        //1，判断经营动作是否已经存在，存在就退出
//        Long count = actionInstanceDAO.countByByTaskInstanceId(instance.getTaskInstanceId(),action.getActionId());
//        if (null != count && 0 != count){
//            log.info("当前任务实例已经生成过动作实例，跳过执行");
//            return 0;
//        }
//
//        //2,插入经营动作实例
//        List<String> actionIds = new ArrayList<>();
//        long minId = 0L;
//        int nowCount = 0;
//        List<CjCustomersDO> cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//        while (CollectionUtils.isNotEmpty(cjCustomersDOS)){
//            actionIds.add(saveActionInstance(cjCustomersDOS,instance,action,false));
//            minId = cjCustomersDOS.get(cjCustomersDOS.size() -1).getId() + 1;
//            cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//            nowCount = nowCount + 1;
//        }
//
//        String lastActionId = "";
//        if (CollectionUtils.isNotEmpty(actionIds)){
//            lastActionId = saveActionInstance(null,instance,action,true);
//        }
//
//        //3,经营动作实例
//        try {
//            //3.1 先同步用户
//            if (CollectionUtils.isEmpty(actionIds) || StringUtils.isEmpty(lastActionId)){
//                return 0;
//            }
//
//            CountDownLatch latch = new CountDownLatch(actionIds.size());
//            actionIds.forEach(actionId -> executorService.submit(new ExecuteAction(actionId,latch)));
//
//            //3.2 等待用户同步结束，执行动作执行指令
//            latch.await();
//            executeAction(lastActionId);
//
//        } catch (Exception e) {
//            log.error("execute task instance error, e:{}",e.getMessage());
//        }
//
//        return total;
//    }
//
//    class ExecuteAction implements Runnable{
//
//        private final String actionInstanceId;
//
//        private final CountDownLatch latch;
//
//        public ExecuteAction(String actionInstanceId,CountDownLatch latch) {
//            this.actionInstanceId = actionInstanceId;
//            this.latch = latch;
//        }
//
//        /**
//         * 1，保存经营动作
//         * 2，执行经营动作
//         */
//        @Override
//        public void run() {
//            try {
//                executeAction(actionInstanceId);
//            } catch (Exception e) {
//                log.error("execute action error, e:{}",e.getMessage());
//            } finally {
//                latch.countDown();
//            }
//        }
//
//    }
//
//    /**
//     * 执行经营动作
//     * @param actionInstanceId 动作实例ID
//     * @return 动作是否执行成功
//     */
//    private boolean executeAction(String actionInstanceId){
//        //1，查询动作实例详情
//        CjTaskActionInstanceDO actionInstanceDO = actionInstanceDAO.queryByActionInstanceId(actionInstanceId);
//        if (null == actionInstanceDO){
//            return true;
//        }
//
//        //2，执行动作前判断任务是否取消
//        if (taskNotInProcess(actionInstanceDO.getTaskId())){
//            log.info("任务:{} 已暂停,动作实例：{},不再执行",actionInstanceDO.getTaskId(),actionInstanceDO.getTaskActionInstanceId());
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.CANCEL.name());
//            actionInstanceDO.setExecuteMessage("任务" + actionInstanceDO.getTaskId() + "已取消");
//            Assert.isTrue(null != actionInstanceDAO.updateByIdSelective(actionInstanceDO),"update action instance fail");
//            return true;
//        }
//
//        //3，执行动作
//        boolean result = false;
//        ActionExecuteStrategy strategy = handleMapping.get(CJTaskActionType.valueOf(actionInstanceDO.getActionType()));
//        FacadeResponse<Void> response;
//        try {
//
//            actionInstanceDO.setExecuteTimes(actionInstanceDO.getExecuteTimes() + 1);
//
//            response = strategy.handle(actionInstanceDO.getActionParam(),actionInstanceDO.getTaskActionInstanceId());
//
//            actionInstanceDO.setExecuteMessage(StringUtils.isEmpty(response.getResponseMsg())?"动作执行成功":response.getResponseMsg());
//            if (response.isSucceeded()){
//                actionInstanceDO.setStatus(CJTaskActionInstanceStatus.SUCCESS.name());
//                result = true;
//            }else {
//                log.error("execute action :{} fail",actionInstanceId);
//                actionInstanceDO.setStatus(CJTaskActionInstanceStatus.FAIL.name());
//            }
//
//        }catch (Exception e){
//            log.error("execute action :{} error,{}",actionInstanceId,e.getMessage());
//            actionInstanceDO.setExecuteMessage(e.getMessage());
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.FAIL.name());
//        }
//
//        //4，更新动作执行结果
//        Assert.isTrue(null != actionInstanceDAO.updateByIdSelective(actionInstanceDO),"update action instance fail");
//        return result;
//    }
//
//    /**
//     * 保存经营动作实例
//     * @param customers
//     * @param taskInstance
//     * @param action
//     * @return
//     */
//    private String saveActionInstance(List<CjCustomersDO> customers,CjTaskInstanceDO taskInstance,CjActionDTO action,boolean syncAction){
//        //1，根据动作类型获取策略类
//        ActionExecuteStrategy strategy = handleMapping.get(action.getActionType());
//
//        //2，组装动作执行参数
//        ExecuteRequest request = strategy.convertRequest(customers,taskInstance,action,syncAction);
//
//        //3，组装动作记录对象
//        CjTaskActionInstanceDO actionInstanceDO = TaskConvert.convertAction(taskInstance);
//        actionInstanceDO.setTaskActionInstanceId(getActionInstanceId());
//        actionInstanceDO.setActionType(action.getActionType().name());
//        actionInstanceDO.setActionId(action.getTemplateId());
//        actionInstanceDO.setActionParam(request);
//        actionInstanceDO.setSyncAction(syncAction);
//        actionInstanceDO.setExecuteTimes(0);
//
//        //4，插入动作执行记录
//        actionInstanceDO = actionInstanceDAO.insert(actionInstanceDO);
//        Assert.isTrue(null != actionInstanceDO,"insert action instance fail");
//
//        return actionInstanceDO.getTaskActionInstanceId();
//    }
//
//    //判断任务是否取消
//    private boolean taskNotInProcess(String taskId){
//        CjTaskDO taskDO = taskDAO.selectByTaskId(taskId);
//        if (null == taskDO){
//            return true;
//        }
//        return !taskDO.getStatus().equals(CJTaskStatus.PUBLISHED.name()) && !taskDO.getStatus().equals(CJTaskStatus.PROCESSING.name());
//    }
//
//    /**
//     * 执行策略
//     * @param instance
//     * @return
//     */
//    private Set<CjActionDTO> executeStrategy(CjTaskInstanceDO instance){
//        CjTaskDO taskDO = taskDAO.selectByEntityId(instance.getTaskId());
//        if (null == taskDO){
//            return Collections.emptySet();
//        }
//        Set<CjActionDTO> actions = new HashSet<>();
//
//        ExecuteReq executeReq = new ExecuteReq();
//        executeReq.setBizId(instance.getTaskInstanceId());
//        executeReq.setProjectCode(taskDO.getRuleCode());
//        Map<String, Object> context = new HashMap<>();
//        executeReq.setContext(context);
//        FacadeResponse<ExecuteRes> response = executeClient.execute(executeReq);
//        if (!response.isSucceeded() || null == response.getValue() || CollectionUtils.isEmpty(response.getValue().getActions())){
//            log.info("not hit strategy {}",response.getResponseMsg());
//            return Collections.emptySet();
//        }
//        for (String action : response.getValue().getActions()){
//            CjActionDTO actionDTO = JsonUtils.parseObject(action,CjActionDTO.class);
//            if (StringUtils.isEmpty(actionDTO.getTemplateId()) || StringUtils.isEmpty(actionDTO.getSubject()) || StringUtils.isEmpty(actionDTO.getSenderAddress())){
//                log.warn("strategy is valid templateId or subject or senderAddress is null");
//                continue;
//            }
//            //todo 默认使用EDM，后续修改为从策略返回的json中解析获得
//            actionDTO.setActionType(CJTaskActionType.EDM);
//            actionDTO.setActionId(actionDTO.getTemplateId() + "_" + actionDTO.getSubject());
//            actions.add(actionDTO);
//        }
//        return actions;
//    }
//
//    //获取任务实例ID
//    private String getInstanceId() {
//        return AttaIdUtils.getByProdCode(AttaIdUtils.ProdCode.CJ_TASK_INSTANCE);
//    }
//
//    //获取动作实例ID
//    private String getActionInstanceId() {
//        return AttaIdUtils.getByProdCode(AttaIdUtils.ProdCode.CJ_TASK_ACTION_INSTANCE);
//    }
//
//    //获取批次ID
//    //todo 适配一天内多次执行的任务
//    //todo 增加时间格式
//    //todo 当前仅支持天级别的数据更新，需要支持到小时或分钟级别
//    private String getBatch(String userGroupId){
//        LocalDate localDate = LocalDate.now();
//        return userGroupId + "_" + localDate;
//    }
//
//    /**
//     * 判断任务当前时间是否需要执行
//     * @param taskDO
//     * @param nowTime
//     * @return
//     */
//    private boolean inProcess(CjTaskDO taskDO,Date nowTime){
//        CJTaskExecutePeriod period = valueOf(taskDO.getExecutePeriod());
//        boolean isWeekDay = true;
//        boolean isMonthDay = true;
//        Date executeTime = new Date();
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(executeTime.toInstant(), ZoneId.systemDefault());
//        executeTime = DateUtils.setHours(executeTime,taskDO.getExecuteHour());
//        executeTime = DateUtils.setMinutes(executeTime,taskDO.getExecuteMinute());
//        executeTime = DateUtils.setSeconds(executeTime,0);
//        executeTime = DateUtils.setMilliseconds(executeTime,0);
//        switch (period){
//            case PER_DAY:
//                break;
//            case PER_WEEK:
//                int week = localDateTime.getDayOfWeek().getValue();
//                isWeekDay = (week == taskDO.getExecuteWeekDay());
//                break;
//            case PER_MONTH:
//                LocalDateTime lastDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
//                int dayOfMonth = Math.min(taskDO.getExecuteDay(),lastDay.getDayOfMonth());
//                executeTime = DateUtils.setDays(executeTime,dayOfMonth);
//                isMonthDay = DateUtils.isSameDay(executeTime,nowTime);
//                break;
//            default:
//                log.error("任务执行周期设置错误");
//                return false;
//        }
//        long begin = DateUtils.addMinutes(executeTime,-customerJourneyConfig.getTriggerDistance()).getTime();
//        long end = DateUtils.addMinutes(executeTime,customerJourneyConfig.getTriggerDistance()).getTime();
//        return nowTime.getTime() < end && nowTime.getTime() > begin && isWeekDay && isMonthDay;
//    }
//
//}
