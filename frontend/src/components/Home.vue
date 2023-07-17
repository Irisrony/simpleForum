<style src="../assets/css/home.css" scoped></style>

<script>
import request from '@/utils/request'
import {ElMessage} from 'element-plus'

export default {
  data(){
    // 注册密码校验
    const validateSurePassword = (rule, value, callback) => {
      if (value !== '') {
        if (value !== this.registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
          return false
        } else {
          callback()
        }
      }
    }
    return {
      // 分页查询
      queryForm:{
        pageNum: 1,
        pageSize: 10
      },
      selectedArr: [],
      // 帖子总数
      total: 0,
      // 登录表单
      loginForm:{
        username: '',
        password: ''
      },
      // 登录用户
      loginUsername: '',
      // 登录状态
      isLogin: false,
      // 帖子列表
      postData: [],
      // 注册表单
      registerForm:{
        username: '',
        password: '',
        okpassword: ''
      },
      // 注册表单状态
      registerFormVisible: false,
      // 注册数据校验规则
      registerFormRules:{
        username:[
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 4, max: 12, message: '用户名长度在 4 到 12 字符之间', trigger: 'blur' },
        ],
        password:[
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
          {
            min: 6,
            max: 12,
            message: '密码在 6 到 12 个字符之间',
            trigger: 'blur',
          },
        ],
        okpassword: [
          {
            required: true,
            message: '请再次输入密码',
            trigger: 'blur',
          },
          {
            validator: validateSurePassword,
            trigger: 'blur',
          },
        ]
      },
      // 发帖表单
      postsForm:{
        auth: '',
        title: '',
        content: '',
      },
      // 发帖表单状态
      postsFormVisible: false,
      // 发帖表单校验规则
      postsFormRules:{
        title:[
          {
            required: true,
            message: '请输入标题',
            trigger: 'blur',
          },
          {
            min: 4,
            max: 10,
            message: '标题长度在 4 到 10 个字符之间',
            trigger: 'blur',
          },
        ],
        content: [
          {
            required: true,
            message: '请输入帖子内容',
            trigger: 'blur',
          },
          {
            min: 10,
            max: 100,
            message: '内容长度在 10 到 100 个字符之间',
            trigger: 'blur',
          },
        ]
      },
    }
  },
  created() {
    this.getPosts()
    if (sessionStorage.getItem("token") != null){
      if (sessionStorage.getItem("username") != null){
        this.isLogin = true
        this.loginUsername = sessionStorage.getItem("username")
      }else{
        sessionStorage.clear()
      }
    }
  },
  methods:{
    // 请求分页查询数据
    getPosts(){
      request.get('/posts/page', {params:this.queryForm}).then(res => {
        // console.log(res);
        this.postData = res.data
        this.total = res.total
        this.onDeletePosts()
      })
    },
    resetLoginForm(){
      this.loginForm.username = ''
      this.loginForm.password = ''
    },
    resetRegisterForm(){
      this.registerForm.username = ''
      this.registerForm.password = ''
      this.registerForm.okpassword = ''
    },
    resetPostsForm(){
      this.postsForm.title = ''
      this.postsForm.content = ''
      this.$refs.quillEditor.setContents(null)
    },
    // 用户登录
    onSubmit(){
      if(this.loginForm.username === '' || this.loginForm.password === ''){
        ElMessage({
          showClose: true,
          message: 'Username or password wrong!',
          type: "error"
        })
        this.resetLoginForm()
        return
      }
      this.loginUsername = this.loginForm.username
      request.post('/user/login',this.loginForm).then(res=>{
        // console.log(res)
        this.resetLoginForm()
        if(res.status){
          ElMessage({
            showClose: true,
            message: 'Login Success!',
            type: "success"
          })
          this.isLogin = true
          sessionStorage.setItem("token",res.token)
          sessionStorage.setItem("username",this.loginUsername)
        }else{
          ElMessage({
            showClose: true,
            message: 'Username or password wrong!',
            type: "error"
          })
          this.loginUsername = ''
        }
      })
    },
    // 分页查询状态改变
    handleSizeChange(){
      this.getPosts()
    },
    handleCurrentChange(){
      this.getPosts()
    },
    // 用户注册
    uploadRegister(){
      this.$refs.registerFormRef.validate(valid=>{
        if(!valid){
          ElMessage({
            showClose: true,
            message: '请正确填写注册信息',
            type: "error"
          })
          this.resetRegisterForm()
          return
        }
        request.post('/user/register',this.registerForm).then(res =>{
          this.resetRegisterForm()
          if(res.status){
            ElMessage({
              showClose: true,
              message: '注册成功！',
              type: "success"
            })
          }else{
            ElMessage({
              showClose: true,
              message: '注册失败！',
              type: "error"
            })
          }
          this.registerFormVisible = false
        })

      })
    },
    // 用户退出
    onLogout(){
      sessionStorage.clear()
      this.resetLoginForm()
      this.isLogin = false
      this.loginUsername = ''
      ElMessage({
        showClose: true,
        message: '您已成功退出登录',
        type: "success"
      })
    },
    // 删除帖子
    onDeletePosts(){
      this.$refs.postsTable.getSelectionRows().forEach(item=>{
        this.selectedArr.push(item.id)
      })
      this.total -= this.selectedArr.length
      let newPostData = []
      this.postData.forEach(item=>{
        if (!(this.selectedArr.includes(item.id))){
          newPostData.push(item)
        }
      })
      this.postData = newPostData
    },
    validateContent(){
      let len = this.postsForm.content.length
      return  len > 10 && len <= 100
    },
    // 编辑器修改
    handleTextChange(){
      this.postsForm.content = this.$refs.quillEditor.getContents()
    },
    // 帖子上传
    uploadPosts(){
      if(!this.validateContent()){
        ElMessage({
          showClose: true,
          message: '内容长度在 10 到 100 个字符之间',
          type: "error"
        })
        return
      }

      this.postsForm.content = this.$refs.quillEditor.getHTML()

      this.postsForm.auth = this.loginUsername

      this.$refs.postsFormRef.validate(valid=>{
        if(!valid){
          ElMessage({
            showClose: true,
            message: '请正确填写帖子内容',
            type: "error"
          })
          this.handleTextChange()
          return
        }
        request.post('/posts/upload',this.postsForm).then(res =>{
          this.resetPostsForm()
          if(res.status){
            ElMessage({
              showClose: true,
              message: '帖子上传成功！',
              type: "success"
            })
            // 更新帖子信息
            this.getPosts()
          }else{
            ElMessage({
              showClose: true,
              message: '帖子上传失败！',
              type: "error"
            })
          }
          this.postsFormVisible = false
        })
        this.resetPostsForm()
      })
    },
    handleClick(row){
      sessionStorage.setItem('id',row.id);
      this.$router.push('/page')
    }
  }
}

