<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="history-top flex-c">
            <div class="top-con flex-n">
              <div class="con-w">
                <span>任务状态：</span>
                <el-select
                  v-model="taskStatusId"
                  placeholder="请选择"
                  clearable
                  style="width:150px"
                  @change="changeTaskStatus"
                  multiple
                  collapse-tags
                >
                  <el-option
                    v-for="item in taskStatusArr"
                    :key="item.id"
                    :label="item.valueName"
                    :value="item.id"
                  />
                </el-select>
              </div>
                <div class="con-w">
                    <span>任务编号：</span>
                    <el-select
                        v-model="searchObj.taskId"
                        placeholder="请选择"
                        clearable
                        filterable
                        style="width:200px"
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
                        v-model="searchObj.welderId"
                        placeholder="请选择"
                        clearable
                        filterable
                        style="width:150px"
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
                <span>焊机品牌：</span>
                <el-select
                  v-model="weldBrand"
                  placeholder="请选择"
                  clearable
                  style="width:100px"
                  @change="changeWeldBrand"
                >
                  <el-option
                    v-for="item in manufactorArr"
                    :key="item.value"
                    :label="item.valueName"
                    :value="item.value"
                  />
                </el-select>
              </div>
                <div class="con-w">
                    <span>焊机编号：</span>
                    <el-select
                        v-model="weldMachineIdObj"
                        placeholder="请选择"
                        clearable
                        filterable
                        style="width:140px"
                    >
                        <el-option
                            v-for="item in weldingMachineArr"
                            :key="item.macFlag+'_'+item.id"
                            :label="item.machineNo"
                            :value="JSON.stringify(item)"
                        >
                          <span style="float: left">{{ item.machineNo }}</span>
                          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.macFlag===0?'OTC':'松下' }}</span>
                        </el-option>
                    </el-select>
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
                        :clearable="false"
                        v-model="endTime"
                        type="datetime"
                        placeholder="结束时间"
                        :picker-options="disabledDate2"
                    >
                    </el-date-picker>
                </div>
                <div class="con-w">
                    <el-input size="small" style="width: 50px;display: none" v-model="dataSource"></el-input>
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
                    @current-change="currentChangeEvent"
                >
                    <vxe-table-column
                        type="seq"
                        title="序号"
                        width="50"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="taskNo"
                        title="任务编号"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="welderName"
                        title="焊工姓名"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="welderNo"
                        title="焊工编号"
                        min-width="150"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="machineNo"
                        title="焊机编号"
                        min-width="150"
                    >
                    </vxe-table-column>
                  <vxe-table-column
                    field="macFlag"
                    title="焊机厂家"
                    min-width="150"
                  >
                    <template #default="{row}">
                      {{row.macFlag===0?'OTC':'松下'}}
                    </template>
                  </vxe-table-column>

                    <vxe-table-column
                        field="taskRealityStartTime"
                        title="开始时间"
                        min-width="150"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="taskRealityEndTime"
                        title="终止时间"
                        min-width="150"
                    ></vxe-table-column>
                </vxe-table>
            </div>
            <div class="flex cz-bot-wrap">

                <span
                    class="zoom-btn"
                    @click="zoomFun"
                ><i class="el-icon-zoom-in"></i></span>
                <el-pagination
                    class="p10"
                    :current-page.sync="page"
                    :page-size="10"
                    align="right"
                    background
                    :page-sizes="[10, 50, 100, 150, 200]"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    :total="total"
                    @current-change="handleCurrentChange"
                />
            </div>
        </div>
        <div class="history-bottom flex">
            <line-com-e ref="lineComE"></line-com-e>
            <line-com-2 ref="lineCom2"></line-com-2>
        </div>
        <el-dialog
            title="历史曲线图"
            :visible.sync="dialogVisible"
            fullscreen
        >
            <div
                class="flex-c"
                style="height:calc(100vh - 100px)"
            >
                <line-com-e ref="lineComEChild"></line-com-e>
                <line-com-2 ref="lineCom2Child"></line-com-2>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import moment from 'moment'
