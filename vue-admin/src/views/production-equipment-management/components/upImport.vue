<template>
    <div>
        <!-- 选择设备 -->
        <vxe-modal
            title="升级文件导入"
            v-model="model2"
            width="700"
        >
            <template #default>
                <div
                    class="flex pb10"
                    style="border-bottom:1px solid #ddd"
                >
                    <el-upload
                        class="mr10"
                        ref="upload"
                        :show-file-list="false"
                        :headers="headers"
                        :action="importUrl"
                        :auto-upload="false"
                        :on-change='changeFile'
                        :on-success="handleFileSuccess"
                    >
                        <span slot="trigger">
                            <el-input
                                size="small"
                                style="width:250px;margin-right:10px"
                                readonly
                                v-model="fileName"
                                placeholder=""
                            ></el-input>
                            <el-button
                                slot="trigger"
                                size="small"
                                type="primary"
                            >选取文件</el-button>
                        </span>

                    </el-upload>
                </div>

                <div class="top-con flex-n">
                    <div class="con-w">
                        <span>班组：</span>
                        <el-cascader
                            v-model="searchObj.grade"
                            size="small"
                            style="width:180px"
                            clearable
                            :options="teamArr"
                            :props="defalutProps"
                            :show-all-levels="false"
                            @change="search"
                        />
                    </div>
                </div>
                <vxe-table
                    ref="vxeTable"
                    border
                    show-overflow
                    auto-resize
                    size="mini"
                    height="300"
                    :loading="loading2"
                    highlight-hover-row
                    resizable
                    stripe
                    :data="list"
                    row-id="id"
                    :checkbox-config="{'highlight':true,'reserve':true}"
                    @checkbox-all="selectTable2Fun"
                    @checkbox-change="selectTable2Fun"
                >
                    <vxe-table-column
                        type="checkbox"
                        width="60"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="machineNo"
                        title="固定资产编号"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="typeStr"
                        title="设备类型"
                        width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="deptName"
                        title="所属项目"
                        width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="status"
                        title="状态"
                        width="60"
                    >
                      <template slot-scope="scope">
                        <span :class="{'green':scope.row.statusStr=='启用','waring':scope.row.statusStr=='维修','error':scope.row.statusStr=='报废'}">{{scope.row.statusStr}}</span>
                      </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="firmStr"
                        title="厂家"
                        width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="gatherNo"
                        title="采集序号"
                        min-width="100"
                    >
                    </vxe-table-column>
                </vxe-table>
                <div
                    class="p10 flex"
                    style="justify-content: space-between;"
                >
                    <el-pagination
                        :current-page.sync="page"
                        :page-size="10"
                        align="right"
                        background
                        small
                        layout="total, prev, pager, next"
                        :total="total2"
                        @current-change="handleCurrentChange"
                    />
                    <div>
                        <el-button
                            size="small"
                            type="primary"
                            @click="submitUpload"
                        >确定</el-button>
                        <el-button
                            size="small"
                            @click="model2=false"
                        >取消</el-button>
                    </div>

                </div>
            </template>
        </vxe-modal>

        <!-- 程序文件信息 -->
        <vxe-modal
            title="升级文件信息"
            v-model="model"
            width="550"
        >
            <template #default>
                <el-form
                    ref="form"
                    :model="ruleForm"
                    label-width="100px"
                >
                    <el-form-item label="程序路径：">
                        <el-input
                            style="width:350px"
                            v-model="packagePath"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="端口号：">
                        <el-input
                            style="width:100px"
                            v-model="ruleForm.port"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            size="small"
                            type="primary"
                            @click="submitFile"
                        >确定</el-button>
                        <el-button
                            size="small"
                            @click="model=false"
                        >取消</el-button>
                    </el-form-item>
                </el-form>

            </template>
        </vxe-modal>

      <!-- 下发结果显示 -->
      <vxe-modal
        title="下发升级预览"
        v-model="model3"
        width="700"
      >
        <template #default>
          <vxe-table
            border
            show-overflow
            auto-resize
            size="mini"
            height="300"
            highlight-hover-row
            resizable
            stripe
            :data="proData"
          >
            <vxe-table-column
              field="gatherNo"
              title="采集序号"
              min-width="100"
            >
              <template #default="{row}">
                {{row.gatherNo}}
              </template>
            </vxe-table-column>
            <vxe-table-column
              field="channelNo"
              title="下发状态"
              min-width="100"
            >
              <template #default="{row}">
                            <span v-if="row.status===2">
                              发送中...
                            </span>
                            <span v-else-if="row.status===0" style="color:#06834B">
                              成功
                            </span>
                          <span v-else style="color:#f00">
                              失败
                            </span>
              </template>
            </vxe-table-column>
          </vxe-table>
          <div class="p10 tr">
            <el-button
              size="small"
              type="primary"
              @click="model3=false"
            >确定</el-button>
          </div>
        </template>
      </vxe-modal>
    </div>

</template>

