<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>所属作业区：</span>
                <el-select
                    size="small"
                    class="w150"
                    v-model="ruleForm.deptName"
                    placeholder="请选择"
                >
                    <el-option
                        label="菜单"
                        value="1"
                    ></el-option>
                    <el-option
                        label="按钮"
                        value="2"
                    ></el-option>
                </el-select>
            </div>
            <div class="con-w">
                <span>所属班组：</span>
                <el-select
                    size="small"
                    class="w150"
                    v-model="ruleForm.deptName"
                    placeholder="请选择"
                >
                    <el-option
                        label="菜单"
                        value="1"
                    ></el-option>
                    <el-option
                        label="按钮"
                        value="2"
                    ></el-option>
                </el-select>
            </div>
            <div class="con-w">
                <span>任务状态：</span>
                <el-select
                    size="small"
                    class="w150"
                    v-model="ruleForm.deptName"
                    placeholder="请选择"
                >
                    <el-option
                        label="菜单"
                        value="1"
                    ></el-option>
                    <el-option
                        label="按钮"
                        value="2"
                    ></el-option>
                </el-select>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-search"
                    type="primary"
                >搜索</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-plus"
                    @click="addFun"
                >新增</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-add"
                >导入</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-remove"
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
                >批量完成</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    type="danger"
                    plain
                    @click="delAll"
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
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                @selection-change="handleSelectionChange"
            >
                <el-table-column
                    type="selection"
                    width="40"
                >
                </el-table-column>
                <el-table-column
                    prop="taskNo"
                    label="任务编号"
                    align="center"
                    width="70"
                    fixed="left"
                />
                <el-table-column
                    prop="gradeIdToStr"
                    label="任务等级"
                    align="center"
                    width="130"
                    show-overflow-tooltip
                />
                <el-table-column
                    prop="deptName"
                    label="所属班组"
                    align="center"
                    width="130"
                />
                <el-table-column
                    prop="welderName"
                    label="焊工姓名"
                    align="center"
                    width="130"
                />
                <el-table-column
                    prop="planStarttime"
                    label="计划开始时间"
                    align="center"
                    width="140"
                />
                <el-table-column
                    prop="realityStarttime"
                    label="实际开始时间"
                    align="center"
                    width="140"
                />
                <el-table-column
                    prop="planEndtime"
                    label="计划结束时间"
                    align="center"
                    width="140"
                />
                <el-table-column
                    prop="realityEndtime"
                    label="实际结束时间"
                    align="center"
                    width="140"
                />
                <el-table-column
                    prop="evaluateContent"
                    label="任务评价"
                    align="center"
                    width="150"
                />
                <el-table-column
                    prop="evaluateStarsIdToStr"
                    label="评价等级"
                    align="center"
                    width="120"
                />
                <el-table-column
                    label="操 作"
                    align="center"
                    fixed="right"
                    width="300"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="editFun(scope.row.id)"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delProcessFun(scope.row.id)"
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
                        >
                            评价
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-pagination
            :current-page.sync="currentPage1"
            :page-size="10"
            align="right"
            class="p10"
            layout="total, prev, pager, next"
            :total="total"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />

        <!-- 新增/修改 -->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
        >
            <el-form
                :model="ruleForm"
                :rules="rules"
                ref="ruleForm"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="任务编号"
                    prop="taskNo"
                >
                    <el-input v-model="ruleForm.taskNo"></el-input>
                </el-form-item>
                <el-form-item
                    label="任务等级"
                    prop="grade"
                >
                    <el-select
                        v-model="ruleForm.grade"
                        placeholder="请选择"
                    >
                        <el-option
                            :label="item.valueName"
                            :value="item.id"
                            :key="item.id"
                            v-for="item in levelArr"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="所属班组"
                    prop="deptId"
                >
                    <el-select
                        v-model="ruleForm.deptId"
                        placeholder="请选择活动区域"
                    >
                        <el-option
                            label="菜单"
                            value="1"
                        ></el-option>
                        <el-option
                            label="按钮"
                            value="2"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="计划时间"
                    prop="planStarttime"
                >
                    <el-date-picker
                        v-model="dateTime"
                        @change="changeTime"
                        style="width:98%"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    >
                    </el-date-picker>
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
import { getInfo } from '../../api/user'
import { getProcessList, editProcess, getlevel, getProcessDetail, delProcess, addProcess, delCheckAllProcess, changeAllStatus, changeStatus } from '_api/productionProcess/process'
export default {
    data () {
        return {
            //搜索条件
            visable1: false,
            ruleFormObj: {
                id: '',
                taskNo: '',
                gradeIdToStr: '',
                grade: '',
                deptName: '',
                deptId: '',
                planStarttime: '',
                planEndtime: ''
            },
            ruleForm: {
                id: '',
                taskNo: '',
                gradeIdToStr: '',
                grade: '',
                deptName: '',
                deptId: '',
                planStarttime: '',
                planEndtime: ''
            },
            rules: {
                taskNo: [
                    { required: true, message: '任务编号不能为空', trigger: 'blur' }
                ],
                deptId: [
                    { required: true, message: '班组不能为空', trigger: 'blur' }
                ],
            },
            title: "修改任务",
            dateTime: '',
            //任务等级下拉数据
            levelArr: [],
            //分页
            page: 1,
            selectTableAll: [],






            role: null,
            showButtonDel: true,
            showButtonEdit: true,
            showButtonComment: true,
            list: [],
            total: 0,
            listLoading: true,
            formData: {
                taskNo: '',
                gradeIdToStr: '',
                deptName: '',
                planStarttime: '',
                planEndtime: '',
                realityStarttime: '',
                realityEndtime: '',
                evaluateContent: '',
                evaluateStarsIdToStr: ''
            }
        }
    },

    created () {
        this.getList()
        this.getUserRoles()
        if (this.levelArr.length == 0) {
            this.getLevelFun();
        }
    },
    methods: {
        getList () {
            let req = {
                pn: this.page
            }
            getProcessList(req).then(res => {
                this.list = res.data.list
                this.total = res.data.total
            })
        },
        getUserRoles () {
            getInfo().then(res => {
                console.log(res.data)
                this.role = res.data.role
                // if (this.role[0] !== 'admin') {
                //   this.showButtonDel = false
                // }
            })
        },
        handleSizeChange (val) {
            console.log(`每页 ${val} 条`)
        },
        handleCurrentChange (pn) {
            this.page = pn;
            this.getList();
        },
        changeTime (v) {
            if (v && v[0]) {
                this.ruleForm.planStarttime = v[0];
                this.ruleForm.planEndtime = v[1];
            }
        },
        // 修改信息
        async editFun (id) {
            this.visable1 = true;
            this.title = "修改任务"
            let { data, code } = await getProcessDetail({ id });
            if (code == 200) {
                this.ruleForm = { ...data };
                this.dateTime = [this.ruleForm.planStarttime || '', this.ruleForm.planEndtime || '']
            }
        },
        addFun () {
            this.title = "新增任务"
            this.visable1 = true;
            this.ruleForm = { ...this.ruleFormObj };
            Reflect.deleteProperty(this.ruleForm, 'id');
        },
        //获取任务等级下来菜单
        async getLevelFun () {
            let { code, data } = await getlevel();
            if (code == 200) {
                this.levelArr = data.taskRateList || [];
            }
        },

        //表格复选框勾选数据
        handleSelectionChange (v) {
            this.selectTableAll = v;
        },

        //删除任务
        delProcessFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { data, code } = await delProcess({ id });
                if (code == 200) {
                    this.$message.success("操作成功");
                    this.getList();
                }
            }).catch(() => { });
        },

        //批量删除
        delAll () {
            this.$confirm('确定要删除所选内容吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let idList = this.selectTableAll.map(item => item.id);
                let { code } = await delCheckAllProcess({ idList });
                if (code == 200) {
                    this.$message.success("操作成功");
                    this.selectTableAll = [];
                    this.getList();
                }
            }).catch(() => { });
        },

        //批量修改状态
        changeAllStatus () {
            this.$confirm('确定要修改所选内容吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let filterArr = this.selectTableAll.filter(item => item.statusStr != '进行中');
                if (filterArr.length > 0) {
                    return this.$message.error("只能选择进行中的数据");
                }
                let idList = this.selectTableAll.map(item => item.id);
                let { code } = await changeAllStatus({ idList });
                if (code == 200) {
                    this.$message.success("操作成功");
                    this.selectTableAll = [];
                    this.getList();
                }
            }).catch(() => { });
        },

        //修改状态
        isEnd (id) {
            this.$confirm('确定要完成吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let req = { id: id, statusStr: '进行中' }
                let { code } = await changeStatus(req);
                if (code == 200) {
                    this.$message.success("操作成功");
                    this.getList();
                }
            }).catch(() => { });
        },



        //新增/编辑提交
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        let { data, code } = await editProcess(this.ruleForm);
                        if (code == 200) {
                            this.$message.success("修改成功");
                            this.visable1 = false;
                            this.getList();
                        }
                    } else {
                        let { data, code } = await addProcess(this.ruleForm);
                        if (code == 200) {
                            this.$message.success("新增成功");
                            this.visable1 = false;
                            this.getList();
                        }
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
    }
}
</script>

<style scoped>
</style>
