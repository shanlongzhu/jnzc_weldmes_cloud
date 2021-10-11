<template>
    <!-- 新增/修改 -->
    <el-dialog
        title="修改密码"
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
                label="原密码："
                prop="oldPwd"
                v-if="step==1"
            >
                <el-input
                    v-model="ruleForm.oldPwd"
                    key="oldPwd"
                    style="width:250px"
                ></el-input>
            </el-form-item>
            <el-form-item
                label="新密码："
                prop="newPwd"
                key="newPwd"
                v-if="step==2"
            >
                <el-input
                    v-model="ruleForm.newPwd"
                    style="width:250px"
                ></el-input>
            </el-form-item>
            <el-form-item
                label="重复新密码："
                prop="repeatNewPwd"
                key="repeatNewPwd"
                v-if="step==2"
            >
                <el-input
                    v-model="ruleForm.repeatNewPwd"
                    style="width:250px"
                ></el-input>
            </el-form-item>
            <el-form-item v-if="step==1">
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                >下一步</el-button>
            </el-form-item>
            <el-form-item v-if="step==2">
                <el-button
                    type="primary"
                    @click="submitForm('ruleForm')"
                >提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
export default {
    components: {},
    props: {},
    data () {
        return {
            visable1: false,
            step: 1,
            ruleForm: {
                oldPwd: '',
                newPwd: '',
                repeatNewPwd: ''
            },
            rules: {
                oldPwd: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                newPwd: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                repeatNewPwd: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
            },

        }
    },
    watch: {},
    computed: {},
    methods: {
        init () {
            this.visable1 = true;
            this.step = 1;
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

    },
    created () { },
    mounted () { }
}
</script>
<style lang="scss" scoped>
.wrapper {
}
</style>