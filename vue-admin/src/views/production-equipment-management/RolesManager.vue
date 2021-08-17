<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex"
    >

        <div
            class="roles-l flex-c"
            style="height:100%; flex:1; width:0px"
        >
            <div class="p10">
                <el-button
                    size="mini"
                    plain
                    @click="addRolsFun"
                    v-has="'add'"
                >新增角色</el-button>
            </div>
            <div
                class=""
                style="flex:1;height:0px"
            >
                <vxe-table
                    ref="xTable"
                    border
                    :data="list"
                    :loading="loading"
                    size="mini"
                    height="auto"
                    auto-resize
                    resizable
                    stripe
                    highlight-current-row
                    @current-change="selectTableData"
                >
                    <vxe-table-column
                        type="seq"
                        width="50"
                        title="序号"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="name"
                        title="角色名"
                        min-width="120"
                    >
                        <template
                        slot="header"
                        slot-scope="scope"
                    >
                        <span>角色名</span><span class="red-star">*</span>
                    </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="remark"
                        title="描述"
                        min-width="120"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="createTime"
                        title="操作"
                        width="100"
                    >
                        <template #default="{row,rowIndex}">
                            <span @click.stop="editFun(row.id,rowIndex)">
                                <i
                                    style="font-size:16px; color:#1890ff;cursor:pointer;margin-left:10px"
                                    class="el-icon-edit"
                                ></i>
                            </span>
                            <span @click.stop="delFun(row.id)">
                                <i
                                    style="font-size:16px;color:red;cursor:pointer;margin-left:10px"
                                    class="el-icon-delete"
                                ></i>
                            </span>
                        </template>
                    </vxe-table-column>
                </vxe-table>
            </div>
            <el-pagination
                class="p10"
                :current-page.sync="page"
                :page-size="10"
                align="right"
                background
                layout="total, prev, pager, next"
                :total="total"
                @current-change="handleCurrentChange"
            />

        </div>
        <div
            class="roles-r flex-c"
            style="height:100%; flex:1; width:0px"
        >
            <el-tabs
                v-model="activeName"
                class="flex-c"
                style="height:100%;"
            >
                <el-tab-pane
                    label="权限分配"
                    name="first"
                >
                    <div
                        class="p10"
                        style="border-bottom:1px solid #ddd"
                    >
                        <el-button
                            size="mini"
                            type="default"
                            plain
                            @click="saveAuth"
                        >保存权限</el-button>
                    </div>
                    <div
                        class=""
                        style="flex:1;height:0px;overflow-y:auto"
                        v-loading="menusLoading"
                    >
                        <el-tree
                            :data="menuList"
                            show-checkbox
                            check-strictly
                            node-key="id"
                            ref="tree"
                            :props="defaultProps"
                            default-expand-all
                            :expand-on-click-node="false"
                            check-on-click-node
                        >
                        </el-tree>
                    </div>
                </el-tab-pane>
                <el-tab-pane
                    label="用户列表"
                    name="second"
                >
                    <div
                        class="flex-c"
                        style="flex:1"
                    >
                        <div style="flex:1;height:0px">
                            <vxe-table
                                ref="xTable"
                                border
                                :data="userList"
                                :loading="loading"
                                size="mini"
                                height="auto"
                                auto-resize
                                resizable
                                stripe
                            >
                                <vxe-table-column
                                    type="seq"
                                    width="50"
                                    title="序号"
                                >
                                </vxe-table-column>
                                <vxe-table-column
                                    field="name"
                                    title="用户名"
                                    min-width="120"
                                ></vxe-table-column>
                                <vxe-table-column
                                    field="loginName"
                                    title="登录名"
                                    min-width="120"
                                ></vxe-table-column>
                            </vxe-table>
                        </div>
                        <el-pagination
                            class="p10"
                            :current-page.sync="page2"
                            :page-size="10"
                            align="right"
                            background
                            layout="total, prev, pager, next"
                            :total="total2"
                            @current-change="handleCurrentChange2"
                        />
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog
            :title="title"
            :visible.sync="sourceVisible"
            width="500px"
            :close-on-click-modal='false'
        >
            <el-form
                :model="ruleForm"
                :rules="rules"
                ref="ruleForm"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="角色名"
                    prop="name"
                >
                    <el-input
                        v-model="ruleForm.name"
                        style="width:250px"
                    ></el-input>
                </el-form-item>

                <el-form-item
                    label="描述"
                    prop="remark"
                >
                    <el-input
                        v-model="ruleForm.remark"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="sourceVisible=false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>

