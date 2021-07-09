<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex"
    >

        <div
            class="roles-l flex-c"
            style="height:100%; flex:1; width:0px"
        >
            <div
                class=""
                style="flex:1;height:0px"
            >
                <vxe-table
                    ref="xTable"
                    border
                    :data="typeList"
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
                        field="typeName"
                        title="类型"
                        min-width="120"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="type"
                        title="值"
                        min-width="120"
                    ></vxe-table-column>
                    <!-- <vxe-table-column
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
                    </vxe-table-column> -->
                </vxe-table>
            </div>

        </div>
        <div
            class="roles-r flex-c"
            style="height:100%; flex:1;width:0px"
        >
            <div
                class="p10"
                style="padding-left:0px"
            >
                <el-button
                    size="mini"
                    plain
                    @click="addRolsFun"
                    v-has="'add'"
                >新增</el-button>
            </div>
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
                        title="名称"
                        min-width="120"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="loginName"
                        title="值"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="loginName"
                        title="类型"
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
                    label="名称"
                    prop="value"
                >
                    <el-input
                        v-model="ruleForm.value"
                        style="width:250px"
                    ></el-input>
                </el-form-item>

                <el-form-item
                    label="值"
                    prop="valueName"
                >
                    <el-input
                        v-model="ruleForm.valueName"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="类型名称"
                    prop="typeName"
                >
                    <el-input
                        disabled
                        v-model="ruleForm.typeName"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="类型标识"
                    prop="type"
                >
                    <el-input
                        disabled
                        v-model="ruleForm.type"
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

import { findTypeList, getRoleUserList, getDicType,addDic,findDicIdInfo,delDic,editDic } from '_api/system/systemApi'
export default {
    name: 'dictionaries',
    data () {
        return {
            defaultProps: {
                children: 'menus'
            },
            //字典类型
            typeList:[],
            selectType:{},

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
                value:'',
                valueName:'',
                typeName: '',//角色名
                type: ''//描述
            },
            //
            rules: {
                value: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                valueName: [
                    { required: true, message: '不能为空', trigger: 'blur' }
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
        this.getDicTypeFun();
    },
    methods: {
        //获取字典类型
        async getDicTypeFun () {
            let { data, code } = await getDicType();
            if (code == 200) {
                this.typeList = (data || []).sort((a, b) => {
                    return b.type - a.type
                });
            }
        },


        //获取字典列表
        async getList () {
            this.loading = true;
            let req = {
                pn: this.page,
                ...this.selectType
            }
            let { code, data } = await findTypeList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || [];
                this.total = data.total;
            }
        },
        resetData () {
        },


        //新增字典
        addRolsFun () {
            if(JSON.stringify(this.selectType)=='{}'){
                return this.$message.error("请先选择字典类型");
            }
            this.title = '新增字典';            
            this.sourceVisible = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.objRuleForm };
                this.ruleForm.typeName = this.selectType.typeName;
                this.ruleForm.type = this.selectType.type;
                Reflect.deleteProperty(this.ruleForm, "id");
            })
        },
        //编辑字典
        async editFun (id, index) {
            this.title = '编辑字典';
            let { data, code } = await findDicIdInfo({ id });
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
                let { data, code } = await delDic({ id });
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

        

        


        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty("id")) {
                        let { data, code } = await editDic({ ...this.ruleForm });
                        if (code == 200) {
                            this.$message.success("修改成功");
                            this.sourceVisible = false;
                            this.getList();
                        }
                    } else {
                        let { data, code } = await addDic({ ...this.ruleForm });
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

        //类型点击
        selectTableData ({ row }) {
            this.selectType = row;
            this.$nextTick(() => {
                this.getList();
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
