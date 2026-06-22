<template>
  <div class="login-page">
    <div class="water-lines"></div>
    <section class="intro"><div class="big-logo">≋</div><h1>宁波东海供水管网<br/>漏损控制管理系统</h1><p>让每一滴水都被看见，让每一次异常都及时响应</p></section>
    <el-form ref="formRef" :model="form" :rules="rules" class="login-card" @keyup.enter="handleLogin">
      <div class="login-title">欢迎登录</div><div class="login-subtitle">智慧水务综合管理平台</div>
      <el-form-item prop="username"><el-input v-model="form.username" size="large" placeholder="请输入账号" :prefix-icon="User" /></el-form-item>
      <el-form-item prop="password"><el-input v-model="form.password" size="large" type="password" placeholder="请输入密码" show-password :prefix-icon="Lock" /></el-form-item>
      <el-form-item prop="captchaCode"><div class="captcha-row"><el-input v-model="form.captchaCode" size="large" placeholder="验证码" :prefix-icon="Key" /><img :src="captchaImage" alt="验证码" @click="loadCaptcha" title="点击刷新" /></div></el-form-item>
      <el-button type="primary" size="large" class="login-button" :loading="loading" @click="handleLogin">登录系统</el-button>
      <p class="tip">首次登录账号：admin，初始密码：123456</p>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import { captchaApi, loginApi } from '@/api/auth'
import { appStore } from '@/store/app'
const router=useRouter(), formRef=ref<FormInstance>(), captchaImage=ref(''), loading=ref(false)
const form=reactive({username:'admin',password:'123456',captchaId:'',captchaCode:''})
const rules={username:[{required:true,message:'请输入账号'}],password:[{required:true,message:'请输入密码'}],captchaCode:[{required:true,message:'请输入验证码'}]}
const loadCaptcha=async()=>{ const data=await captchaApi(); form.captchaId=data.captchaId; captchaImage.value=data.image; form.captchaCode='' }
const handleLogin=async()=>{ if(!await formRef.value?.validate().catch(()=>false))return; loading.value=true; try{const data=await loginApi(form);localStorage.setItem('token',data.token);localStorage.setItem('userInfo',JSON.stringify(data));appStore.ready=false;await appStore.load();ElMessage.success('登录成功');router.replace('/dashboard')}catch(e:any){ElMessage.error(e.message||'登录失败');loadCaptcha()}finally{loading.value=false}}
onMounted(loadCaptcha)
</script>

<style scoped>
.login-page{width:100vw;height:100vh;display:flex;align-items:center;justify-content:center;gap:150px;position:relative;overflow:hidden;background:linear-gradient(135deg,#087fd6,#0aa8dc 55%,#e9fbff);color:#fff}
.water-lines{position:absolute;inset:0;background:radial-gradient(circle at 20% 20%,rgba(255,255,255,.18),transparent 30%),radial-gradient(circle at 75% 75%,rgba(255,255,255,.22),transparent 22%)}
.intro,.login-card{position:relative;z-index:1}.intro{text-align:center;text-shadow:0 3px 12px rgba(0,65,116,.25)}.big-logo{width:105px;height:105px;border-radius:50%;background:#fff;color:#0788d7;display:grid;place-items:center;margin:0 auto 24px;font-size:80px;font-weight:900}.intro h1{font-size:34px;line-height:1.45;margin:0}.intro p{font-size:16px;opacity:.85;letter-spacing:2px}
.login-card{width:420px;padding:42px 40px 32px;border-radius:18px;background:rgba(255,255,255,.97);box-shadow:0 22px 70px rgba(0,66,114,.28);color:#244d6d}.login-title{text-align:center;font-size:26px;font-weight:800}.login-subtitle{text-align:center;color:#8da0b1;margin:8px 0 28px}.captcha-row{width:100%;display:flex;gap:10px}.captcha-row img{width:118px;height:40px;border-radius:6px;cursor:pointer}.login-button{width:100%;background:linear-gradient(90deg,#168be6,#08a0dc);border:0}.tip{text-align:center;color:#9aa9b6;font-size:12px;margin:18px 0 0}
@media(max-width:900px){.intro{display:none}.login-page{gap:0}.login-card{width:min(420px,calc(100vw - 32px))}}
</style>
