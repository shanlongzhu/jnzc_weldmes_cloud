<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <!-- <div class="con-w">
                <span>班组：</span>
                <el-cascader
                    v-model="teamId"
                    size="small"
                    class="w150"
                    clearable
                    :options="teamArr"
                    :props="defalutProps"
                    :show-all-levels="false"
                />
            </div> -->
            
            <div class="con-w">
                <span>时间：</span>
                <el-date-picker
                        style="width:200px"
                        :clearable="false"
                        size="small"
                        v-model="startTime"
                        type="datetime"
                        placeholder="开始时间"
                        :picker-options="disabledDate"
                    >
                    </el-date-picker>
                    <span>&nbsp;-&nbsp;</span>
                    <el-date-picker
                        style="width:190px"
                        :clearable="false"
                        size="small"
                        v-model="endTime"
                        type="datetime"
                        placeholder="结束时间"
                        :picker-options="disabledDate2"
                    >
                    </el-date-picker>
            </div>
            <div class="con-w">
                <el-radio-group v-model="status" size="mini">
                    <el-radio :label="3">未分配</el-radio>
                    <el-radio :label="6">已分配</el-radio>
                    <el-radio :label="9">全部</el-radio>
                </el-radio-group>
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
            >
                <vxe-table-column
                    type="seq"
                    title="序号"
                    width="50"
                    fixed="left"
                ></vxe-table-column>
                <vxe-table-column
                    field="name"
                    title="所属班组"
                    min-width="150"
                    fixed="left"
                >
                    <template #default={row}>
                        {{row.sysDept.name}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="machineNo"
                    title="设备编号"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="createTime"
                    title="日期"
                    min-width="150"
                >
                </vxe-table-column>

                <vxe-table-column
                    field="taskNo"
                    title="任务号"
                    width="100"
                >
                    <template #default={row}>
                        {{row.taskInfo.taskNo}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="taskNo"
                    title="状态"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.sysDictionary.valueName}}
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

    </div>
</template>

<script>
import moment from 'moment'
import { getTeam } from '_api/productionProcess/process'
import { getWeldingTaskDataList, exportWeldingTaskDataList } from '_api/productDataStat/productDataStatApi'
import { getToken } from '@/utils/auth'
export default {
    name: 'weldingMachine',
    data () {

        return {
            list: [],
            //分页
            page: 1,
            total: 0,
            pageSize:10,

            //搜索条件
            startTime: moment(new Date()).startOf('day'),//时间
            endTime: moment(new Date()).endOf('day'),//时间
            teamId: '',
            status:'',

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
                size:this.pageSize,
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                teamId: this.teamId && this.teamId.length > 0 ? this.teamId.slice(-1).join('') : '',
                status:this.status
            }
            this.loading = true;
            let { data, code } = await getWeldingTaskDataList(req);
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

        handleSizeChange (s) {
            this.pageSize = s;
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
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                teamId: this.teamId && this.teamId.length > 0 ? this.teamId.slice(-1).join('') : '',
                status:this.status
            }
            location.href = exportWeldingTaskDataList(req)
        }
    }
}
</script>

<style>
.con-w .el-radio{
    margin-right:14px
}
.con-w .el-radio .el-radio__label{
    padding-left: 5px;
}
</style>


