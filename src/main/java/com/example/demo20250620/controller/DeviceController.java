package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRepair;
import com.example.demo20250620.entity.Devicestate;
import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.DeviceRepairRepository;
import com.example.demo20250620.repository.DevicestateRepository;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/deviceaction/")
public class DeviceController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DevicestateRepository devicestateRepository;
    @Autowired
    private DeviceRepairRepository deviceRepairRepository;
    
    @Autowired
    private com.example.demo20250620.repository.SysUserRepository sysUserRepository;
    
    @Autowired
    private com.example.demo20250620.service.DeviceStatusNotificationService deviceStatusNotificationService;
    
    @Autowired
    private com.example.demo20250620.service.LogOperationService logOperationService;

    @Autowired
    private final HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    public DeviceController(HttpServletRequest request, com.example.demo20250620.service.LogOperationService logOperationService) {
        this.request = request;
        this.logOperationService = logOperationService;
    }

//    @CrossOrigin(origins = "http://127.0.0.1:8080")

    public boolean EmptyorNot(String s){return s == null || s.isEmpty();}



    @GetMapping("/alldevices")
    public Map<String, Object> getAllDevices(
            @RequestParam(required = false) String devicexp,
            @RequestParam(required = false) String devicetype,
            @RequestParam(required = false) String devicexh,
            @RequestParam(required = false) String devicecs,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            int pageIndex = Math.max(0, page - 1);
            Page<Device> devicePage;
            
            boolean hasXp = !EmptyorNot(devicexp);
            boolean hasType = !EmptyorNot(devicetype);
            boolean hasXh = !EmptyorNot(devicexh);
            boolean hasCs = !EmptyorNot(devicecs);
            
            if (!hasXp && !hasType && !hasXh && !hasCs) {
                devicePage = deviceRepository.findAllWithDevType(PageRequest.of(pageIndex, limit));
            } else {
                devicePage = deviceRepository.findByMultipleConditions(
                    devicexp, devicetype, devicexh, devicecs, PageRequest.of(pageIndex, limit));
            }
            
            responseObj.put("data", devicePage.getContent());
            responseObj.put("total", devicePage.getTotalElements());
            responseObj.put("success", true);
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "获取设备列表失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PostMapping("/createdevice")
    public Map<String, Object> createDevice(@RequestBody Device device){
        Map<String, Object> responseObj = new HashMap<>();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        try {
            device.setDevicescdata(LocalDateTime.now());
            device.setDeviceajdata(null);
            device.setDeviceghdata(null);
            
            Devicestate state = new Devicestate();
            state.setId(1L);
            device.setDevicestate(state);
            
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(device.getId());
            responseObj.put("success", true);
            responseObj.put("message", "设备创建成功");

            // 记录设备新增成功日志
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_CREATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】新增设备【" + device.getDeviceno() + "】成功",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            device.getId(),
                            device.getDeviceno(),
                            request);
                }
            }
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备创建失败: " + e.getMessage());

            // 记录设备新增失败日志
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_CREATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】新增设备失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            null,
                            device.getDeviceno(),
                            e.getMessage(),
                            request);
                }
            }
        }
        return responseObj;
    }

    @DeleteMapping("/deldevices/{id}")
    public void deleteDevice(@PathVariable Long id){deviceRepository.deleteById(id);}
    
    @PutMapping("/checkdevice/{id}")
    public Map<String, Object> checkDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                if (currentUser != null && currentUser.isPresent() && logOperationService != null) {
                    SysUser admin = currentUser.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_CHECK,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】设备安检失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            id,
                            null,
                            "设备不存在",
                            request);
                }
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDeviceajdata(LocalDateTime.now());
            
            Devicestate state = new Devicestate();
            state.setId(2L);
            device2.setDevicestate(state);
            
            deviceRepository.save(device2);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(id);
            
            // 记录操作日志
            if (currentUser != null && currentUser.isPresent() && logOperationService != null) {
                SysUser admin = currentUser.get();
                logOperationService.logSuccess(
                        admin.getId(),
                        admin.getSysusername(),
                        admin.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_CHECK,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "管理员【" + admin.getSysusername() + "】安检设备【" + device2.getDeviceno() + "】成功",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        id,
                        device2.getDeviceno(),
                        request);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "设备安检成功");
        } catch (Exception e) {
            // 记录失败日志
            if (currentUser != null && currentUser.isPresent() && logOperationService != null) {
                SysUser admin = currentUser.get();
                logOperationService.logFail(
                        admin.getId(),
                        admin.getSysusername(),
                        admin.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_CHECK,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "管理员【" + admin.getSysusername() + "】设备安检失败：" + e.getMessage(),
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        id,
                        null,
                        e.getMessage(),
                        request);
            }
            responseObj.put("success", false);
            responseObj.put("message", "设备安检失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/returndevice/{id}")
    public Map<String, Object> returnDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDeviceghdata(LocalDateTime.now());
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(10L);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(id);
            responseObj.put("success", true);
            responseObj.put("message", "设备归还成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备归还失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/repairdevice/{id}")
    public Map<String, Object> repairDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(6L);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(id);
            responseObj.put("success", true);
            responseObj.put("message", "设备维修状态更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备维修状态更新失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/unshelvedevice/{id}")
    public Map<String, Object> unshelveDevice(@PathVariable Long id, @RequestBody(required = false) Map<String, String> request){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            
            // 根据设备是否被借出来决定上架后的状态
            // 如果设备被借出（deviceyhid 不为空），状态变为"借用中"(ID=4)
            // 否则状态变为"已安检待借用"(ID=2)
            Long newStateId = (device2.getDeviceyh() != null) ? 4L : 2L;
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(newStateId);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(id);
            
            List<DeviceRepair> repairs = deviceRepairRepository.findByDeviceId(id);
            for (DeviceRepair repair : repairs) {
                if (repair.getEndRepairTime() == null) {
                    repair.setEndRepairTime(LocalDateTime.now());
                    if (request != null && request.containsKey("repairRecord")) {
                        repair.setRepairRecord(request.get("repairRecord"));
                    }
                    deviceRepairRepository.save(repair);
                }
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "设备上架成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备上架失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("updatedevicebyid/{id}")
    public Map<String, Object> updateDevice(@PathVariable Long id, @RequestBody Device device){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDevCpu(device.getDevCpu());
            device2.setDevType(device.getDevType());
            device2.setDevicexh(device.getDevicexh());
            device2.setDevManufacturer(device.getDevManufacturer());
            device2.setDevicesn(device.getDevicesn());
            device2.setDeviceno(device.getDeviceno());
            device2.setDevicescdata(device.getDevicescdata());
            device2.setDeviceajdata(device.getDeviceajdata());
            device2.setDeviceghdata(device.getDeviceghdata());
            device2.setDeviceyh(device.getDeviceyh());
            device2.setDevicestate(device.getDevicestate());

            deviceRepository.save(device2);
            responseObj.put("success", true);
            responseObj.put("message", "设备更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备更新失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 操作员转借设备
     */
    @PostMapping("/transferDevice")
    public Map<String, Object> transferDevice(@RequestBody Map<String, Object> request) {
        System.out.println("=== transferDevice 方法被调用 ===");
        System.out.println("请求参数: " + request);
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = Long.parseLong(request.get("deviceId").toString());
            Long targetUserId = Long.parseLong(request.get("targetUserId").toString());
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 设置转借目标用户ID
            device.setTransferTargetId(targetUserId);
            
            // 更新设备状态为"转借中待转借人通过"(ID=7)
            Optional<Devicestate> stateOpt = devicestateRepository.findById(7L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            System.out.println("=== 准备通知客户端设备状态更新 ===");
            System.out.println("设备ID: " + deviceId);
            System.out.println("deviceStatusNotificationService: " + (deviceStatusNotificationService != null ? "可用" : "不可用"));
            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                System.out.println("=== 通知已发送 ===");
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "转借申请已提交，等待转借人确认");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "转借失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 转借人同意转借
     */
    @PostMapping("/acceptTransfer")
    public Map<String, Object> acceptTransfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = Long.parseLong(request.get("deviceId").toString());
            Long userId = Long.parseLong(request.get("userId").toString());
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 验证当前用户是否是转借目标用户
            if (!userId.equals(device.getTransferTargetId())) {
                responseObj.put("success", false);
                responseObj.put("message", "您不是转借目标用户，无法同意转借");
                return responseObj;
            }
            
            // 更新借用人为当前用户
            Optional<SysUser> userOpt = sysUserRepository.findById(userId);
            if (!userOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                return responseObj;
            }
            device.setDeviceyh(userOpt.get());
            
            // 更新设备状态为"借用中"(ID=4)
            Optional<Devicestate> stateOpt = devicestateRepository.findById(4L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            // 清空转借目标用户ID
            device.setTransferTargetId(null);
            
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            responseObj.put("success", true);
            responseObj.put("message", "转借已同意，设备状态已更新为借用中");
        } catch (Exception e) {
            responseObj.put("success", true);
            responseObj.put("message", "同意转借失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 导出设备信息到Excel
     */
    @GetMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(required = false) String devicexp,
            @RequestParam(required = false) String devicetype,
            @RequestParam(required = false) String devicexh,
            @RequestParam(required = false) String devicecs) throws IOException {
        
        List<Device> devices;
        boolean hasXp = !EmptyorNot(devicexp);
        boolean hasType = !EmptyorNot(devicetype);
        boolean hasXh = !EmptyorNot(devicexh);
        boolean hasCs = !EmptyorNot(devicecs);
        
        if (!hasXp && !hasType && !hasXh && !hasCs) {
            devices = deviceRepository.findAllWithDevType();
        } else {
            devices = deviceRepository.findByMultipleConditions(devicexp, devicetype, devicexh, devicecs);
        }
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("设备信息");
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        
        String[] headers = {"芯片", "类型", "型号", "厂商", "序列号", "编号", "送测日期", "安检日期", "归还厂商日期", "借用人", "状态"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 1;
        for (Device device : devices) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(device.getDevCpu() != null ? device.getDevCpu().getCpuname() : "");
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(device.getDevType() != null ? device.getDevType().getTypename() : "");
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(device.getDevicexh() != null ? device.getDevicexh() : "");
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(device.getDevManufacturer() != null ? device.getDevManufacturer().getManufacturername() : "");
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(device.getDevicesn() != null ? device.getDevicesn() : "");
            cell4.setCellStyle(dataStyle);
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(device.getDeviceno() != null ? device.getDeviceno() : "");
            cell5.setCellStyle(dataStyle);
            
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(device.getDevicescdata() != null ? device.getDevicescdata().toString() : "");
            cell6.setCellStyle(dataStyle);
            
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(device.getDeviceajdata() != null ? device.getDeviceajdata().toString() : "");
            cell7.setCellStyle(dataStyle);
            
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(device.getDeviceghdata() != null ? device.getDeviceghdata().toString() : "");
            cell8.setCellStyle(dataStyle);
            
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(device.getDeviceyh() != null ? device.getDeviceyh().getSysusername() : "");
            cell9.setCellStyle(dataStyle);
            
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(device.getDevicestate() != null ? device.getDevicestate().getStateDetail() : "");
            cell10.setCellStyle(dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        String filename = "设备信息_" + java.time.LocalDateTime.now().toString().replace(":", "-") + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        
        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", encodedFilename);
        responseHeaders.add("Access-Control-Expose-Headers", "Content-Disposition");
        
        return new ResponseEntity<>(outputStream.toByteArray(), responseHeaders, org.springframework.http.HttpStatus.OK);
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        try {
            String userIdStr = (String) request.getSession().getAttribute("userId");
            return userIdStr != null ? Long.parseLong(userIdStr) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUserName() {
        try {
            return (String) request.getSession().getAttribute("userName");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前登录用户角色
     */
    private Integer getCurrentUserRole() {
        try {
            String roleStr = (String) request.getSession().getAttribute("userRole");
            return roleStr != null ? Integer.parseInt(roleStr) : null;
        } catch (Exception e) {
            return null;
        }
    }

}
