<template>
    <!-- 新增/修改 -->
    <el-dialog
        title="修改个人信息"
        :visible.sync="visable1"
        :close-on-click-modal="false"
        width="500px"
    >
        <el-form
            ref="ruleForm"
            :model="ruleForm"
            :rules="rules"
            label-width="120px"
            class="demo-ruleForm"
        >
            <el-form-item
                label="昵称："
                prop="userName"
            >
                <el-input
                    v-model="ruleForm.userName"
                    key="oldPwd"
                    style="width:250px"
                ></el-input>
            </el-form-item>
            <el-form-item label="头像：">
                <el-upload
                    class="avatar-uploader"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers"
                    :action="importUrl"
                >
                    <img
                        v-if="imageUrl"
                        :src="imageUrl"
                        class="avatar"
                    >
                    <i
                        v-else
                        class="el-icon-plus avatar-uploader-icon"
                    ></i>
                </el-upload>
            </el-form-item>

            <el-form-item>
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                >提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
import { getToken } from '@/utils/auth'
export default {
    components: {},
    props: {},
    data () {
        return {
            visable1: false,
            ruleForm: {
                userName: '',
            },
            rules: {
                userName: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
            },

            importUrl: `${process.env.VUE_APP_BASE_API}/collection/importExcel`,
            headers: {
                'Authorization': getToken()
            },
            imageUrl:''

        }
    },
    watch: {},
    computed: {},
    methods: {
        init () {
            this.visable1 = true
        },
        // 新增/编辑提交
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.step == 1) {
                        this.$refs[formName].resetFields();
                        this.step = 2;

                    }
                    console.log(1111)

                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },

        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        },

    },
    created () { },
    mounted () { }
}
</script>
<style lang="scss">
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 148px;
    height: 148px;
    line-height: 148px;
    text-align: center;
  }
  .avatar {
    width: 148px;
    height: 148px;
    display: block;
  }
</style>