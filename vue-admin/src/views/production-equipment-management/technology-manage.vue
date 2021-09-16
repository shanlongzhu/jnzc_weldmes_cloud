<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-plus"
                    @click="addFun"
                    v-has="'addGYK'"
                >新增工艺库</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-add"
                    @click="commandIssueFun"
                    v-has="'commandIssue'"
                >控制命令下发</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-remove"
                    @click="passwordIssueFun"
                    v-has="'passwordIssue'"
                >密码下发</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-printer"
                    @click="setLockFun(1)"
                    v-has="'setLock'"
                >松下焊机通道锁定</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    @click="setLockFun(0)"
                    v-has="'setLock'"
                >松下焊机通道解锁</el-button>
            </div>
        </div>
        <div
            class="table-con"
            style="flex:1;height:0px"
        >

            <vxe-table
                border
                :data="list"
                size="mini"
                height="auto"
                :loading="loading"
                auto-resize
                :expand-config="{toggleMethod: expandChange}"
            >
                <vxe-table-column
                    type="expand"
                    width="60"
                >
                    <template #content="{ row }">
                        <expandTableCo2
                            v-if="row.sysDictionary.value==5"
                            :id="row.id"
                            :modelType="row.sysDictionary.value"
                            @editDetail="editDetailFunCo2"
                            @reload="getList"
                        ></expandTableCo2>
                        <expandTableTIG
                            v-else-if="row.sysDictionary.value==4"
                            :id="row.id"
                            :modelType="row.sysDictionary.value"
                            @editDetail="editDetailFunTIG"
                            @reload="getList"
                        ></expandTableTIG>
                        <expandTableAT3
                            v-else-if="row.sysDictionary.value==6"
                            :id="row.id"
                            :modelType="row.sysDictionary.value"
                            @editDetail="editDetailFunAT3"
                            @reload="getList"
                        >
                        </expandTableAT3>
                        <expandTableFR2
                            v-else-if="row.sysDictionary.value==7"
                            :id="row.id"
                            :modelType="row.sysDictionary.value"
                            @editDetail="editDetailFunFR2"
                            @reload="getList"
                        ></expandTableFR2>
                        <expand-table
                            v-else
                            :id="row.id"
                            :modelType="row.sysDictionary.value"
                            @editDetail="editDetailFun"
                            @reload="getList"
                        ></expand-table>
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="wpsName"
                    title="工艺名称"
                >
                    <template slot="header">
                        <span>工艺名称</span><span class="red-star">*</span>
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="weldModel"
                    title="焊机型号"
                >
                    <template #default={row}>
                        {{row.sysDictionary.valueName}}
                    </template>
                    <template slot="header">
                        <span>焊机型号</span><span class="red-star">*</span>
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="createTime"
                    title="创建日期"
                ></vxe-table-column>
                <vxe-table-column
                    field="createTime"
                    title="操作"
                    width="350"
                >
                    <template #default="{row}">
                        <el-button
                            size="mini"
                            type="warning"
                            plain
                            @click="issueOrdersFun(row)"
                            v-has="'issueOrders'"
                        >
                            工艺库下发
                        </el-button>
                        <el-button
                            size="mini"
                            type="success"
                            plain
                            @click="addLibraryFun(row)"
                            v-has="'addGY'"
                        >
                            新增工艺
                        </el-button>
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="editFun(row.id)"
                            v-has="'editGYK'"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delFun(row.id)"
                            v-has="'delGYK'"
                        >
                            删除
                        </el-button>
                    </template>
                </vxe-table-column>
            </vxe-table>
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

        <!-- 新增/修改 工艺库-->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            :close-on-click-modal="false"
            width="600px"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="120px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="工艺库名称"
                    prop="wpsName"
                >
                    <el-input
                        style="width:250px"
                        v-model="ruleForm.wpsName"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="厂家"
                    prop="firmId"
                >
                    <el-select
                        v-model="ruleForm.firmId"
                        placeholder="请选择"
                        style="width:250px"
                        @change="changeFirm"
                    >
                        <el-option
                            v-for="item in manufactorArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="设备型号"
                    prop="weldModel"
                >
                    <el-select
                        v-model="ruleForm.weldModel"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in modelArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item
                    class="mt10 tc"
                    label-width="0"
                >
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="visable1 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 工艺明细 -->
        <add-tech ref="addTech"></add-tech>
        <!-- 松下 工艺新增 -->
        <addSxCO2 ref="SxCO2"></addSxCO2>
        <addSxTIG ref="SxTIG"></addSxTIG>
        <addSxAT3 ref="SxAT3"></addSxAT3>
        <addSxFR2 ref="SxFR2"></addSxFR2>

        <!-- 工艺库下发 -->
        <issue-orders ref="issueOrdersRef"></issue-orders>
        <issueOrdersCo2 ref="issueOrdersCo2"></issueOrdersCo2>
        <issueOrdersTIG ref="issueOrdersTIG"></issueOrdersTIG>
        <issueOrdersAT3 ref="issueOrdersAT3"></issueOrdersAT3>
        <issueOrdersFR2 ref="issueOrdersFR2"></issueOrdersFR2>

        <!-- 密码下发 -->
        <passwordIssue ref="passwordIssue"></passwordIssue>
        <!-- 控制命令下发 -->
        <commandIssue ref="commandIssue"></commandIssue>
        <!-- 通道解锁/锁定 -->
        <setLock ref="setLock"></setLock>
    </div>
