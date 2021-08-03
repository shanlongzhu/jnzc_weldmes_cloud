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
                <span>时间：</span>
                <el-date-picker
                    style="width:350px"
                    size="small"
                    v-model="dateTime"
                    type="datetimerange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"
                    :picker-options="disabledDate"
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
                    field="taskNo"
                    title="任务编号"
                    min-width="150"
                >
                    <template #default={row}>
                        {{row.taskInfo.taskNo}}
                    </template>
                </vxe-table-column>
                
                <vxe-table-column
                    field="time"
                    title="焊接时间"
                    min-width="150"
                >
                </vxe-table-column>

                <vxe-table-column
                    field="time2"
                    title="工作时间"
                    width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="utilization"
                    title="焊接效率(%)"
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
import { getWorkProDataList, exportWorkProDataList } from '_api/productDataStat/productDataStatApi'
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
            dateTime: [moment(new Date()).startOf('day'), new Date()],
            taskNo:'',

            loading: false,
            headers: {
                'Authorization': getToken()
            },
            disabledDate: {
                disabledDate (time) {
                    return time.getTime() > Date.now() + 3600 * 1000 * 24
                }
            },
        }
    },

    created () {
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
                taskNo:this.taskNo,
                time1: this.dateTime&&this.dateTime[0] ? moment(this.dateTime[0]).format('YYYY-MM-DD HH:mm:ss') : '',
                time2: this.dateTime&&this.dateTime[1] ? moment(this.dateTime[1]).format('YYYY-MM-DD HH:mm:ss') : '',
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
                time1: this.dateTime&&this.dateTime[0] ? this.dateTime[0] : '',
                time2: this.dateTime&&this.dateTime[0] ? this.dateTime[1] : '',
            }
            location.href = exportWorkProDataList(req)
        },      

    }
}
</script>

<style scoped>
</style>


