<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
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
                    field="deptName"
                    title="所属班组"
                    min-width="150"
                    fixed="left"
                ></vxe-table-column>
                <vxe-table-column
                    field="allCount"
                    title="设备总数"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="onOffCount"
                    title="开机设备数"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="realWeldOnline"
                    title="实焊设备数"
                    min-width="150"
                >
                </vxe-table-column>

                <vxe-table-column
                    field="noTaskCount"
                    title="未绑定设备数"
                    width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="equipUtilization"
                    title="设备利用率(%)"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="taskCount"
                    title="焊接任务数"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="realWeldTime"
                    title="焊接时间"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="onOffTime"
                    title="工作时间"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="normalTime"
                    title="正常焊接时间"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="weldingEfficiency"
                    title="焊接效率(%)"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="supergageTime"
                    title="超规范时间"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="standardPercentage"
                    title="规范符合率(%)"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="materialsConsumption"
                    title="焊材消耗(m)"
                    min-width="150"
                ></vxe-table-column>
                <vxe-table-column
                    field="powerConsumption"
                    title="电能消耗(kwh)"
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
        <el-dialog
            title="未绑定设备明细"
            :visible.sync="dialogVisible"
            width="400"
        >
            <el-tag
                class="mr10"
                v-for="(item,index) in noTaskModelList"
                :key="'443'+index"
            >{{'编号:'+item}}</el-tag>
        </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import moment from 'moment'
import { getTeamDataList, exportTeamDataList } from '_api/productDataStat/productDataStatApi'
import { getToken } from '@/utils/auth'
export default {
    name: 'teamProDataStatistics',
    data () {

        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            startTime: moment(new Date()).startOf('day'),//时间
            endTime: moment(new Date()).endOf('day'),//时间

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
            noTaskModelList: []
        }
    },
    computed: {
        ...mapGetters([
            'user'
        ])
    },

    created () {
        console.log(this.user)
        this.getList();
    },
    methods: {

        search () {
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                pn: this.page,
                time1: this.startTime? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.endTime  ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
                deptId:this.user.user.deptId
            }


            this.loading = true;
            let { data, code } = await getTeamDataList(req);
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
                deptId:this.user.user.deptId
            }
            location.href = exportTeamDataList(req)
        },
        //
        viewDetail (row) {
            this.dialogVisible = true;
            this.noTaskModelList = row.noTaskMachineDetail.split(',') || []
        },
        cellClick ({ column, row }) {
            if (column.title == "未绑定设备数" && row.noTaskCount != 0) {
                this.viewDetail(row);
            }
        }

    }
}
</script>

<style scoped>
</style>


