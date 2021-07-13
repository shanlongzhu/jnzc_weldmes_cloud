<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
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
                    value-format="yyyy-MM-dd HH:mm:ss"
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
                    label="序号"
                    align="left"
                    width="50"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="welderName"
                    label="所属班组"
                    align="left"
                    min-width="100"
                    fixed="left"
                />
                <el-table-column
                    prop="welderNo"
                    label="设备总数"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cellphone"
                    label="开机设备数"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="rank"
                    label="实焊设备数"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueName}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="welderNo"
                    label="未绑定设备数"
                    align="left"
                    min-width="120"
                />
                <el-table-column
                    prop="macPath"
                    label="设备利用率(%)"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNames}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="焊接任务数"
                    align="left"
                    min-width="170"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDept.name}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="remarks"
                    label="焊接时间"
                    align="left"
                    min-width="170"
                />
                <el-table-column
                    prop="remarks"
                    label="工作时间"
                    align="left"
                    min-width="170"
                />
                <el-table-column
                    prop="remarks"
                    label="焊接效率(%)"
                    align="left"
                    min-width="170"
                />                
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
    </div>
</template>

<script>
import { getTeamDataList,exportTeamDataList } from '_api/productDataStat/productDataStatApi'
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
            dateTime: '',

            loading: false,
            importUrl: `${process.env.VUE_APP_BASE_API}/solderer/importExcel`,
            headers: {
                'Authorization': getToken()
            }
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
                time1:this.dateTime[0]?this.dateTime[0]:'',
                time2:this.dateTime[1]?this.dateTime[1]:'',
            }
            this.loading = true;
            let { data, code } = await getTeamDataList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list
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
                time1:this.dateTime[0]?this.dateTime[0]:'',
                time2:this.dateTime[1]?this.dateTime[1]:'',
            }
            location.href = exportTeamDataList(req)
        },

    }
}
</script>

<style scoped>
</style>


