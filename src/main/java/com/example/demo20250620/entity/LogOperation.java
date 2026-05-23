package com.example.demo20250620.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_operation")
public class LogOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作时间
     */
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    /**
     * 操作用户ID
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 操作用户名
     */
    @Column(name = "operator_name", length = 100)
    private String operatorName;

    /**
     * 操作者角色（1-管理员，2-操作员）
     */
    @Column(name = "operator_role")
    private Integer operatorRole;

    /**
     * 操作类型（如：设备借用、设备归还、设备转借、设备维修等）
     */
    @Column(name = "operation_type", length = 100, nullable = false)
    private String operationType;

    /**
     * 操作模块（设备管理、借用管理、维修管理、转借管理等）
     */
    @Column(name = "operation_module", length = 100)
    private String operationModule;

    /**
     * 操作描述
     */
    @Column(name = "operation_description", length = 500)
    private String operationDescription;

    /**
     * 操作结果（SUCCESS-成功，FAIL-失败）
     */
    @Column(name = "operation_result", length = 20)
    private String operationResult;

    /**
     * 目标类型（设备、借用记录、维修记录、转借记录等）
     */
    @Column(name = "target_type", length = 50)
    private String targetType;

    /**
     * 目标ID
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 目标名称（如设备编号、设备型号等）
     */
    @Column(name = "target_name", length = 200)
    private String targetName;

    /**
     * 详细信息（JSON格式，存储操作前后的数据变化）
     */
    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    /**
     * 操作者IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 用户代理（浏览器信息）
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * 异常信息（操作失败时记录）
     */
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    // 操作类型常量
    public static final String TYPE_DEVICE_CREATE = "设备新增";
    public static final String TYPE_DEVICE_CHECK = "设备安检";
    public static final String TYPE_DEVICE_RETURN = "设备归还";
    public static final String TYPE_DEVICE_REPAIR = "设备维修";
    public static final String TYPE_DEVICE_UNSHELVE = "设备上架";
    public static final String TYPE_DEVICE_BORROW = "设备借用";
    public static final String TYPE_DEVICE_TRANSFER = "设备转借";
    public static final String TYPE_REPAIR_APPLY = "维修申请";
    public static final String TYPE_REPAIR_CONFIRM = "维修确认";
    public static final String TYPE_REPAIR_FINISH = "维修完成";
    public static final String TYPE_TRANSFER_APPLY = "转借申请";
    public static final String TYPE_TRANSFER_USER_APPROVE = "转借同意";
    public static final String TYPE_TRANSFER_ADMIN_APPROVE = "转借批准";
    public static final String TYPE_BORROW_APPROVE = "借用批准";
    public static final String TYPE_BORROW_REJECT = "借用拒绝";
    public static final String TYPE_USER_LOGIN = "用户登录";
    public static final String TYPE_USER_LOGOUT = "用户登出";
    public static final String TYPE_USER_CREATE = "用户新增";
    public static final String TYPE_USER_DELETE = "用户删除";
    public static final String TYPE_USER_UPDATE = "用户修改";
    public static final String TYPE_USER_CHANGE_PASSWORD = "修改密码";
    public static final String TYPE_USER_RESET_PASSWORD = "重置密码";

    // 操作模块常量
    public static final String MODULE_DEVICE = "设备管理";
    public static final String MODULE_BORROW = "借用管理";
    public static final String MODULE_REPAIR = "维修管理";
    public static final String MODULE_TRANSFER = "转借管理";
    public static final String MODULE_USER = "用户管理";

    // 操作结果常量
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAIL = "FAIL";

    // 目标类型常量
    public static final String TARGET_DEVICE = "设备";
    public static final String TARGET_BORROW_RECORD = "借用记录";
    public static final String TARGET_REPAIR_RECORD = "维修记录";
    public static final String TARGET_TRANSFER_RECORD = "转借记录";
    public static final String TARGET_USER = "用户";

    // 构造函数
    public LogOperation() {
        this.operationTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getOperatorRole() {
        return operatorRole;
    }

    public void setOperatorRole(Integer operatorRole) {
        this.operatorRole = operatorRole;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationModule() {
        return operationModule;
    }

    public void setOperationModule(String operationModule) {
        this.operationModule = operationModule;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}