import { getTaskArr, getWelderArr, getWeldingArr, getHistoryList, getHistoryTimeData, getHistoryRepeat ,getProcessDbHistoryData} from '_api/productionDataAnalysis/productionDataAnalysisApi'
import { getDictionaries} from '_api/productionProcess/process'
// import LineCom from './components/lineCom.vue';
import LineCom2 from './components/lineCom2.vue';
import LineComE from './components/lineComE.vue';
export default {
    components: { LineCom2, LineComE },
    name: 'historyLine',
    data () {
        return {
            list: [],
            startTime: moment(new Date()).startOf('day'),//时间
            endTime: moment(new Date()).endOf('day'),//时间
            taskStatusId:[],//任务状态
            weldBrand:'',//焊机品牌
            searchObj: {
                taskId: '',//任务编号
                welderId: '',//焊工ID
                weldMachineId: ''//焊机编号
            },
            //分页
            page: 1,
            total: 0,
            pageSize:10,
            loading: false,
            //任务状态
            taskStatusArr:[],
            //任务编号下拉数据
            taskArr: [],
            //焊工下拉数据
            welderArr: [],
            //焊机厂家标识
            manufactorArr:[],
            //焊机下拉数据
            weldingMachineArr: [],
            weldingMachineArrSource:[],
          weldMachineIdObj:'',//选中的焊机编号
            dialogVisible: false,


            //曲线请求参数
            curveReq: {
                taskId: '',
                welderId: '',
                weldMachineId: '',
                startTime: '',
                endTime: '',
                weldType:''
            },
            //曲线数据
            timeData: [],
            voltageData: [],
            electricityData: [],

            //记录剩余表明
            surplusTable: [],
            surIndex: 0,
            disabledDate: {
                disabledDate: (time) => {
                    return time.getTime() > moment(this.endTime).toDate().getTime();
                }
            },
            disabledDate2: {
                disabledDate: (time) => {
                    return time.getTime() < moment(this.startTime).toDate().getTime();
                }
            },
          dataSource:2,
        }
    },

    created () {
        // this.getList();
        this.getTaskListFun();
        this.getWelderListFun();
        this.getWeldingListFun();
        this.getDicFun();
    },
    methods: {
      //获取数据字典
      async getDicFun () {
        let { data, code } = await getDictionaries({ "types": ["12","5"] });
        if (code == 200) {
          this.taskStatusArr = data['12'] || [];
          this.manufactorArr = data["5"]||[];
        }
      },
      //设置任务状态
      changeTaskStatus(v){
        this.searchObj.taskId = '';
        this.getTaskListFun(v);
      },

      //筛选焊机编号

        zoomFun () {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.lineCom2Child.init(this.voltageData, this.timeData);
                this.$refs.lineComEChild.init(this.electricityData, this.timeData)
            })
        },

        search () {
            if (this.startTime && this.endTime) {
                this.page = 1;
                this.getList();
                this.voltageData = [];
                this.electricityData = [];
                this.timeData = [];
            } else {
                return this.$message.error("请选择查询时间")
            }

        },

        //获取任务列表
        async getList () {
            let params = {
              pn: this.page,
              size:this.pageSize,
            }
            let req = {
              startTime: this.startTime ? moment(this.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
              endTime: this.endTime ? moment(this.endTime).format('YYYY-MM-DD HH:mm:ss') : '',
              ...this.searchObj
            }
            req.weldMachineId = this.weldMachineIdObj?JSON.parse(this.weldMachineIdObj).id:null;
            req.weldType = this.weldMachineIdObj?JSON.parse(this.weldMachineIdObj).macFlag:null;

            if (!req.taskId && !req.welderId && req.weldMachineId) {
                this.$nextTick(() => {
                    let rowObj = {
                        taskId: req.taskId,
                        welderId: req.welderId,
                        machineId: req.weldMachineId,
                        taskRealityStartTime: req.startTime,
                        taskRealityEndTime: req.endTime,
                        weldType:req.weldType
                    }
                    this.currentChangeEvent({ row: rowObj });
                });
            } else {
                this.loading = true;
                let { code, data } = await getHistoryList(params,req);
                this.loading = false;
                if (code == 200) {
                    this.list = data.list
                    this.total = data.total
                }
                this.$nextTick(() => {
                    this.$refs.lineCom2.init(this.voltageData, this.timeData);
                    this.$refs.lineComE.init(this.electricityData, this.timeData);
                })
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

        //获取任务编号
        async getTaskListFun (req) {
            let { data, code } = await getTaskArr(req);
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
                this.weldingMachineArrSource = data || [];
            }
        },

      changeWeldBrand(v){
        console.log(v)
        if(v||v===0){
          this.weldingMachineArr = this.weldingMachineArrSource.filter(item => item.macFlag==v);
        }else{
          this.weldingMachineArr = [...this.weldingMachineArrSource];
        }

      },


        //选择任务数据
        async currentChangeEvent ({ row }) {
            this.surIndex = 0;
            this.curveReq.weldType = row.weldType||row.macFlag;
            this.curveReq.taskId = row.taskId;
            this.curveReq.welderId = row.welderId;
            this.curveReq.weldMachineId = row.machineId;
            this.curveReq.startTime = row.taskRealityStartTime;
            this.curveReq.endTime = row.taskRealityEndTime;

            this.$refs.lineCom2.echartsLoading();
            this.$refs.lineComE.echartsLoading();
            this.getHistoryCurve();
        },
        async getHistoryCurve () {
            if(this.dataSource==2){
              //ProcessDb
              let req = {
                weldMachineId:this.curveReq.weldMachineId,
                startTime:this.curveReq.startTime,
                endTime:this.curveReq.endTime,
                weldType:this.curveReq.weldType
              }
              let {code,data} = await getProcessDbHistoryData(req);
              if(code==200){
                this.timeData = data.otcHistoryList.map(item => item.weldTime);
                this.voltageData = data.otcHistoryList.map(item => item.voltage);
                this.electricityData = data.otcHistoryList.map(item => item.electricity);
                this.$refs.lineCom2.init(this.voltageData, this.timeData);
                this.$refs.lineComE.init(this.electricityData, this.timeData);
              }
            }else{
              //原数据源
              let req = {
                ...this.curveReq
              }
              let { data, code } = await getHistoryTimeData(req);
              if (code == 200) {
                this.surplusTable = data.tableNames || [];
                this.timeData = (data.list || []).filter(item => item.weldTime).map(item => item.weldTime);
                this.voltageData = (data.list || []).filter(item => item.voltage || item.voltage === 0).map((item, index) => {
                  return item.voltage
                });

                this.electricityData = (data.list || []).filter(item => item.electricity || item.electricity === 0).map(item => {
                  return item.electricity;
                });

                this.$refs.lineCom2.init(this.voltageData, this.timeData);
                this.$refs.lineComE.init(this.electricityData, this.timeData);
                this.repeatFun();
              }
            }
        },
        async repeatFun () {
            this.surIndex++;
            let req = this.surplusTable[this.surIndex]
            if (req) {
                let { data, code } = await getHistoryRepeat(req)
                if (code == 200) {
                    this.repeatFun();

                    const timeData = (data.list || []).filter(item => item.weldTime).map(item => item.weldTime);
                    const voltageData = (data.list || []).filter(item => item.voltage || item.voltage === 0).map((item, index) => {
                        return item.voltage
                    });

                    const electricityData = (data.list || []).filter(item => item.electricity || item.electricity === 0).map(item => {
                        return item.electricity;
                    });
                    this.voltageData = this.voltageData.concat(voltageData);
                    this.electricityData = this.electricityData.concat(electricityData);
                    this.timeData = this.timeData.concat(timeData);
                    this.$refs.lineCom2.addData(voltageData, timeData);
                    this.$refs.lineComE.addData(electricityData, timeData);
                }

            }
        },

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

.cz-bot-wrap {
    justify-content: space-between;
    align-items: center;
}
.cz-bot-wrap .zoom-btn {
    font-size: 20px;
    margin-left: 10px;
    cursor: pointer;
}
</style>


