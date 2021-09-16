<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div
            class="table-con flex"
            style="flex:1; height:0px"
        >
            <div
                v-show="isMenuShow"
                class="user-l"
                style='height:100%;width:300px;border:1px solid #ddd;'
            >
                <div class="organizational-tit">组织机构菜单</div>
                <div style="height:calc(100% - 34px);overflow-y:auto">
                    <organization @currentChangeTree="currentChangeTree"></organization>
                </div>
            </div>
            <div style="width:10px" class="flex-c btn-show-hide" @click="changeMenuShowHide"><span :class="{'el-icon-caret-right':!isMenuShow,'el-icon-caret-left':isMenuShow}"></span></div>
            <div
                class="user-r flex-c"
                style='height:100%;flex:1; width:0px'
            >
                <div class="top-con flex-n">
                    <div class="con-w">
                        <span>用户名：</span>
                        <el-input
                            size="small"
                            class="w100"
                            v-model="searchObj.userName"
                        ></el-input>
                    </div>
                    <div class="con-w">
                        <span>登录名：</span>
                        <el-input
                            size="small"
                            class="w100"
                            v-model="searchObj.loginName"
                        ></el-input>
                    </div>
                    <div class="con-w">
                        <span>手机号：</span>
                        <el-input
                            size="small"
                            class="w150"
                            v-model="searchObj.mobile"
                        ></el-input>
                    </div>
                    <div class="con-w">
                        <span>角色：</span>
                        <el-select
                            v-model="searchObj.roleId"
                            placeholder="请选择"
                            clearable
                            filterable
                            style="width:150px"
                        >
                            <el-option
                                v-for="item in rolesArr"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </div>
                    <div class="con-w">
                        <el-button
                            size="small"
                            icon="el-icon-search"
                            type="primary"
                            @click="search"
                        >搜索</el-button>
                    </div>
                    <div class="con-w">
                        <el-button
                            size="small"
                            icon="el-icon-plus"
                            @click="addFun"
                            v-has="'add'"
                        >新增</el-button>
                    </div>
                </div>
                <div style="flex:1;height:0">
                    <vxe-table
                        border
                        :data="list"
                        size="mini"
                        height="auto"
                        :loading="loading"
                        auto-resize
                    >
                        <vxe-table-column
                            type="seq"
                            title="序号"
                            width="50"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="name"
                            title="用户名"
                            width="100"
                        >
                            <template
                        slot="header"
                    >
                        <span>用户名</span><span class="red-star">*</span>
                    </template>
                        </vxe-table-column>
                        <vxe-table-column
                            field="loginName"
                            title="登录名"
                            width="120"
                        >
                            <template
                        slot="header"
                    >
                        <span>登录名</span><span class="red-star">*</span>
                    </template>
                        </vxe-table-column>
                        <vxe-table-column
                            field="mobile"
                            title="手机号"
                            width="120"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="email"
                            title="邮箱"
                            width="140"
                        >
                        </vxe-table-column>
                        <vxe-table-column
                            field="position"
                            title="岗位"
                            width="120"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="deptName"
                            title="部门名称"
                            width="120"
                        >
                            <template
                        slot="header"
                    >
                        <span>部门名称</span><span class="red-star">*</span>
                    </template>
                        </vxe-table-column>
                        <vxe-table-column
                            field="status"
                            title="状态"
                            width="80"
                        >
                            <template #default={row}>
                                <span
                                    v-if="row.status"
                                    style="color:#42a3fd"
                                >正常</span>
                                <span
                                    v-else
                                    style="color:red"
                                >禁用</span>
                            </template>
                        </vxe-table-column>

                        <vxe-table-column
                            field="roleName"
                            title="角色"
                            width="120"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="createTime"
                            title="操作"
                            min-width="150px"
                            fixed="right"
                        >
                            <template #default="{row}">
                                <el-button
                                    size="mini"
                                    type="primary"
                                    plain
                                    @click="editFun(row.id)"
                                    v-has="'edit'"
                                >
                                    修改
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    plain
                                    @click="delFun(row.id)"
                                    v-has="'del'"
                                >
                                    删除
                                </el-button>
                            </template>
                        </vxe-table-column>
                    </vxe-table>
                </div>
            </div>
        </div>
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="pageSize"
            align="right"
            background
            :page-sizes="[10, 50, 100, 150, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            :total="total"
            @current-change="handleCurrentChange"
        />

        <!-- 新增/修改 -->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            :close-on-click-modal="false"
            width="500px"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="用户名"
                    prop="name"
                >
                    <el-input
                        v-model="ruleForm.name"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="登录名"
                    prop="loginName"
                >
                    <el-input
                        v-model="ruleForm.loginName"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="密码"
                    prop="password"
                >
                    <el-input
                        v-model="ruleForm.password"
                        auto-complete="new-password"
                        style="width:250px"
                        type="password"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="电话"
                    prop="mobile"
                >
                    <el-input
                        v-model="ruleForm.mobile"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="邮箱"
                    prop="email"
                >
                    <el-input
                        v-model="ruleForm.email"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="岗位"
                    prop="position"
                >
                    <el-input
                        v-model="ruleForm.position"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="部门"
                    prop="deptId"
                >
                    <el-cascader
                        v-model="ruleForm.deptId"
                        style="width:250px"
                        clearable
                        :options="teamArr"
                        :props="defalutProps"
                        :show-all-levels="false"
                    />
                </el-form-item>
                <el-form-item
                    label="角色"
                    prop="roleId"
                >
                    <el-select
                        v-model="ruleForm.roleId"
                        filterable
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in rolesArr"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="状态"
                    prop="status"
                >
                    <el-radio-group v-model="ruleForm.status">
                        <el-radio :label="1">启用</el-radio>
                        <el-radio :label="0">禁用</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="visable1 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { getTeam } from '_api/productionProcess/process'
