<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>任务编号：</span>
                <el-input v-model="taskNo" placeholder="" class="w150"></el-input>
            </div>
            <div class="con-w">
                <span>班组：</span>
                <el-cascader
                    v-model="deptId"
                    size="small"
                    class="w150"
                    clearable
                    :options="teamArr"
                    :props="defalutProps"
                    :show-all-levels="false"
                />
            </div>
            <div class="con-w">
                <span>时间：</span>
                <el-date-picker
                        style="width:200px"
                        size="small"
                        v-model="startTime"
                        :clearable="false"
                        type="datetime"
                        placeholder="开始时间"
                        :picker-options="disabledDate"
                    >
                    </el-date-picker>
                    <span>&nbsp;-&nbsp;</span>
                    <el-date-picker
                        style="width:190px"
                        size="small"
                        v-model="endTime"
                        :clearable="false"
                        type="datetime"
                        placeholder="结束时间"
                        :picker-options="disabledDate2"
                    >
                    </el-date-picker>
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
                    icon="el-icon-document-remove"
                    @click="exportExcelFun"
                >导出</el-button>
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
                highlight-current-row
                auto-resize
                @cell-click="cellClick"
            >
                <vxe-table-column
                    type="seq"
                    title="序号"
                    width="50"
                    fixed="left"
                ></vxe-table-column>

                <vxe-table-column
                    field="taskNo"
                    title="任务编号"
                    min-width="100"
                >
                    <template #default={row}>
                        {{row.taskInfo.taskNo}}
                    </template>
                </vxe-table-column>

                <vxe-table-column
                    field="name"
                    title="班组"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.sysDept.name}}
                    </template>
                </vxe-table-column>

                <vxe-table-column
                    field="count"
                    title="参与人员数"
                    min-width="100"
                ></vxe-table-column>
                <vxe-table-column
                    field="count2"
                    title="使用设备数"
                    min-width="100"
                ></vxe-table-column>                
                <vxe-table-column
                    field="onOffTime"
                    title="工作时间"
                    min-width="100"
                ></vxe-table-column>   
                <vxe-table-column
                    field="realWeldTime"
                    title="焊接时间"
                    min-width="100"
                ></vxe-table-column>
                             
                <vxe-table-column
                    field="normalTime"
                    title="正常时间"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="weldingEfficiency"
                    title="焊接效率"
                    width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="supergageTime"
                    title="超规范时间"
                    min-width="100"
                ></vxe-table-column>
                <vxe-table-column
                    field="standardPercentage"
                    title="规范符合率"
                    min-width="100"
                ></vxe-table-column>
                <vxe-table-column
                    field="materialsConsumption"
                    title="焊材消耗"
                    min-width="100"
                ></vxe-table-column>
                <vxe-table-column
                    field="powerConsumption"
                    title="电能消耗"
                    min-width="100"
                ></vxe-table-column>                
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
        <el-dialog
            :title="title"
            :visible.sync="dialogVisible"
            width="400"
        >
            {{layerCon}}
        </el-dialog>        
    </div>
</template>

<script>
import moment from 'moment'
import { getWorkProDataList, exportWorkProDataList } from '_api/productDataStat/productDataStatApi'
import { getTeam } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
export default {
    name: 'workpieceProduction',
    data () {

        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            //搜索条件
            startTime: moment(new Date()).startOf('day'),//时间
            endTime: moment(new Date()).endOf('day'),//时间

            taskNo:'',
            deptId:'',

            loading: false,
            headers: {
                'Authorization': getToken()
            },
            disabledDate:{
              disabledDate:(time)=>{
                return time.getTime() > moment(this.endTime).toDate().getTime();
              }
            },
            disabledDate2:{
              disabledDate:(time)=>{
                return time.getTime() < moment(this.startTime).toDate().getTime();
              }
            },

            //未绑定设备明细
            dialogVisible: false,
            layerCon: '',
            title: '焊接任务',

            //机构数据
            teamArr: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
        }
    },

    created () {
        this.getList();
        if (this.teamArr.length == 0) {
            this.getTeamList()
        }
    },
    methods: {
        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || []
        },
        search () {
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                pn: this.page,
                taskNo:this.taskNo,
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                deptId:this.deptId && this.deptId.length > 0 ? this.deptId.slice(-1).join('') : '',
            }
            this.loading = true;
            let { data, code } = await getWorkProDataList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || [];
                this.total = data.total
            }
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
                taskNo:this.taskNo,
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                deptId:this.deptId && this.deptId.length > 0 ? this.deptId.slice(-1).join('') : '',
            }
            location.href = exportWorkProDataList(req)
        }, 
        
        cellClick ({ column, row }) {
            if (column.title == "参与人员数" && row.count != 0) {
                this.title = "参与人员数";
                this.layerCon = row.welderNo;
                this.dialogVisible = true;
            } else if (column.title == "使用设备数" && row.count2 != 0) {
                this.title = "使用设备数";
                this.layerCon = row.machineNo;
                this.dialogVisible = true;
            }
        }

    }
}
</script>

<style scoped>
</style>


