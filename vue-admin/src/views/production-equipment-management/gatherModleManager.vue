<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>编号：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="gatherNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>所属项目：</span>
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
                    v-has="'import'"
                    :action="importUrl"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers"
                    accept=".xlsx"
                >
                    <el-button
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
                    @click="exportExcelFun"
                >导出</el-button>
            </div>

        </div>
        <div
            class="table-con"
            style="flex:1;height:0px"
        >
            <el-table
                id="out-table"
                :data="list"
                stripe
                style="width: 100%"
                align="center"
                v-loading='loading'
                border
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
            >
                <el-table-column
                    label=""
                    align="left"
                    min-width="200"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="gatherNo"
                    label="编号"
                    align="left"
                    min-width="70"
                    fixed="left"
                />
                <el-table-column
                    prop="deptName"
                    label="所属项目"
                    align="left"
                    min-width="100"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDept.name}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="welderName"
                    label="状态"
                    align="left"
                    min-width="100"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueName}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="welderName"
                    label="通讯协议"
                    align="left"
                    min-width="100"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNames}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ipPath"
                    label="IP地址"
                    align="left"
                    min-width="100"
                />
                <el-table-column
                    prop="macPath"
                    label="MAC地址"
                    align="left"
                    min-width="100"
                />
                <el-table-column
                    prop="createTime"
                    label="出厂时间"
                    align="left"
                    min-width="170"
                />
                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    width="150"
                    fixed="right"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            v-has="'edit'"
                            @click="editFun(scope.row.id)"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            v-has="'del'"
                            @click="delFun(scope.row.id)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
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
                    label="编号"
                    prop="gatherNo"
                >
                    <el-input
                        v-model="ruleForm.gatherNo"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="所属项目"
                    prop="deptId"
                >
                    <el-cascader
                        v-model="ruleForm.deptId"
                        size="small"
                        style="width:250px"
                        clearable
                        :options="teamArr"
                        :props="defalutProps"
                        :show-all-levels="false"
                    />
                </el-form-item>
                <el-form-item
                    label="状态"
                    prop="status"
                >
                    <el-select
                        v-model="ruleForm.status"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in statusArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="通讯协议"
                    prop="protocol"
                >
                    <el-select
                        v-model="ruleForm.protocol"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in protocolArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="IP地址"
                    prop="ipPath"
                >
                    <el-input
                        v-model="ruleForm.ipPath"
                        style="width:250px"
                    />
                </el-form-item>
                <el-form-item
                    label="MAC地址"
                    prop="macPath"
                >
                    <el-input
                        v-model="ruleForm.macPath"
                        style="width:250px"
                    />
                </el-form-item>
                <el-form-item
                    label="出厂时间"
                    prop="createTime"
                >
                    <el-date-picker
                        v-model="ruleForm.createTime"
                        style="width:250px"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetime"
                        placeholder="选择日期时间"
                    />
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
import { getEquList, getEquDetail, delEqu, exportExcel, addEqu, editEqu } from '_api/productionEquipment/production'
import { getTeam, getDictionaries } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
export default {
    name: 'gatherModleManager',
    data () {
        // 验证采集编号的规则
        var checkGatherNo = (rule, value, callback) => {
            // 验证集编号的正则表达式
            const regGatherNo = /^\d{4}$/
            if (regGatherNo.test(value)) {
                return callback()
            }
            callback(new Error('请输入四位纯数字'))
        };
        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            grade: '',//编号
            gatherNo: '',//所属项目

            visable1: false,
            ruleFormObj: {
            },
            ruleForm: {
                gatherNo: '',//编号
                status: '',//状态
                deptId: '',//项目（机构id）
                ipPath: '',//ip
                macPath: '',//mac
                protocol: '',//通讯协议
                createTime: ''//出厂时间
            },
            rules: {
                gatherNo: [
                    { required: true, message: '不能为空', trigger: 'blur' },
                    { validator: checkGatherNo, trigger: 'blur' }
                ],
                deptId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                status: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                protocol: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ]
            },
            title: '修改任务',
            dateTime: '',

            //机构数据
            teamArr: [],
            //状态下拉数据
            statusArr: [],
            //通讯协议下来数据
            protocolArr: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            loading: false,
            importUrl: `${process.env.VUE_APP_BASE_API}/collection/importExcel`,
            headers: {
                'Authorization': getToken()
            }
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        // 获取班组
        if (this.teamArr.length == 0) {
            this.getTeamList()
        }
        this.getList();
        this.getDicFun();
    },
    methods: {
        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["1", "2"] });
            if (code == 200) {
                this.statusArr = data['1'] || [];
                this.protocolArr = data['2'] || [];
            }
        },
        search () {
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                pn: this.page,
                grade: this.grade && this.grade.length > 0 ? this.grade.slice(-1).join('') : '',
                gatherNo: this.gatherNo
            }
            this.loading = true;
            let { data, code } = await getEquList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新建采集模块"
            this.visable1 = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })

        },
        //修改
        async editFun (id) {
            this.title = "修改采集模块"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getEquDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {}
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
                let { code, data } = await delEqu({ id })
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
        //导出
        exportExcelFun () {
            this.$message({
                showClose: true,
                message: '导出中...',
                type: 'success',
                duration: 1000
            });
            let req = {
                grade: this.grade && this.grade.length > 0 ? this.grade.slice(-1).join('') : '',
                gatherNo: this.gatherNo
            }
            location.href = exportExcel(req);
        },
        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        },
        // 新增/编辑提交
        submitForm (formName) {
            console.log(this.ruleForm)
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await editEqu(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await addEqu(req);
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

    }
}
</script>

<style scoped>
</style>