<script>
import { getTeam } from '_api/productionProcess/process'
import { getWelderList } from '_api/productionEquipment/production'
import { getToken } from '@/utils/auth'
export default {
    name: 'upImport',
    components: {},
    props: {},
    data () {
        return {
            model2: false,
            loading2: false,
            modelArr2: [],
            list: [],
            //分页
            page: 1,
            total2: 0,
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            //机构数据
            teamArr: [],
            //搜索条件
            searchObj: {
                grade: '',
                model: ''
            },
            //设备选中数据
            selectEquipment: {},

            importUrl: `${process.env.VUE_APP_BASE_API}/welder/machineUpgrade`,
            headers: {
                'Authorization': getToken()
            },

            //上传文件名称
            fileName: '',
            //文件信息
            model: false,
            ruleForm: {
                //端口
                port: '80',
            },

            gatherNoArr: [],//选中设备的所有采集编号

            //mqtt
            newClientMq: {},

            //下发状态
            model3:false,
            //检测下发状态
            proData:[]
        }
    },
    watch: {},
    computed: {
      packagePath(){
        return `http://${process.env.VUE_APP_MQTT_API}:${this.ruleForm.port}/${this.fileName}`;
      }
    },
    methods: {
        init () {
            this.model2 = true;
            this.search();
            this.getTeamList();
            this.selectEquipment = {};
            this.gatherNoArr = [];
            this.$nextTick(() => {
                this.$refs.vxeTable.clearCheckboxReserve();
                this.proData = [];
                this.fileName = '';
            })
        },
        //获取设备
        async getList () {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            // req.model = this.libray.weldModel;
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading2 = true;
            let { data, code } = await getWelderList(req);
            this.loading2 = false;
            if (code == 200) {
                this.list = data.list
                this.total2 = data.total
            }
        },
        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || []
        },
        search () {
            this.page = 1;
            this.getList();
        },
        //分页
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },
        //设备选中数据
        selectTable2Fun ({ records }) {
            this.selectEquipment['page' + this.page] = records;
        },

        //选择文件
        changeFile (file, fileList) {
            this.fileName = file.name || "";
        },

        //提交
        submitUpload () {
            if (!this.fileName) {
                return this.$message.error("请选择升级程序文件");
            }

            let equipmentArr = [];//选中的设备
            //把分页中存储的选中机型取出
            for (let item in this.selectEquipment) {
                if (this.selectEquipment[item] && this.selectEquipment[item].length > 0) {
                    equipmentArr = [...equipmentArr, ...this.selectEquipment[item]]
                }
            }
            if (equipmentArr.length == 0) {
                return this.$message.error("请选择设备");
            }
            //检查选择的设备采集编号是否存在空值
            if (equipmentArr.filter(item => !item.gatherNo || item.gatherNo == '').length > 0) {
                return this.$message.error("选择的设备存在采集序号为空");
            }
            //取出选中设备所有采集编号
            equipmentArr.filter(item => item.gatherNo).forEach(item => {
                this.gatherNoArr = [...this.gatherNoArr, ...item.gatherNo.split(',')]            });

            this.model = true;
        },

        submitFile () {
          // this.submitIssue();
            this.$refs.upload.submit();
        },

        handleFileSuccess (res) {
            if (res.msg == 'true') {
                this.$refs.upload.clearFiles();
                this.submitIssue();
            }
        },

        //命令下发
        submitIssue () {
            this.gatherNoArr.map(item => {
                let objItem = {}
                objItem['gatherNo'] = item;
                objItem['packagePath'] = this.fileName;
                objItem['port'] =  this.ruleForm.port;
                const msg = JSON.stringify(objItem);
                this.doPublish(msg);
                objItem.status=2;//发送中
                this.proData.push(objItem)
            });
            this.model3 = true;
            this.model2 = false;
            this.model = false;
            this.issueTimeOut();
        },

        //mqtt创建
        newMqtt () {
            const PahoMQTT = require('paho-mqtt');
            const name = new Date().getTime() + 'client';
            this.newClientMq = new PahoMQTT.Client(`${process.env.VUE_APP_MQTT_API}`, Number(8083), name);
            this.newClientMq.connect({
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: true,
                useSSL: false,
                onFailure: function (e) {
                    console.log(e);
                },
                reconnect: true,
                onSuccess: (res) => {
                    this.newClientMq.subscribe('jnOtcV1ProgramPathIssueReturn', {
                        qos: 0,
                        onSuccess: function (e) {
                            console.log("下发返回主题订阅成功：jnOtcV1ProgramPathIssueReturn");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    })
                }
            })
            this.newClientMq.onMessageArrived = ({ destinationName, payloadString }) => {
              console.log(payloadString)
                if (destinationName == 'jnOtcV1ProgramPathIssueReturn') {
                    var datajson = JSON.parse(payloadString);
                    console.log(datajson)
                    this.proData.map(item => {
                      //没有成功的状态全部置为失败
                      if(parseInt(item.gatherNo)===parseInt(datajson.gatherNo)){
                        item.status = datajson.status;
                      }
                    })
                }
            }
        },

        doPublish(msg){
            this.newClientMq.publish('jnOtcV1IssueProgramPath',msg)
        },


        //下发超时
        issueTimeOut () {
            setTimeout(() => {
                this.newClientMq.unsubscribe('jnOtcV1ProgramPathIssueReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                })
                this.proData.map(item => {
                  //没有成功的状态全部置为失败
                  if(item.status!==0){
                    item.status =1;
                  }
                })
            }, 5000)
        },




    },
    created () {

    },
    mounted () {
        this.newMqtt();
    }
}
</script>
<style lang="scss" scoped>
.wrapper {
}
</style>

