<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>所属班组：</span>
                <el-cascader
                    v-model="grade"
                    size="small"
                    class="w150"
                    clearable
                    :options="teamArr"
                    :props="defalutProps"
                    :show-all-levels="false"
                />
            </div>
            <div class="con-w">
                <span>任务状态：</span>
                <el-select
                    v-model="taskStatus"
                    size="small"
                    class="w120"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in statusArr"
                        :key="item.id"
                        :label="item.valueName"
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
            <div class="con-w">
                <el-upload
                    :action="importUrl"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers"
                    accept=".xlsx"
                    v-has="'import'"
                >
                    <el-button
                        v-has="'import'"
                        size="small"
                        icon="el-icon-document-add"
                    >导入</el-button>
                </el-upload>

            </div>
            <div class="con-w">
                <el-button
                    v-has="'export'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="exprotExcelFun"
                >导出</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-printer"
                >打印</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    type="success"
                    plain
                    @click="changeAllStatus"
                    v-has="'allStatus'"
                >批量完成</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    type="danger"
                    plain
                    @click="delAll"
                    v-has="'allDel'"
                >批量删除</el-button>
            </div>
        </div>
        <div
            class="table-con"
            style="flex:1;height:0px"
        >
            <el-table
                :data="list"
                stripe
                style="width: 100%"
                align="center"
                border
                size="mini"
                height="100%"
                v-loading='loading'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                @selection-change="handleSelectionChange"
            >
                <el-table-column
                    type="selection"
                    width="40"
                />
                <el-table-column
                    prop="taskNo"
                    label="任务编号"
                    align="left"
                    min-width="160"
                    fixed="left"
                >
                    <template slot="header">
                        <span>任务编号</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gradeIdToStr"
                    label="任务等级"
                    align="left"
                    width="130"
                    show-overflow-tooltip
                >
                    <template slot="header">
                        <span>任务等级</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="deptName"
                    label="所属班组"
                    align="left"
                    width="130"
                >
                    <template slot="header">
                        <span>所属班组</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="welderName"
                    label="焊工编号"
                    align="left"
                    width="130"
                />
                <el-table-column
                    prop="planStarttime"
                    label="计划开始时间"
                    align="left"
                    width="140"
                />
                <el-table-column
                    prop="realityStarttime"
                    label="实际开始时间"
                    align="left"
                    width="140"
                />
                <el-table-column
                    prop="planEndtime"
                    label="计划结束时间"
                    align="left"
                    width="140"
                />
                <el-table-column
                    prop="realityEndtime"
                    label="实际结束时间"
                    align="left"
                    width="140"
                />
                <el-table-column
                    prop="volMax"
                    label="电压上限"
                    align="left"
                    width="100"
                />
                <el-table-column
                    prop="volMin"
                    label="电压下限"
                    align="left"
                    width="100"
                />
                <el-table-column
                    prop="eleMax"
                    label="电流上限"
                    align="left"
                    width="100"
                />
                <el-table-column
                    prop="eleMin"
                    label="电流下限"
                    align="left"
                    width="100"
                />
                <el-table-column
                    prop="evaluateContent"
                    label="任务评价"
                    align="left"
                    width="150"
                />
                <el-table-column
                    prop="evaluateStarsIdToStr"
                    label="评价等级"
                    align="left"
                    width="120"
                />
                <el-table-column
                    prop="craftRemarks"
                    label="工艺备注"
                    align="left"
                    width="150"
                    show-overflow-tooltip
                />
                <el-table-column
                    label="操 作"
                    align="left"
                    fixed="right"
                    width="300"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="editFun(scope.row.id)"
                            v-has="'edit'"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delProcessFun(scope.row.id)"
                            v-has="'del'"
                        >
                            删除
                        </el-button>
                        <el-button
                            v-show="scope.row.statusStr=='未领取'"
                            size="mini"
                            plain
                            disabled
                        >
                            未领取
                        </el-button>
                        <el-button
                            v-show="scope.row.statusStr=='进行中'"
                            size="mini"
                            type="warning"
                            plain
                            @click="isEnd(scope.row.id)"
                            v-has="'isEnd'"
                        >
                            确认完成
                        </el-button>
                        <el-button
                            v-show="scope.row.statusStr=='已完成'"
                            size="mini"
                            type="success"
                            plain
                        >
                            已完成
                        </el-button>
                        <el-button
                            size="mini"
                            type="info"
                            plain
                            @click="evelFun(scope.row)"
                            v-has="'evel'"
                        >
                            评价
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-pagination
            :current-page.sync="page"
            :page-size="pageSize"
            align="right"
            class="p10"
            :page-sizes="[10, 50, 100, 150, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            :total="total"
            background
            @current-change="handleCurrentChange"
        />

        <!-- 新增/修改 -->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            width="700px"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="任务编号"
                            prop="taskNo"
                        >
                            <el-input
                                v-model="ruleForm.taskNo"
                                style="width:200px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="任务等级"
                            prop="grade"
                        >
                            <el-select
                                v-model="ruleForm.grade"
                                placeholder="请选择"
                                style="width:200px"
                                @change="changeSelectGetWelder"
                            >
                                <el-option
                                    v-for="item in levelArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="所属班组"
                            prop="deptId"
                        >
                            <el-cascader
                                v-model="ruleForm.deptId"
                                size="small"
                                style="width:200px"
                                clearable
                                :options="teamArr"
                                :props="defalutProps"
                                :show-all-levels="false"
                                @change="changeSelectGetWelder"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="焊工姓名"
                            prop="welderId"
                        >
                            <el-select
                                v-model="ruleForm.welderId"
                                placeholder="请选择"
                                style="width:200px"
                            >
                                <el-option
                                    v-for="item in welderArr"
                                    :key="item.id"
                                    :label="item.welderName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="电压上限"
                            prop="volMax"
                        >
                            <el-input
                                v-model="ruleForm.volMax"
                                style="width:200px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="电压下限"
                            prop="volMin"
                        >
                            <el-input
                                v-model="ruleForm.volMin"
                                style="width:200px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="电流上限"
                            prop="eleMax"
                        >
                            <el-input
                                v-model="ruleForm.eleMax"
                                style="width:200px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="电流下限"
                            prop="eleMin"
                        >
                            <el-input
                                v-model="ruleForm.eleMin"
                                style="width:200px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="20">
                        <el-form-item
                            label="工艺备注"
                            prop="craftRemarks"
                        >
                            <el-input
                                type="textarea"
                                :rows="2"
                                placeholder="请输入内容"
                                v-model="ruleForm.craftRemarks"
                            >
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="20">
                        <el-form-item
                            label="计划时间"
                            prop="planStarttime"
                        >
                            <el-date-picker
                                v-model="dateTime"
                                style="width:98%"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                @change="changeTime"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item>
                            <el-button
                                type="primary"
                                @click="submitForm('ruleForm')"
                            >保存</el-button>
                            <el-button @click="visable1 = false">取消</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>

        <!-- 评价 -->
        <el-dialog
            title="评价"
            :visible.sync="evaluate"
            width="500px"
        >
            <el-form
                ref="evalForm"
                class="demo-ruleForm"
                :model="evaluateRuleForm"
                :rules="evaluateRules"
                label-width="100px"
            >
                <el-form-item
                    label="评分"
                    prop="star"
                >
                    <div style="padding-top:7px">
                        <el-rate v-model="evaluateRuleForm.star" />
                    </div>
                </el-form-item>
                <el-form-item
                    label="评价内容"
                    prop="comments"
                >
                    <el-input
                        v-model="evaluateRuleForm.comments"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入内容"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitEval('evalForm')"
                    >提交</el-button>
                    <el-button @click="evaluate = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { getInfo } from '../../api/user'