</template>

<script>
import { getDictionaries, getProcesLibraryList, getProcesLibraryDetail, delProcesLibrary, editProcesLibrary, addProcesLibrary, addProcesLibraryChild, editProcesLibraryChild } from '_api/productionProcess/process'
import { getWeldingModel } from '_api/productionEquipment/production'
import expandTable from './components/expandTable.vue';
//GL5-Co2展开表格
import expandTableCo2 from './components/sxGL5/CO2/expandTableCo2'
//GL5-TIG展开表格
import expandTableTIG from './components/sxGL5/TIG/expandTableTIG'
//AT3展开表格
import expandTableAT3 from './components/sxAT3/expandTableAT3'
//FR2展开表格
import expandTableFR2 from './components/FR2/expandTableFR2'

//新增工艺
import AddTech from './components/addTech.vue';
import addSxTIG from './components/sxGL5/TIG/addSxTIG'
import addSxCO2 from './components/sxGL5/CO2/addSxCO2'
import addSxAT3 from './components/sxAT3/addSxAT3'
import addSxFR2 from './components/FR2/addSxFR2'

//工艺下发
import IssueOrders from './components/issueOrders.vue';
import issueOrdersCo2 from './components/sxGL5/CO2/issueOrdersCo2';
import issueOrdersTIG from './components/sxGL5/TIG/issueOrdersTIG';
import issueOrdersAT3 from './components/sxAT3/issueOrdersAT3';
import issueOrdersFR2 from './components/FR2/issueOrdersFR2';

// 密码下发
import passwordIssue from './components/passwordIssue/passwordIssue';
//控制命令下发
import commandIssue from './components/commandIssue/commandIssue'
//设置通道锁定/解锁
import setLock from './components/sxGL5/Lock/setLock'

