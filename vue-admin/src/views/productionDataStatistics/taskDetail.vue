<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
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
                <span>焊工编号：</span>
                <el-input
                    size="mini"
                    v-model="welderNo"
                    class="w120"
                    placeholder=""
                ></el-input>
            </div>
            <div class="con-w">
                <span>焊工姓名：</span>
                <el-input
                    size="mini"
                    v-model="welderName"
                    class="w120"
                    placeholder=""
                ></el-input>
            </div>
            <div class="con-w">
                <span>焊机编号：</span>
                <el-input
                    size="mini"
                    v-model="machineNo"
                    class="w120"
                    placeholder=""
                ></el-input>
            </div>
            <div class="con-w">
                <span>任务编号：</span>
                <el-input
                    size="mini"
                    v-model="taskNo"
                    class="w120"
                    placeholder=""
                ></el-input>
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
            >
                <vxe-table-column
                    type="seq"
                    title="序号"
                    width="50"
                    fixed="left"
                ></vxe-table-column>
                <vxe-table-column
                    field="welderNo"
                    title="焊工编号"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.welderInfo.welderNo}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="welderName"
                    title="焊工姓名"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.welderInfo.welderName}}
                    </template>
                </vxe-table-column>

                <vxe-table-column
                    field="machineNo"
                    title="焊机编号"
                    width="100"
                >
                    <template #default={row}>
                        {{row.machineWeldInfo.machineNo}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="taskNo"
                    title="任务编号"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.taskInfo.taskNo}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="name"
                    title="班组"
                    width="100"
                >
                    <template #default={row}>
                       {{row.sysDept.name}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="realityStarttime"
                    title="开始时间"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.taskInfo.realityStarttime}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="realityEndtime"
                    title="结束时间"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.taskInfo.realityEndtime}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="eleScope"
                    title="电流范围"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="electricity"
                    title="平均电流"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="volScope"
                    title="电压范围"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="voltage"
                    title="平均电压"
                    min-width="150"
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

    </div>
</template>

<script>
import moment from 'moment'
import { getTeam } from '_api/productionProcess/process'
import { getTaskDataList, exportTaskDataList } from '_api/productDataStat/productDataStatApi'
import { getToken } from '@/utils/auth'
export default {
    name: 'taskDetail',
    data () {

        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            startTime: moment(new Date()).startOf('day'),//时间
            endTime: moment(new Date()).endOf('day'),//时间
            deptId: '',
            welderNo: '',
            welderName: '',
            machineNo: '',
            taskNo: '',

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
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                deptId: this.deptId && this.deptId.length > 0 ? this.deptId.slice(-1).join('') : '',
                welderNo: this.welderNo,
                welderName: this.welderName,
                machineNo: this.machineNo,
                taskNo: this.taskNo,
            }
            this.loading = true;
            let { data, code } = await getTaskDataList(req);
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
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                deptId: this.deptId && this.deptId.length > 0 ? this.deptId.slice(-1).join('') : '',
                welderNo: this.welderNo,
                welderName: this.welderName,
                machineNo: this.machineNo,
                taskNo: this.taskNo,
            }
            location.href = exportTaskDataList(req)
        }
    }
}
</script>

<style scoped>
</style>