import { getUserTree, findByIdUserList, getRolesListNoPage, addUser, editUser, delUser, findIdUserInfo } from '_api/system/systemApi'
import organization from '_c/Organization'
export default {
    components: { organization },
    name: 'user-manage',
    data () {
        let validatorPhone = function (rule, value, callback) {
            if (value.replace(/\s+/g, "") === '') {
                callback()
            } else if (!/^1\d{10}$/.test(value)) {
                callback(new Error('手机号格式错误'))
            } else {
                callback()
            }
        }
        return {
            list: [],
            //分页
            page: 1,
            total: 0,
            pageSize:10,

            //搜索条件
            searchObj: {
                deptId: '',//部门id
                userName: '',//用户名
                loginName: '',//登录名
                mobile: '',//手机号
                roleId: '',//角色
            },

            visable1: false,
            ruleFormObj: {

            },
            ruleForm: {
                name: '',//用户名
                password: '',//密码
                email: '',//邮箱
                mobile: '',//手机号
                status: 1,//状态
                deptId: '',//部门ID
                loginName: '',//登录名
                position: '',//岗位
                roleId: ''//角色
            },
            rules: {
                name: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                loginName: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                mobile: [
                    { validator: validatorPhone, trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                // position: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                deptId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
            },
            title: '新建用户',

            //机构数据
            teamArr: [],

            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list',
                checkStrictly: true
            },
            loading: false,

            
            //角色下拉数据
            rolesArr: [],

            isMenuShow:true
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        this.getList();
        this.getRolesListFun();
        this.getTeamList();
    },
    methods: {
        //获取角色下拉列表
        async getRolesListFun () {
            let { data, code } = await getRolesListNoPage();
            if (code == 200) {
                this.rolesArr = data || []
            }
        },


        search () {
            this.page = 1;
            this.getList();
        },
        

        //根据部门id获取用户列表
        async getList (id) {
            let req = {
                pn: this.page,
                size:this.pageSize,
                ...this.searchObj
            }
            this.loading = true;
            let { code, data } = await findByIdUserList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新建用户"
            this.visable1 = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })

        },
        //修改
        async editFun (id) {
            this.title = "编辑用户"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await findIdUserInfo({ id });
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data || {}
                })
            }
        },
        //删除
        delFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { code, data } = await delUser({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })

        },

        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || []
        },
        //分页切换
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },
        handleSizeChange (s) {
            this.pageSize = s;
            this.getList();
        },

        // 新增/编辑提交
        submitForm (formName) {
            console.log(this.ruleForm)
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await editUser(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await addUser(req);
                        if (code == 200) {
                            this.$message.success('新增成功')
                            this.visable1 = false
                            this.getList()
                        }
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },
        currentChangeTree (v) {
            this.searchObj.deptId = v.id;
            this.search();
        },

        changeMenuShowHide(){
            this.isMenuShow = !this.isMenuShow;
        }

    }
}
</script>

<style>
.user-l * {
    font-size: 12px;
}
.organizational-tit {
    height: 34px;
    padding: 0 10px;
    line-height: 34px;
    background: #f8f8f8;
    font-weight: bold;
    border-bottom: 1px solid #ddd;
}
</style>