</script>

<template>
  <div class="body-box">
    <!-- 头部 -->
    <header>
      <!-- 登录引导区 -->
      <el-form :model="loginForm" id="header_form">
        <img src="../assets/imgs/logo.png" alt="">
        <div>
          <!--登录区-->
          <el-row >
            <el-form-item label="账号" v-show="!isLogin">
              <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-text v-show="isLogin">欢迎您，{{this.loginUsername}}</el-text>
          </el-row>
          <el-row>
            <el-form-item label="密码" v-show="!isLogin">
              <el-input type="password" v-model="loginForm.password"></el-input>
            </el-form-item>
            <!--  登录前功能区  -->
            <el-button @click="onSubmit" v-show="!isLogin">登录</el-button>
            <el-link @click="registerFormVisible=true" v-show="!isLogin">立即注册</el-link>

            <!--  登录后功能区  -->
            <el-button @click="postsFormVisible=true" v-show="isLogin">发帖</el-button>
            <el-button @click="onDeletePosts" v-show="isLogin">删除</el-button>
            <el-button @click="onLogout" v-show="isLogin">退出</el-button>
          </el-row>
        </div>
      </el-form>

      <!-- 导航区 -->
      <nav class="nv">
        <ul type="none">
          <li>
            <a href="#">首页</a>
          </li>
        </ul>
        <button></button>
        <input type="text" placeholder="请输入搜索内容">
      </nav>
    </header>

    <!-- 内容区 -->
    <div class="container">
      <div class="stat">
        <h2>最新帖子</h2>
      </div>

      <!-- 帖子浏览区 -->
      <div class="posts_list_container">
        <el-table :data="postData" ref="postsTable" height="100%" style="width: 100%" border @row-click="handleClick">
          <el-table-column type="selection" width="50px"/>
          <el-table-column prop="auth" label="Auth" width="240px"/>
          <el-table-column prop="title" label="Title"/>
          <!--<el-table-column prop="content" label="Content" show-overflow-tooltip='true' ></el-table-column>-->
          <el-table-column prop="date" label="Date" />
        </el-table>
        <el-pagination
            v-model:current-page="queryForm.pageNum"
            v-model:page-size="queryForm.pageSize"
            :page-sizes="[1, 2, 5, 10]"
            :small="false"
            layout="sizes, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 注脚 -->
    <footer>
      <div class="foot_stat">
        <p><b>免责声明：本站所有帖子内容以及链接均为虚构，如有侵权问题，请及时联系，我们将尽快处理.</b></p>
        <p><b>202030441499 计科一 刘盛豪</b></p>
      </div>
    </footer>

    <!--  用户注册  -->
    <el-dialog v-model="registerFormVisible" title="用户注册">
      <!--注册表单-->
      <el-form :model="registerForm" label-width="80px" label-position="left" :rules="registerFormRules" ref="registerFormRef">
          <el-form-item label="用户名" prop="username">
            <el-input type="text" v-model="registerForm.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="registerForm.password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="okpassword">
            <el-input type="password" v-model="registerForm.okpassword"></el-input>
          </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="registerFormVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadRegister">
          确认
        </el-button>
      </span>
      </template>
    </el-dialog>

    <!--  用户发帖  -->
    <el-dialog v-model="postsFormVisible" title="发帖区">
      <!--发帖表单-->
      <el-form :model="postsForm" label-width="80px" label-position="left" :rules="postsFormRules" ref="postsFormRef">
        <el-form-item label="标题" prop="title">
          <el-input type="text" v-model="postsForm.title" style="width: 200px;"></el-input>
        </el-form-item>
        <QuillEditor @textChange="handleTextChange" contentType="text" ref="quillEditor" plcaeholder="请输入帖子内容" theme="snow" style="height: 200px;"></QuillEditor>
      </el-form>
      <template #footer>
        <p>您还可以输入 <strong>{{100 - postsForm.content.length}}</strong> 个字</p>
        <span class="dialog-footer">
          <el-button @click="postsFormVisible = false">取消</el-button>
          <el-button type="primary" @click="uploadPosts">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


