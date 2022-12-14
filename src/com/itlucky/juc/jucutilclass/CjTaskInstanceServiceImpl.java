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
//     * ???????????????????????????????????????????????????
//     * @return ????????????????????????
//     */
//    @Override
//    public Integer createTaskInstance() {
//        //1????????????????????????????????????????????????
//        Date nowTime = new Date();
//        LocalDate todayLocalDate = LocalDate.now();
//        LocalDate tomorrowLocalDate = todayLocalDate.plusDays(1);
//        ZoneId zoneId = ZoneId.systemDefault();
//        Date today = Date.from(todayLocalDate.atStartOfDay().atZone(zoneId).toInstant());
//        Date tomorrow = Date.from(tomorrowLocalDate.atStartOfDay().atZone(zoneId).toInstant());
//
//        //2????????????????????????????????????
//        AtomicInteger count = new AtomicInteger();
//        List<CjTaskDO> tasks = taskDAO.queryInProgressTasks(nowTime, CJTaskTriggerMethod.PERIODIC.name());
//        if (CollectionUtils.isEmpty(tasks)){
//            log.info("??????????????????????????????????????????");
//            return count.get();
//        }
//
//
//        tasks.forEach(task -> {
//            try {
//                //3???????????????????????????????????????
//                if (!inProcess(task,nowTime)){
//                    return;
//                }
//
//                //4??????????????????????????????????????????
//                List<CjTaskInstanceDO> taskInstanceDOS = taskInstanceDAO.queryTaskInstancesByTaskId(task.getTaskId(),today,tomorrow);
//                if (CollectionUtils.isNotEmpty(taskInstanceDOS)){
//                    log.info("?????????{}???{},????????????????????????????????????",task.getTaskId(),task.getTaskName());
//                    return;
//                }
//
//                //5??????????????????????????????
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
//     * ???????????????????????????????????????
//     * @param task
//     */
//    private void createTaskInstance(CjTaskDO task){
//
//        //1???????????????????????????
//        CjTaskInstanceDO taskInstanceDO = TaskConvert.convertInstance(task,null);
//        taskInstanceDO.setTaskInstanceId(getInstanceId());
//        taskInstanceDO.setBatchId(getBatch(task.getUserGroupId()));
//
//        transactionTemplate.execute(transactionStatus -> {
//
//            //2???????????????????????????
//            Assert.isTrue(null != taskInstanceDAO.insert(taskInstanceDO),"insert task instance fail");
//
//            //3?????????????????????????????????????????????????????????????????????
//            //todo ??????job??????????????????????????????
//            if (CJTaskStatus.PUBLISHED.name().equals(task.getStatus())){
//                task.setStatus(CJTaskStatus.PROCESSING.name());
//                task.setModifiedExplanation("?????????????????????????????????????????????????????????");
//                CjTaskLogDO taskLogDO = TaskConvert.convertTaskLog(task);
//                Assert.isTrue(null != taskLogDAO.insert(taskLogDO),"insert task log fail");
//                Assert.isTrue(1 == taskDAO.update(task),"update task fail");
//            }
//            return true;
//        });
//    }
//
//    /**
//     * ??????????????????
//     * @return
//     */
//    @Override
//    public Integer executeTaskInstances() {
//        //1,?????????????????????????????????
//        AtomicInteger count = new AtomicInteger();
//        List<CjTaskInstanceDO> taskInstances = taskInstanceDAO.queryTaskInstancesByStatus(CJTaskInstanceStatus.NEW.name());
//        if (CollectionUtils.isEmpty(taskInstances)){
//            log.info("????????????????????????????????????????????????");
//            return count.get();
//        }
//        taskInstances.forEach(this::executeInstance);
//        return taskInstances.size();
//    }
//
//    /**
//     * ??????????????????
//     * @param taskInstance
//     */
//    private void executeInstance(CjTaskInstanceDO taskInstance) {
//
//        int total = 0;
//        taskInstance.setExecutedBeginTime(new Date());
//        //1,??????????????????????????????
//        if (taskNotInProcess(taskInstance.getTaskId())){
//            log.info("??????:{} ?????????,???????????????{},????????????",taskInstance.getTaskId(),taskInstance.getTaskInstanceId());
//            taskInstance.setStatus(CJTaskInstanceStatus.CANCEL.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"cancel task : update task instance fail");
//            return;
//        }
//
//        //2???????????????????????????
//        Set<CjActionDTO> actions = executeStrategy(taskInstance);
//        if (CollectionUtils.isEmpty(actions)){
//            log.info("???????????????{}?????????????????????????????????",taskInstance.getTaskInstanceId());
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"miss strategy : update task instance fail");
//            return;
//        }
//
//        //3????????????????????????????????????
//        int totalCustomer = cjCustomersDAO.countByBatchId(taskInstance.getBatchId());
//        if (0 == totalCustomer){
//            log.info("???????????????{} ??????????????????{},?????????????????????????????????",taskInstance.getTaskInstanceId(),taskInstance.getBatchId());
//            taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//            taskInstance.setExecutedEndTime(new Date());
//            taskInstance.setUserTotal(total);
//            Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"not exist customers : update task instance fail");
//            return;
//        }
//
//        //4,???????????????????????????????????????
//        total = actions.stream().mapToInt(action -> executeInstanceOneAction(totalCustomer, taskInstance, action)).sum();
//        taskInstance.setStatus(CJTaskInstanceStatus.SUCCESS.name());
//        taskInstance.setUserTotal(total);
//        Assert.isTrue(1 == taskInstanceDAO.update(taskInstance),"execute action success : update task instance fail");
//    }
//
//
//    /**
//     * ??????????????????
//     * @param total ????????????
//     * @param instance ????????????
//     * @param action ????????????
//     * @return
//     */
//    private Integer executeInstanceOneAction(Integer total,CjTaskInstanceDO instance,CjActionDTO action){
//
//        //1?????????????????????????????????????????????????????????
//        Long count = actionInstanceDAO.countByByTaskInstanceId(instance.getTaskInstanceId());
//        if (null != count && 0 != count){
//            log.info("????????????????????????????????????????????????????????????");
//            return 0;
//        }
//
//        //2?????????customer??????????????????????????????
//        int number = total%customerJourneyConfig.getBatchSize() == 0 ? total/customerJourneyConfig.getBatchSize() : total/customerJourneyConfig.getBatchSize() + 1;
//        CountDownLatch latch = new CountDownLatch(number);
//
//        //todo ???????????????????????????????????????
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
//            //?????? ???????????? ??? ????????????
//            latch.await();
//        } catch (Exception e) {
//            log.error("execute task instance error, e:{}",e.getMessage());
//        }
//
//        //3??????????????????????????????????????????
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
//         * 1?????????????????????
//         * 2?????????????????????
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
//     * ?????????????????????????????????
//     * @param customers
//     * @param taskInstance
//     * @param action
//     */
//    private void insetAndExecuteAction(List<CjCustomersDO> customers,CjTaskInstanceDO taskInstance,CjActionDTO action){
//        //1????????????????????????????????????
//        ActionExecuteStrategy strategy = handleMapping.get(action.getActionType());
//
//        //2???????????????????????????
//        ExecuteRequest request = strategy.convertRequest(customers,taskInstance,action);
//
//        //3???????????????????????????
//        CjTaskActionInstanceDO actionInstanceDO = TaskConvert.convertAction(taskInstance);
//        actionInstanceDO.setTaskActionInstanceId(getActionInstanceId());
//        actionInstanceDO.setActionType(action.getActionType().name());
//        actionInstanceDO.setActionId(action.getTemplateId());
//        actionInstanceDO.setActionParam(request);
//
//        //4???????????????????????????
//        actionInstanceDO = actionInstanceDAO.insert(actionInstanceDO);
//        Assert.isTrue(null != actionInstanceDO,"insert action instance fail");
//
//        //5??????????????????????????????????????????
//        if (taskNotInProcess(taskInstance.getTaskId())){
//            log.info("??????:{} ?????????,???????????????{},????????????",taskInstance.getTaskId(),actionInstanceDO.getActionId());
//            return;
//        }
//
//        //6???????????????
//        boolean handle = strategy.handle(request,actionInstanceDO.getTaskActionInstanceId());
//        if (!handle){
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.FAIL.name());
//        }else {
//            actionInstanceDO.setStatus(CJTaskActionInstanceStatus.SUCCESS.name());
//        }
//
//        //7???????????????????????????
//        Assert.isTrue(null != actionInstanceDAO.updateByIdSelective(actionInstanceDO),"update action instance fail");
//
//    }
//
//    //????????????????????????
//    private boolean taskNotInProcess(String taskId){
//        CjTaskDO taskDO = taskDAO.selectByTaskId(taskId);
//        if (null == taskDO){
//            return true;
//        }
//        return !taskDO.getStatus().equals(CJTaskStatus.PUBLISHED.name()) && !taskDO.getStatus().equals(CJTaskStatus.PROCESSING.name());
//    }
//
//    /**
//     * ????????????
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
//        //todo ??????????????????????????????????????????
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
//    //??????????????????ID
//    private String getInstanceId() {
//        return AttaIdUtils.getByProdCode(AttaIdUtils.ProdCode.CJ_TASK_INSTANCE);
//    }
//
//    //??????????????????ID
//    private String getActionInstanceId() {
//        return AttaIdUtils.getByProdCode(AttaIdUtils.ProdCode.CJ_TASK_ACTION_INSTANCE);
//    }
//
//    //????????????ID
//    private String getBatch(String userGroupId){
//        LocalDate localDate = LocalDate.now();
//        return userGroupId + "_" + localDate;
//    }
//
//    /**
//     * ??????????????????????????????????????????
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
//                log.error("??????????????????????????????");
//                return false;
//        }
//        long begin = DateUtils.addMinutes(executeTime,-customerJourneyConfig.getTriggerDistance()).getTime();
//        long end = DateUtils.addMinutes(executeTime,customerJourneyConfig.getTriggerDistance()).getTime();
//        return nowTime.getTime() < end && nowTime.getTime() > begin && isWeekDay && isMonthDay;
//    }
//
//}