import { getRoleList, getMenuList, addRole, editRole, delRole, getRoleDetail, getCurrentRoleList, saveRoleList, getRoleUserList } from '_api/system/systemApi'
export default {
    name: 'RolesManager',
    data () {
        return {
            defaultProps: {
                children: 'menus'
            },
            //角色列表
            list: [],
            page: 1,
            total: 0,
            loading: false,

            //用户分页
            page2: 1,
            total2: 0,

            //目录权限资源
            menuList: [],
            menusLoading: false,
            activeName: 'first',
            //层标题
            title: '',
            //树形新增点击数据
            hanldObj: {},
            //model资源编辑层
            sourceVisible: false,
            //form
            objRuleForm: {

            },
            ruleForm: {
                name: '',//角色名
                remark: ''//描述
            },
            //
            rules: {
                name: [
                    { required: true, message: '名称不能为空', trigger: 'blur' }
                ]
            },

            //定位新增，编辑当前角色高亮
            editRowIndex: '',
            //当前选中的角色id
            selectId: '',
            //用户列表
            userList: []
        }
    },

    created () {
        this.objRuleForm = { ...this.ruleForm };
        this.getList();
    },
    methods: {
        //获取角色列表
        async getList () {
            this.loading = true;
            this.resetData();
            let req = {
                pn: this.page
            }
            let { code, data } = await getRoleList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || [];
                this.total = data.total;
                this.setHightRow();
            }
        },
        resetData () {
            this.menuList = [];
            this.selectId = [];
        },
        //新增角色
        addRolsFun () {
            this.title = '新增角色';            
            this.sourceVisible = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.objRuleForm };
                Reflect.deleteProperty(this.ruleForm, "id");
            })            
        },
        //编辑角色
        async editFun (id, index) {
            this.editRowIndex = index;
            this.title = '编辑角色';
            let { data, code } = await getRoleDetail({ id });
            if (code == 200) {
                this.sourceVisible = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data || {};
                })               
            }
        },
        //删除角色
        delFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { data, code } = await delRole({ id });
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })
        },

        //高亮编辑的角色
        setHightRow () {
            if (this.editRowIndex || this.editRowIndex === 0) {
                this.$refs.xTable.setCurrentRow(this.list[this.editRowIndex]);
                this.selectTableData({ row: this.list[this.editRowIndex], rowIndex: this.editRowIndex });
            }
        },

        //获取资源列表
        async getMenuList () {
            this.menusLoading = true;
            this.menuList = [];
            let { data, code } = await getMenuList();
            this.menusLoading = false;
            if (code == 200) {
                this.menuList = data || [];
            }
        },
        async getRoleMenuAndBtn (id) {
            let { data, code } = await getCurrentRoleList({ id })
            if (code == 200) {
                let ids = (data || []).filter(item => item.id).map(item => item.id);
                this.$nextTick(() => {
                    this.$refs.tree.setCheckedKeys(ids);
                })
            }
        },
        async saveAuth () {
            if (this.selectId && this.selectId != "") {
                let req = {
                    'roleId': this.selectId,
                    'menuIds': this.$refs.tree.getCheckedKeys()
                }
                this.$message.warning('保存中...');
                let { data, code } = await saveRoleList(req);
                if (code == 200) {
                    this.$message.success("保存成功");
                    this.getList();
                }
            } else {
                this.$message.error("请选择要分配权限的角色");
            }
        },



        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty("id")) {
                        let { data, code } = await editRole({ ...this.ruleForm });
                        if (code == 200) {
                            this.$message.success("修改成功");
                            this.sourceVisible = false;
                            this.getList();
                        }
                    } else {
                        this.editRowIndex = this.list.length;
                        let { data, code } = await addRole({ ...this.ruleForm });
                        if (code == 200) {
                            this.$message.success("保存成功");
                            this.sourceVisible = false;
                            this.getList();
                        }
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },

        //角色点击
        selectTableData ({ row, rowIndex }) {
            this.activeName = 'first';
            this.getMenuList();
            this.selectId = row.id;
            this.editRowIndex = rowIndex;
            this.$nextTick(() => {
                this.getRoleMenuAndBtn(row.id);
                this.getUserListFun();
            })
        },

        //角色分页
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },

        //用户分页
        handleCurrentChange2 (p) {
            this.page2 = p;
            this.getUserListFun();
        },

        //根据角色Id获取用户列表
        async getUserListFun () {
            let req = {
                id: this.selectId,
                pn: this.page2
            }
            let { data, code } = await getRoleUserList(req);
            if (code == 200) {
                this.userList = data.list || [];
                this.total2 = data.total;
            }
        }
    }
}
</script>

<style>
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.span-mark {
    display: inline-block;
    width: 200px;
}
.roles-r {
    border: 1px solid #ddd;
    margin-left: 10px;
}
.roles-r * {
    font-size: 12px;
}

.roles-r .el-tabs__content {
    flex: 1;
    height: 0px;
}
.roles-r .el-tabs__content .el-tab-pane {
    height: 100%;
    display: flex;
    flex-flow: column;
}
.roles-r .el-tabs__header {
    margin-bottom: 0px;
}
.roles-r .el-tabs__header .el-tabs__nav-wrap {
    padding-left: 10px;
}
.roles-r .el-tabs__header .el-tabs__item {
    line-height: 34px;
    height: 34px;
}

.vxe-table--render-default .vxe-body--row.row--current {
    background-color: #aadef7;
}
</style>
