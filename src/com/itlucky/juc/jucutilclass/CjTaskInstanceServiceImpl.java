//package com.atta.customerjourney.task.service.impl;
//
//import com.atta.customerjourney.common.config.CustomerJourneyConfig;
//import com.atta.customerjourney.customer.dal.dao.CjCustomersDAO;
//import com.atta.customerjourney.customer.dal.dataobject.CjCustomersDO;
//import com.atta.customerjourney.task.convert.TaskConvert;
//import com.atta.customerjourney.task.dal.dao.CjTaskActionInstanceDAO;
//import com.atta.customerjourney.task.dal.dao.CjTaskDAO;
//import com.atta.customerjourney.task.dal.dao.CjTaskInstanceDAO;
//import com.atta.customerjourney.task.dal.dao.CjTaskLogDAO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskActionInstanceDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskInstanceDO;
//import com.atta.customerjourney.task.dal.dataobject.CjTaskLogDO;
//import com.atta.customerjourney.task.dto.CjActionDTO;
//import com.atta.customerjourney.task.service.CjTaskInstanceService;
//import com.atta.customerjourney.task.strategy.ActionExecuteStrategy;
//import com.atta.customerjourney.task.strategy.HandleMapping;
//import com.atta.customerjourney.utils.AttaIdUtils;
//import com.atta.infra.customerjourney.api.enums.*;
//import com.atta.infra.strategy.execute.api.client.ExecuteClient;
//import com.atta.infra.user.selector.execute.request.ExecuteRequest;
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
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
//    private ExecuteClient executeClient;
//
//    @Autowired
//    private HandleMapping handleMapping;
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
//                List<CjTaskInstanceDO> taskInstanceDOS = taskInstanceDAO.queryTaskInstancesByTaskId(task.getTaskId(),today,tomorrow);
//                if (CollectionUtils.isNotEmpty(taskInstanceDOS)){
//                    log.info("任务：{}，{},今天已经创建过实例，跳过",task.getTaskId(),task.getTaskName());
//                    return;
//                }
//
//                //5，创建任务实例并计数
//                createTaskInstance(task);
//                count.addAndGet(1);
//            }catch (IllegalArgumentException e){
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
//        //1，组装任务实例对象
//        CjTaskInstanceDO taskInstanceDO = TaskConvert.convertInstance(task,null);
//        taskInstanceDO.setTaskInstanceId(getInstanceId());
//        taskInstanceDO.setBatchId(getBatch(task.getUserGroupId()));
//
//        transactionTemplate.execute(transactionStatus -> {
//
//            //2，插入任务实例记录
//            Assert.isTrue(null != taskInstanceDAO.insert(taskInstanceDO),"insert task instance fail");
//
//            //3，如果当前任务是发布状态，更新任务状态为进行中
//            //todo 新建job更新任务状态为已完成
//            if (CJTaskStatus.PUBLISHED.name().equals(task.getStatus())){
//                task.setStatus(CJTaskStatus.PROCESSING.name());
//                task.setModifiedExplanation("任务到达执行时间，修改任务状态为进行中");
//                CjTaskLogDO taskLogDO = TaskConvert.convertTaskLog(task);
//                Assert.isTrue(null != taskLogDAO.insert(taskLogDO),"insert task log fail");
//                Assert.isTrue(1 == taskDAO.update(task),"update task fail");
//            }
//            return true;
//        });
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
//     * 执行任务实例
//     * @param taskInstance
//     */
//    private void executeInstance(CjTaskInstanceDO taskInstance) {
//
//        int total = 0;
//        taskInstance.setExecutedBeginTime(new Date());
//        //1,判断任务状态是否正常
//        if (taskNotInProcess(taskInstance.getTaskId())){
//            log.info("任务:{} 已暂停,任务实例：{},取消执行",taskInstance.getTaskId(),taskInstance.getTaskInstanceId());
//            taskInstance.setStatus(CJTaskInstanceStatus.CANCEL.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"cancel task : update task instance fail");
//            return;
//        }
//
//        //2，判断是否命中策略
//        Set<CjActionDTO> actions = executeStrategy(taskInstance);
//        if (CollectionUtils.isEmpty(actions)){
//            log.info("任务实例：{}，未命中策略，跳过执行",taskInstance.getTaskInstanceId());
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"miss strategy : update task instance fail");
//            return;
//        }
//
//        //3，判断用户群组是否有数据
//        int totalCustomer = cjCustomersDAO.countByBatchId(taskInstance.getBatchId());
//        if (0 == totalCustomer){
//            log.info("任务实例：{} 关联批次号：{},没有查询到用户群组数据",taskInstance.getTaskInstanceId(),taskInstance.getBatchId());
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"not exist customers : update task instance fail");
//            return;
//        }
//
//        //4,执行任务实例，更新任务状态
//        total = actions.stream().mapToInt(action -> executeInstanceOneAction(totalCustomer, taskInstance, action)).sum();
//        taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//        taskInstance.setUserTotal(total);
//        Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"execute action success : update task instance fail");
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
//        Long count = actionInstanceDAO.countByByTaskInstanceId(instance.getTaskInstanceId());
//        if (null != count && 0 != count){
//            log.info("当前任务实例已经生成过动作实例，跳过执行");
//            return 0;
//        }
//
//        //2，根据customer数量判断需要多少线程
//        int number = total%customerJourneyConfig.getBatchSize() == 0 ? total/customerJourneyConfig.getBatchSize() : total/customerJourneyConfig.getBatchSize() + 1;
//        CountDownLatch latch = new CountDownLatch(number);
//
//        //todo 先插入再执行，提供补偿机制
//        try {
//            long minId = 0L;
//            List<CjCustomersDO> cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//            while (CollectionUtils.isNotEmpty(cjCustomersDOS)){
//
//                executorService.submit(new ExecuteAction(cjCustomersDOS,instance,action,latch));
//                minId = cjCustomersDOS.get(cjCustomersDOS.size() -1).getId() + 1;
//                cjCustomersDOS = cjCustomersDAO.queryByBatchId(minId,instance.getBatchId(),customerJourneyConfig.getBatchSize());
//
//            }
//            //等待 所有线程 都 执行完毕
//            latch.await();
//        } catch (Exception e) {
//            log.error("execute task instance error, e:{}",e.getMessage());
//        }
//
//        //3，同步用户结束后提交执行动作
//        insetAndExecuteAction(null,instance,action);
//
//        return total;
//    }
//
//    class ExecuteAction implements Runnable{
//
//        private final List<CjCustomersDO> customers;
//
//        private final CjTaskInstanceDO taskInstance;
//
//        private final CjActionDTO action;
//
//        private final CountDownLatch latch;
//
//        public ExecuteAction(List<CjCustomersDO> customers,CjTaskInstanceDO taskInstance,CjActionDTO action,CountDownLatch latch) {
//            this.customers = customers;
//            this.taskInstance = taskInstance;
//            this.action = action;
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
//                insetAndExecuteAction(customers,taskInstance,action);
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
//     * 执行单次动作插入和调用
//     * @param customers
//     * @param taskInstance
//     * @param action
//     */
//    private void insetAndExecuteAction(List<CjCustomersDO> customers,CjTaskInstanceDO taskInstance,CjActionDTO action){
//        //1，根据动作类型获取策略类
//        ActionExecuteStrategy strategy = handleMapping.get(action.getActionType());
//
//        //2，组装动作执行参数
//        ExecuteRequest request = strategy.convertRequest(customers,taskInstance,action);
//
//        //3，组装动作记录对象
//        CjTaskActionInstanceDO actionInstanceDO = TaskConvert.convertAction(taskInstance);
//        actionInstanceDO.setTaskActionInstanceId(getActionInstanceId());
//        actionInstanceDO.setActionType(action.getActionType().name());
//        actionInstanceDO.setActionId(action.getTemplateId());
//        actionInstanceDO.setActionParam(request);
//
//        //4，插入动作执行记录
//        actionInstanceDO = actionInstanceDAO.insert(actionInstanceDO);
//        Assert.isTrue(null != actionInstanceDO,"insert action instance fail");
//
//        //5，执行动作前判断任务是否取消
//        if (taskNotInProcess(taskInstance.getTaskId())){
//            log.info("任务:{} 已暂停,动作实例：{},不再执行",taskInstance.getTaskId(),actionInstanceDO.getActionId());
//            return;
//        }
//
//        //6，执行动作
//        boolean handle = strategy.handle(request,actionInstanceDO.getTaskActionInstanceId());
//        if (!handle){
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.FAIL.name());
//        }else {
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.SUCCESS.name());
//        }
//
//        //7，更新动作执行结果
//        Assert.isTrue(null != actionInstanceDAO.updateByIdSelective(actionInstanceDO),"update action instance fail");
//
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
//        CjActionDTO actionDTO = new CjActionDTO();
//        actionDTO.setActionType(CJTaskActionType.EDM);
//        actionDTO.setTemplateId(customerJourneyConfig.getTemplateId());
//        actionDTO.setSubject(customerJourneyConfig.getSubject());
//        actionDTO.setReplyEmail(customerJourneyConfig.getReplyEmail());
//        actionDTO.setSenderAddress(customerJourneyConfig.getSenderAddress());
//
//        actions.add(actionDTO);
//        return actions;
//
//
//        //todo 调用策略接口获取经营动作结果
//        /*ExecuteReq executeReq = new ExecuteReq();
//        executeReq.setBizId(instance.getTaskInstanceId());
//        executeReq.setProjectCode(taskDO.getRuleCode());
//        Map<String, Object> context = new HashMap<>();
//        executeReq.setContext(context);
//        FacadeResponse<ExecuteRes> response = executeClient.execute(executeReq);
//        if (!response.isSucceeded() || null == response.getValue() || CollectionUtils.isEmpty(response.getValue().getActions())){
//            return Collections.emptySet();
//        }
//        response.getValue().getActions().forEach(action -> actions.add(JsonUtils.parseObject(action,CjActionDTO.class)));
//        return actions;*/
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
//                isWeekDay = week == taskDO.getExecuteWeekDay();
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
