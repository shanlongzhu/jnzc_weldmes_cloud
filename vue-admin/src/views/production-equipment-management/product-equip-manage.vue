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
                    v-model="searchObj.machineNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>类型：</span>
                <el-select
                    v-model="searchObj.type"
                    size="small"
                    class="w120"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in typeArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>所属项目：</span>
                <el-cascader
                    v-model="searchObj.grade"
                    size="small"
                    class="w150"
                    clearable
                    :options="teamArr"
                    :props="defalutProps"
                    :show-all-levels="false"
                />
            </div>
            <div class="con-w">
                <span>状态：</span>
                <el-select
                    v-model="searchObj.status"
                    size="small"
                    class="w100"
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
                <span>厂家：</span>
                <el-select
                    v-model="searchObj.firm"
                    size="small"
                    class="w120"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in manufactorArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>是否在网：</span>
                <el-select
                    v-model="searchObj.isNetwork"
                    size="small"
                    class="w100"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        label="是"
                        value="0"
                    />
                    <el-option
                        label="否"
                        value="1"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>采集序号：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.gatherNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>IP：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.ipPath"
                ></el-input>
            </div>
            <div class="con-w">
                <span>设备型号：</span>
                <el-select
                    v-model="searchObj.model"
                    size="small"
                    class="w150"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in modelArr"
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
                >新增</el-button>
            </div>
            <div class="con-w">
                <el-upload
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
                border
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
            >
                <el-table-column
                    label=""
                    align="left"
                    min-width="100"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="machineNo"
                    label="固定资产编号"
                    align="left"
                    min-width="100"
                    fixed="left"
                />
                <el-table-column
                    prop="deptName"
                    label="设备类型"
                    align="left"
                    min-width="80"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueName}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="入厂时间"
                    align="left"
                    min-width="150"
                >
                </el-table-column>
                <el-table-column
                    prop="welderName"
                    label="所属项目"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDept.name}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ipPath"
                    label="状态"
                    align="left"
                    min-width="60"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNames}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="macPath"
                    label="厂家"
                    align="left"
                    min-width="100"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNamess}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="是否在网"
                    align="left"
                    min-width="70"
                >
                    <template slot-scope="scope">
                        {{scope.row.isNetwork?'否':'是'}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gatherNo"
                    label="采集序号"
                    align="left"
                    min-width="100"
                >
                    <template slot-scope="scope">
                        {{scope.row.machineGatherInfo.gatherNo}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop=""
                    label="位置"
                    align="left"
                    min-width="100"
                />
                <el-table-column
                    prop="ipPath"
                    label="IP地址"
                    align="left"
                    min-width="120"
                >
                    
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="设备型号"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNamesss}}
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    width="250"
                    fixed="right"
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
                            @click="delFun(scope.row.id)"
                        >
                            删除
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delFun(scope.row.id)"
                        >
                            维修记录
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
            width="600px"
            :close-on-click-modal="false"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="150px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="固定资产编号"
                    prop="machineNo"
                >
                    <el-input
                        v-model="ruleForm.machineNo"
                        style="width:250px"
                    />
                </el-form-item>
                <el-form-item
                    label="设备类型"
                    prop="type"
                >
                    <el-select
                        v-model="ruleForm.type"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in typeArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="入厂时间"
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
                    label="生产厂商"
                    prop="firm"
                >
                    <el-select
                        v-model="ruleForm.firm"
                        placeholder="请选择"
                        style="width:250px"
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
                    label="采集序号"
                    prop="gid"
                >
                    <el-select
                        v-model="ruleForm.gid"
                        placeholder="请选择"
                        style="width:250px"
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
                    label="设备位置"
                    prop="status"
                >
                    <el-input
                        v-model="ruleForm.gatherNo"
                        style="width:250px"
                    />
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
                    label="设备型号"
                    prop="model"
                >
                    <el-select
                        v-model="ruleForm.model"
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
                    label="状态"
                    prop="status"
                >
                    <el-radio-group v-model="ruleForm.status">
                        <el-radio
                            :label="item.id"
                            v-for="item in statusArr"
                            :key="item.id"
                        >{{item.valueName}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item
                    label="是否联网"
                    prop="isNetwork"
                >
                    <el-radio-group v-model="ruleForm.isNetwork">
                        <el-radio :label="0">是</el-radio>
                        <el-radio :label="1">否</el-radio>
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
import { getWelderList, getWelderDetail, delWelder, exportWelderExcel } from '_api/productionEquipment/production'
import { getTeam, getDictionaries } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
export default {
    name: 'product-equip-manage',
    data () {
        return {
            //搜索条件
            searchObj:{
               machineNo:'', 
               type:'',
               grade:'',
               status:'',
               firm:'',
               isNetwork:'',
               gatherNo:'',
               ipPath:'',
               model:''
            },
            list: [],
            //分页
            page: 1,
            total: 0,

            visable1: false,
            ruleFormObj: {
                id: '',
                machineNo: '',//编号
                status: 17,//状态
                type:'',//设备类型
                firm:'',//厂商
                model:'',//设备型号
                deptId: '',//项目（机构id）
                isNetwork: 0,//是否联网
                gid: '',//采集id
                protocol: '',//通讯协议
                createTime: '',//入厂时间
                ipPath: '',//ip
            },
            ruleForm: {
                id: '',
                machineNo: '',//编号
                status: 17,//状态
                type:'',//设备类型
                firm:'',//厂商
                model:'',//设备型号
                deptId: '',//项目（机构id）
                isNetwork: 0,//是否联网
                gid: '',//采集id
                protocol: '',//通讯协议
                createTime: '',//入厂时间
                ipPath: '',//ip
            },
            rules: {
                taskNo: [
                    { required: true, message: '任务编号不能为空', trigger: 'blur' }
                ],
                deptId: [
                    { required: true, message: '班组不能为空', trigger: 'blur' }
                ]
            },
            title: '修改任务',
            dateTime: '',

            //机构数据
            teamArr: [],
            //设备型号
            modelArr: [],
            //状态
            statusArr: [],
            //设备类型
            typeArr: [],
            //厂家
            manufactorArr: [],

            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            importUrl: `${process.env.VUE_APP_BASE_API}/welder/importExcel`,
            headers: {
                'Authorization': getToken()
            }
        }
    },

    created () {
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
            let { data, code } = await getDictionaries({ "types": ["3", "4", "5", "6"] });
            if (code == 200) {
                this.statusArr = data['4'] || [];
                this.manufactorArr = data['5'] || [];
                this.typeArr = data['3'] || [];
                this.modelArr = data['6'] || [];
            }
        },
        search(){
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                page: this.page,
                ...this.searchObj
            }
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''

            let { data, code } = await getWelderList(req);
            if (code == 200) {
                this.list = data.list
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新增焊机设备"
            this.visable1 = true;
        },
        //修改
        async editFun (id) {
            this.title = "修改焊机设备"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getWelderDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.ruleForm = data[0] || {}
            }
        },
        //删除
        delFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { code, data } = await delWelder({ id })
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

        },
        //导出
        exportExcelFun () {
            this.$message({
                showClose: true,
                message: '导出中...',
                type: 'success',
                duration: 1000
            });
            location.href = exportWelderExcel()
        },
        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        }

    }
}
</script>

<style scoped>
</style>