export default {
    components: {
        expandTable,
        AddTech,
        IssueOrders,
        addSxCO2,
        addSxTIG,
        expandTableCo2,
        expandTableTIG,
        issueOrdersCo2,
        issueOrdersTIG,
        addSxAT3,
        expandTableAT3,
        issueOrdersAT3,
        addSxFR2,
        expandTableFR2,
        issueOrdersFR2,
        passwordIssue,
        commandIssue,
        setLock
    },
    data () {
        return {
            visable1: false,
            title: '新增工艺',
            ruleFormObj: {
            },
            ruleForm: {
                wpsName: '',//工艺库名称
                weldModel: '',//设备型号
                firmId: ''
            },
            rules: {
                welderNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                firmId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldModel: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ]
            },
            //设备型号
            modelArr: [],
            //厂家
            manufactorArr: [],
            list: [],
            page: 1,
            total: 0,
            pageSize: 10,
            loading: false,

            //
            mqttClient: {},
            messageObj: ''
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        this.getDicFun();
        this.getList();
    },
    mounted () {
    },
    methods: {
        async issueOrdersFun (row) {
            switch (row.sysDictionary.value) {
                case '5'://co2
                    this.$refs.issueOrdersCo2.init(row);
                    break;
                case '4'://tig
                    this.$refs.issueOrdersTIG.init(row);
                    break;
                case '6'://at3
                    this.$refs.issueOrdersAT3.init(row);
                    break;
                case '7'://fr2
                    this.$refs.issueOrdersFR2.init(row);
                    break;
                default://cpev500
                    this.$refs.issueOrdersRef.init(row);
                    break;
            }
        },

        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["5"] });
            if (code == 200) {
                this.manufactorArr = data['5'] || [];
            }
        },

        search () {
            this.page = 1;
            this.getList();
        },
        //获取列表
        async getList () {
            let req = {
                pn: this.page,
                size: this.pageSize,
            }
            this.loading = true;
            let { data, code } = await getProcesLibraryList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || []
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新增工艺库"
            this.visable1 = true;
            this.modelArr = [];
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })
        },

        //根据厂家获取设备型号
        changeFirm (id) {
            this.ruleForm.weldModel = "";
            this.getWeldingModel(id);
        },
        async getWeldingModel (id) {
            let { data, code } = await getWeldingModel({ id });
            if (code == 200) {
                this.modelArr = data || [];
            }
        },
        //获取工艺库明细
        async editFun (id) {
            this.title = "修改工艺库"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getProcesLibraryDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {};
                    this.getWeldingModel(this.ruleForm.firmId);
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
                let { code, data } = await delProcesLibrary({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })

        },

        //展开表格行
        expandChange ({ row, expanded }) {
            return true
        },


        handleCurrentChange (pn) {
            this.page = pn;
            this.getList();
        },

        handleSizeChange (s) {
            this.pageSize = s;
            this.getList();
        },

        // 新增/编辑提交工艺库
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        const { data, code } = await editProcesLibrary(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        const { data, code } = await addProcesLibrary(req);
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

        //新增工艺
        addLibraryFun (row) {
            console.log(row)
            switch (row.sysDictionary.value) {
                case '5'://co2工艺新增
                    this.$refs.SxCO2.addLibraryFun(row.id);
                    break;
                case '4'://tig工艺新增
                    this.$refs.SxTIG.addLibraryFun(row.id);
                    break;
                case '6'://at3工艺新增
                    this.$refs.SxAT3.addLibraryFun(row.id);
                    break;
                case '7'://FR2工艺新增
                    this.$refs.SxFR2.addLibraryFun(row.id);
                    break;
                default://cpev500工艺新增
                    this.$refs.addTech.addLibraryFun(row.id);
                    break;
            }
        },


        //展开列表调用修改工艺
        async editDetailFun (obj) {
            this.$refs.addTech.editDetailFun(obj);
        },
        async editDetailFunCo2 (obj) {
            this.$refs.SxCO2.editDetailFun(obj);
        },
        async editDetailFunTIG (obj) {
            this.$refs.SxTIG.editDetailFun(obj);
        },
        async editDetailFunAT3 (obj) {
            this.$refs.SxAT3.editDetailFun(obj);
        },
        async editDetailFunFR2 (obj) {
            this.$refs.SxFR2.editDetailFun(obj);
        },


        //密码下发
        passwordIssueFun(){
            this.$refs.passwordIssue.init();
        },
        //控制命令下发
        commandIssueFun(){
            this.$refs.commandIssue.init();
        },
        // 通道解锁/锁定
        setLockFun(v){
            this.$refs.setLock.init(v);
        }

    }
}
</script>

<style>
.procces-wrap .el-input-number .el-input__inner {
    text-align: left;
}
.procces-wrap .el-form-item {
    margin-bottom: 0px;
}
.procces-wrap .el-form-item * {
    font-size: 12px;
}
</style>