import { getProcessList, editProcess, getlevel, getProcessDetail, delProcess, addProcess, delCheckAllProcess, changeAllStatus, changeStatus, getTeam, setEvaluate, exportExcel, importExcel, getDictionaries, getWelderPeopleListNoPage } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
export default {
    data () {
        return {
            // 搜索条件
            // 状态
            taskStatus: '',
            // 班组
            grade: '',
            visable1: false,
            ruleFormObj: {
            },
            ruleForm: {
                welderId: '',//焊工id
                taskNo: '',
                gradeIdToStr: '',
                grade: '',
                deptName: '',
                deptId: '',
                planStarttime: '',
                planEndtime: '',
                eleMax: '',
                eleMin: '',
                volMax: '',
                volMin: '',
                craftRemarks: ''//工艺备注
            },
            rules: {
                // grade: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                taskNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                deptId: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ]
            },
            title: '修改任务',
            dateTime: '',
            // 任务等级下拉数据
            levelArr: [],
            // 分页
            page: 1,
            total: 0,
            pageSize: 10,
            // 表格勾选数据
            selectTableAll: [],
            // 班组数据
            teamArr: [],
            // 状态数据
            statusArr: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list',
                checkStrictly: true
            },

            // 评价
            evaluate: false,
            evaluateRuleForm: {
                id: '',
                star: 5,
                comments: ''
            },
            evaluateRules: {
                star: [
                    { required: true, message: '请选择评分', trigger: 'blur' }
                ],
                comments: [
                    { required: true, message: '评价内容', trigger: 'blur' }
                ]
            },
            list: [],
            // 导入
            importUrl: importExcel(),
            headers: {
                'Authorization': getToken()
            },
            starArr: [],
            loading: true,
            welderArr: [],

            //mqtt
            newClientMq: {},
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        this.getList()
        if (this.levelArr.length == 0) {
            this.getLevelFun()
        }
        // 获取班组
        if (this.teamArr.length == 0) {
            this.getTeamList()
        }

        if (this.starArr.length == 0) {
            this.getDicFun();
        }

        //mqt连接
        this.newMqtt();
    },
    methods: {

        //mqtt创建
        newMqtt () {
            const PahoMQTT = require('paho-mqtt');
            const name = new Date().getTime() + 'client';
            this.newClientMq = new PahoMQTT.Client(`${process.env.VUE_APP_MQTT_API}`, Number(8083), name);
            this.newClientMq.connect({
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: true,
                useSSL: false,
                onFailure: function (e) {
                    console.log(e);
                },
                reconnect: true,
                onSuccess: (res) => {
                    console.log('连接成功');
                }
            })
        },




        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["15"] });
            if (code == 200) {
                this.starArr = data['15'] || []
            }
        },
        search () {
            this.page = 1
            this.getList()
        },
        async getList () {
            this.loading = true;
            const req = {
                pn: this.page,
                size: this.pageSize,
                taskStatus: this.taskStatus,
                grade: this.grade && this.grade.length > 0 ? this.grade.slice(-1).join('') : ''
            }
            let { data, code } = await getProcessList(req);
            this.loading = false;
            this.list = data.list
            this.total = data.total
        },
        handleCurrentChange (pn) {
            this.page = pn
            this.getList()
        },
        handleSizeChange (s) {
            this.pageSize = s;
            this.getList();
        },
        changeTime (v) {
            if (v && v[0]) {
                this.ruleForm.planStarttime = v[0]
                this.ruleForm.planEndtime = v[1]
            }
        },
        // 获取任务详情
        async editFun (id) {
            this.title = '修改任务'
            const { data, code } = await getProcessDetail({ id })
            if (code == 200) {
                this.visable1 = true
                this.ruleForm = { ...data }
                console.log(this.ruleForm)
                this.dateTime = [this.ruleForm.planStarttime || '', this.ruleForm.planEndtime || '']
                if (this.ruleForm.deptId || this.ruleForm.rate) {
                    this.getWelder();
                }
            }
        },
        // 新增任务
        addFun () {
            this.title = '新增任务'
            this.visable1 = true
            this.dateTime = ''
            this.ruleForm = { ...this.ruleFormObj }
            Reflect.deleteProperty(this.ruleForm, 'id')
        },
        // 获取任务等级下来菜单
        async getLevelFun () {
            const { code, data } = await getlevel()
            if (code == 200) {
                this.levelArr = data.taskRateList || []
            }
        },

        // 表格复选框勾选数据
        handleSelectionChange (v) {
            this.selectTableAll = v
        },

        // 删除任务
        delProcessFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const { data, code } = await delProcess({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })
        },

        // 批量删除
        delAll () {
            if (this.selectTableAll.length == 0) {
                return this.$message.error('请选择数据')
            }
            this.$confirm('确定要删除所选内容吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const idList = this.selectTableAll.map(item => item.id)
                const { code } = await delCheckAllProcess({ idList })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.selectTableAll = []
                    this.getList()
                }
            }).catch(() => { })
        },

        // 批量修改状态
        changeAllStatus () {
            if (this.selectTableAll.length == 0) {
                return this.$message.error('请选择数据')
            }
            const filterArr = this.selectTableAll.filter(item => item.statusStr != '进行中')
            if (filterArr.length > 0) {
                return this.$message.warning('只能选择 确认完成 的数据')
            }
            this.$confirm('确定要完成所选任务吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const idList = this.selectTableAll.map(item => item.id)
                const { code, data } = await changeAllStatus({ idList })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.selectTableAll = []
                    this.getList()
                    this.doSendMsg(data)

                }
            }).catch(() => { })
        },

        // 修改状态
        isEnd (id) {
            this.$confirm('确定要完成吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const req = { id: id, statusStr: '进行中' }
                const { code, data } = await changeStatus(req)
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                    this.doSendMsg(data);
                }
            }).catch(() => { })
        },
        //发送完成状态
        doSendMsg (v) {
            if (v.length > 0) {
                for (let i = 0; i < v.length; i++) {
                    setTimeout(() => {
                        let msg = {}
                        msg['weldIp'] = "";
                        msg['gatherNo'] = "";
                        //1 松下 、0 OTC
                        if (v[i].firmCode == '1') {
                            msg['weldType'] = 1;//设备类型
                            msg['weldIp'] = v[i].ipPath
                        } else
                            if (v[i].firmCode == '0') {
                                msg['weldType'] = 0;//设备类型
                                msg['gatherNo'] = v[i].gatherNo || "";//设备IP
                            }
                        msg['startFlag'] = 1;//开始标记
                        let msgStr = JSON.stringify(msg);
                        this.newClientMq.publish('taskClaimIssue', msgStr, 1)
                    }, 200 * i)
                }
            }
        },

        // 新增/编辑提交
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await editProcess(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await addProcess(req)
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
        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.statusArr = data.taskStatus || []
            this.teamArr = data.workArea || []
        },
        // 评价
        async evelFun (row) {
            this.evaluate = true
            this.evaluateRuleForm.id = row.id;
            this.evaluateRuleForm.comments = row.evaluateContent || "";
            this.evaluateRuleForm.star = !row.evaluateStars ? 5 : this.starArr.filter(item => item.id == row.evaluateStars).map(item => item.value).join('');
        },
        submitEval (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    let req = { ...this.evaluateRuleForm };
                    req.start = this.starArr.filter(item => item.value == req.star).map(item => item.id).join('');

                    const { data, code } = await setEvaluate(req);
                    if (code == 200) {
                        this.evaluate = false;
                        this.$message.success("评价成功");
                        this.getList();
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },
        // 导出
        exprotExcelFun () {
            this.$message({
                showClose: true,
                message: '导出中...',
                type: 'success',
                duration: 1000
            });
            const req = {
                taskStatus: this.taskStatus,
                grade: this.grade && this.grade.length > 0 ? this.grade.slice(-1).join('') : ''
            }
            location.href = exportExcel(req)
        },
        // 导入
        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        },
        beforeAvatarUpload () {

        },
        //获取焊工
        changeSelectGetWelder () {
            this.ruleForm.welderId = "";
            this.getWelder();
        },
        async getWelder () {
            let req = {
                grade: this.ruleForm.deptId && this.ruleForm.deptId.length > 0 ? this.ruleForm.deptId.slice(-1).join('') : this.ruleForm.deptId,
                rate: this.ruleForm.grade
            }
            let { data, code } = await getWelderPeopleListNoPage(req);
            if (code == 200) {
                this.welderArr = data || [];
            }
        }

    }
}
</script>

<style scoped>
</style>
