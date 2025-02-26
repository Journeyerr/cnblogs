package com.cnblog.qrcodeLogIn.controller;

import com.alibaba.fastjson2.JSONObject;
import com.cnblog.qrcodeLogIn.dto.LoginInfoDTO;
import com.cnblog.qrcodeLogIn.vo.ResponseVO;
import com.cnblog.qrcodeLogIn.enums.LoginStatusEnum;
import com.cnblog.qrcodeLogIn.utils.JwtUtil;
import com.cnblog.qrcodeLogIn.utils.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author AnYuan
 */

@Slf4j
@RestController
@RequestMapping("/api/qrcode")
public class LoginController {
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    private String cacheKey(String uuid) {
        return "users:login:" + uuid;
    }
    
    private void cache(LoginInfoDTO loginInfoDTO) {
        // 获取登录缓存信息，有效期2分钟
        stringRedisTemplate.opsForValue().set(cacheKey(loginInfoDTO.getUuid()), JSONObject.toJSONString(loginInfoDTO), 2, TimeUnit.MINUTES);
    }
    
    private LoginInfoDTO getCache(String uuid) {
        // 获取登录缓存信息
        String s = stringRedisTemplate.opsForValue().get(cacheKey(uuid));
        return s == null ? null : JSONObject.parseObject(s, LoginInfoDTO.class);
    }
    
    /**
     * 生成二维码
     * @return ResponseEntity
     * @throws Exception
     */
    @GetMapping("/generate")
    public ResponseEntity<ResponseVO> generateQRCode() throws Exception {
        String uuid = UUID.randomUUID().toString();
        String base64QR = QRCodeUtil.generateQRCode(uuid, 200, 200);
    
        LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
        loginInfoDTO.setStatus(LoginStatusEnum.UNSCANNED.name());
        loginInfoDTO.setUuid(uuid);
    
        // 二维码uuid绑定，存入缓存
        cache(loginInfoDTO);
        
        // 返回生成的二维码信息
        ResponseVO vo = ResponseVO.builder().uuid(uuid).qrcode("data:image/png;base64," + base64QR).build();
        
        log.info("-------生成二维码成功：{}-------", uuid);
        return ResponseEntity.ok(vo);
    }
    
    /**
     * 检查扫码状态
     * @param uuid
     * @return
     */
    @GetMapping("/check/{uuid}")
    public ResponseEntity<?> checkStatus(@PathVariable String uuid) {
    
        LoginInfoDTO loginInfoDTO = getCache(uuid);
        if (loginInfoDTO == null) {
            return ResponseEntity.status(410).body("二维码已过期");
        }
    
        String token = "";
        if (LoginStatusEnum.CONFIRMED.name().equals(loginInfoDTO.getStatus())) {
            token = JwtUtil.generateAuthToken(uuid);
        }
    
        ResponseVO vo = ResponseVO.builder().token(token).status(loginInfoDTO.getStatus()).build();
        
        log.info("-------校验二维码状态uuid:{}, 状态：{}-------", uuid, loginInfoDTO.getStatus());
        return ResponseEntity.ok(vo);
    }
    
    /**
     * 手机端确认登录
     * @param uuid
     * @return
     */
    @PostMapping("/scan/{uuid}")
    public ResponseEntity<?> scanQrCode(@PathVariable String uuid) {
    
        LoginInfoDTO loginInfoDTO = getCache(uuid);
        loginInfoDTO.setStatus(LoginStatusEnum.SCANNED.name());
        
        cache(loginInfoDTO);
    
        log.info("-------扫码成功uuid:{}-------", uuid);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 手机端确认登录
     * @param uuid
     * @return
     */
    @PostMapping("/confirm/{uuid}")
    public ResponseEntity<?> confirm(@PathVariable String uuid) {
    
        LoginInfoDTO loginInfoDTO = getCache(uuid);
        loginInfoDTO.setStatus(LoginStatusEnum.CONFIRMED.name());
    
        cache(loginInfoDTO);
        
        log.info("-------确认登录成功uuid:{}-------", uuid);
        return ResponseEntity.ok().build();
    }
}
