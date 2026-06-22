package com.donghai.leakage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.donghai.leakage.common.*;
import com.donghai.leakage.dto.SystemConfigRequest;
import com.donghai.leakage.entity.SysConfig;
import com.donghai.leakage.mapper.SysConfigMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import com.donghai.leakage.security.RequirePermission;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    private final SysConfigMapper mapper;
    @Value("${app.upload-dir:uploads}") private String uploadDir;

    public SystemConfigController(SysConfigMapper mapper) { this.mapper = mapper; }

    @GetMapping
    @RequirePermission("system:config:list")
    public Result<Map<String, String>> get() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("systemName", value("system_name", "供水管网漏损控制管理系统"));
        result.put("systemLogo", value("system_logo", ""));
        return Result.success(result);
    }

    @PutMapping
    @RequirePermission("system:config:edit")
    public Result<Void> update(@Valid @RequestBody SystemConfigRequest request) {
        save("system_name", request.getSystemName(), "系统名称");
        save("system_logo", request.getSystemLogo() == null ? "" : request.getSystemLogo(), "系统 Logo 地址");
        return Result.success();
    }

    @PostMapping("/logo")
    @RequirePermission("system:config:edit")
    public Result<Map<String, String>> upload(@RequestParam MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("请选择图片文件");
        String original = Optional.ofNullable(file.getOriginalFilename()).orElse("logo.png");
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        if (!List.of(".jpg", ".jpeg", ".png", ".svg").contains(ext)) throw new BusinessException("仅支持 jpg、jpeg、png、svg 图片");
        Path dir = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(dir);
        String name = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        return Result.success("上传成功", Map.of("fileUrl", "/api/files/" + name));
    }

    private String value(String key, String fallback) {
        SysConfig config = mapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        return config == null || config.getConfigValue() == null ? fallback : config.getConfigValue();
    }

    private void save(String key, String value, String description) {
        SysConfig config = mapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        if (config == null) {
            config = new SysConfig(); config.setConfigKey(key); config.setDescription(description); config.setCreateTime(LocalDateTime.now());
        }
        config.setConfigValue(value); config.setUpdateTime(LocalDateTime.now());
        if (config.getId() == null) mapper.insert(config); else mapper.updateById(config);
    }
}
