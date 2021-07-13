<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="history-top flex-c">
            <div class="top-con flex-n">
                <div class="con-w">
                    <span>任务编号：</span>
                    <el-select
                        v-model="searchObj.rate"
                        placeholder="请选择"
                        clearable
                        style="width:120px"
                    >
                        <el-option
                            v-for="item in taskArr"
                            :key="item.id"
                            :label="item.taskNo"
                            :value="item.id"
                        />
                    </el-select>
                </div>
                <div class="con-w">
                    <span>焊工：</span>
                    <el-select
                        v-model="searchObj.rate"
                        placeholder="请选择"
                        clearable
                        style="width:120px"
                    >
                        <el-option
                            v-for="item in welderArr"
                            :key="item.id"
                            :label="item.welderName"
                            :value="item.id"
                        >
                        <span style="float: left; min-width:80px">{{ item.welderName }}</span>
                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.welderNo }}</span>
                        </el-option>                        
                    </el-select>
                </div>
                <div class="con-w">
                    <span>焊机编号：</span>
                    <el-select
                        v-model="searchObj.rate"
                        placeholder="请选择"
                        clearable
                        style="width:120px"
                    >
                        <el-option
                            v-for="item in weldingMachineArr"
                            :key="item.id"
                            :label="item.machineNo"
                            :value="item.id"
                        />
                    </el-select>
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
            </div>
            <div style="flex:1;height:0">
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
                    ></vxe-table-column>
                    <vxe-table-column
                        field="name"
                        title="任务编号"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="loginName"
                        title="焊工姓名"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="mobile"
                        title="焊工编号"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="email"
                        title="焊机编号"
                        min-width="150"
                    >
                    </vxe-table-column>

                    <vxe-table-column
                        field="status"
                        title="开始时间"
                        min-width="150"
                    >
                        <template #default={row}>
                            <span
                                v-if="row.status"
                                style="color:#42a3fd"
                            >正常</span>
                            <span
                                v-else
                                style="color:red"
                            >禁用</span>
                        </template>
                    </vxe-table-column>

                    <vxe-table-column
                        field="roleName"
                        title="终止时间"
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
                small
                layout="total, prev, pager, next"
                :total="total"
                @current-change="handleCurrentChange"
            />
        </div>
        <div class="history-bottom flex">
            <line-com></line-com>
            <line-com-2></line-com-2>

        </div>
    </div>
</template>

<script>
import { getTaskArr, getWelderArr, getWeldingArr } from '_api/productionDataAnalysis/productionDataAnalysisApi'
import LineCom from './components/lineCom.vue';
import LineCom2 from './components/lineCom2.vue';
export default {
    components: { LineCom, LineCom2 },
    name: 'historyLine',
    data () {
        return {
            list: [],
            dateTime: '',//时间
            taskCode: '',//任务编号
            searchObj: {},
            //分页
            page: 1,
            total: 0,
            loading: false,

            //任务编号下拉数据
            taskArr: [],
            //焊工下拉数据
            welderArr: [],
            //焊机下拉数据
            weldingMachineArr: []

        }
    },

    created () {
        this.getList();
        this.getTaskListFun();
        this.getWelderListFun();
        this.getWeldingListFun();
    },
    methods: {
        search () {
            this.page = 1;
            this.getList();
        },

        //获取任务列表
        async getList () {
            let req = {
                pn: this.page,
                time1: this.dateTime[0] ? this.dateTime[0] : '',
                time2: this.dateTime[1] ? this.dateTime[1] : '',
            }
            this.loading = true;
            let { code, data } = await findByIdUserList(req);
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

        //获取任务编号
        async getTaskListFun () {
            let { data, code } = await getTaskArr();
            if (code == 200) {
                this.taskArr = data || [];
            }
        },
        //获取焊工列表
        async getWelderListFun () {
            const { code, data } = await getWelderArr();
            if (code == 200) {
                this.welderArr = data || []
            }
        },
        //获取焊机列表
        async getWeldingListFun () {
            const { data, code } = await getWeldingArr();
            if (code == 200) {
                this.weldingMachineArr = data || [];
            }
        }
    }
}
</script>

<style>
.history-top {
    flex: 1;
}
.history-bottom {
    flex: 1;
}
.history-bottom-l {
    flex: 1;
    border-bottom: 1px solid #ddd;
    height: 100%;
    width: 0;
}
.history-bottom-r {
    flex: 1;
    width: 0px;
    height: 100%;
}
</style>


