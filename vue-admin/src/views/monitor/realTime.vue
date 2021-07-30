<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div
            class="table-con flex"
            style="flex:1; height:0px"
        >
            <div
                class="user-l"
                style='height:100%;width:300px;border:1px solid #ddd;margin-right:10px'
            >
                <div class="organizational-tit">组织机构菜单</div>
                <div style="height:calc(100% - 34px);overflow-y:auto">
                    <el-tree
                        :data="treeData"
                        ref="treeDom"
                        :expand-on-click-node="false"
                        v-loading="treeLoading"
                        :props="defaultProps"
                        default-expand-all
                        @current-change="currentChangeTree"
                        highlight-current
                        node-key="id"
                    ></el-tree>
                </div>
            </div>
            <div
                class="user-r flex-c real-tit"
                style='height:100%;flex:1; width:0px'
            >
                <div class="organizational-tit fs14">焊机实时状态监测</div>
                <div
                    class="real-con"
                    v-loading="loading"
                >
                    <div class="real-con-box flex-n">
                        <div
                            class="real-con-item flex-n"
                            v-for="item in list"
                            :key="item.id"
                        >
                            <span class="real-con-item-img">
                                <img :src="`/swipes/${imgType(item.typeStr)}${statusText(item.weldStatus).imgN}.png`" />
                            </span>
                            <div class="real-con-item-txt">
                                <p><span>设备编号：</span>{{item.machineNo||'--'}}</p>
                                <p><span>任务编号：</span>{{item.taskNo||'--'}}</p>
                                <p><span>操作人员：</span>{{item.welderName||'--'}}</p>
                                <p><span>焊接电流：</span>{{item.electricity||item.electricity===0?item.electricity:'--'}}A</p>
                                <p><span>焊接电压：</span>{{item.voltage||item.voltage===0?item.voltage:'--'}}V</p>
                                <p><span>焊机状态：</span><strong>{{statusText(item.weldStatus).str}}</strong></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            background
            layout="total, prev, pager, next"
            :total="total"
            @current-change="handleCurrentChange"
        /> -->
        <el-drawer
            title="我是标题"
            :visible.sync="drawer"
            :with-header="false"
        >
            <span>我来啦!</span>
        </el-drawer>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getTeam } from '_api/productionProcess/process'
import { getUserTree, getTreeDeptInfo, addDept, findIdDeptInfo, editDept, delDept } from '_api/system/systemApi'
import { getModelFindId } from '_api/productionEquipment/production'
export default {
    name: 'organizational',
    data () {

        return {
            //mqtt
            client: {},
            options: {
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: false,
                useSSL: false,
                reconnect: true,
                clientId: "adminTest" + new Date().getTime()
            },
            timeout: '',

            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj: {
                id: ''
            },
            
            loading: false,

            //部门tree数据
            treeLoading: false,
            treeData: [],
            defaultProps: {
                children: 'list',
                label: 'name'
            },
            //
            drawer:false
        }
    },
    created () {
        this.searchObj.id = 1;
        this.getList();
        this.getUserTreeFun();
    },
    methods: {
        //mqtt创建
        createConnection () {
            let connectUrl = `ws://${process.env.VUE_APP_MQTT_API}:8083/mqtt`
            try {
                this.client = mqtt.connect(connectUrl, this.options)
            } catch (error) {
                console.log('连接失败', error)
            }
            this.client.on('connect', () => {
                console.log('连接成功')
                this.doSubscribe();

            })
            this.client.on('error', error => {
                console.log('连接失败', error)
            })
            this.client.on('message', (topic, message) => {
                if (topic == 'rtcdata') {
                    var datajson = JSON.parse(`${message}`);
                    console.log(datajson)
                    clearTimeout(this.timeout);
                    //第一条不延时显示
                    this.setData(0, datajson);
                    //后面两条延时显示
                    for (let i = 1; i < 3; i++) {
                        ((i) => {
                            this.timeout = setTimeout(() => {
                                this.setData(i, datajson);
                            }, (i + 1) * 1000);
                        })(i)
                    }
                }
            })
        },

        setData (i, arr) {
            this.list.forEach(item => {
                let filterArr = arr.filter(v1 => v1.gatherNo == item.gatherNo);
                if (filterArr.length == 3) {
                    item.voltage = filterArr[i].voltage
                    item.electricity = filterArr[i].electricity
                    item.welderName = filterArr[i].welderName
                    item.taskNo = filterArr[i].taskNo
                    item.weldStatus = filterArr[i].weldStatus;//状态
                } else {
                    item.voltage = ''
                    item.electricity = ''
                    item.welderName = ''
                    item.taskNo = ''
                    item.weldStatus = -1;//状态
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('rtcdata', 0, (error, res) => {
                console.log('订阅rtcdata')
                if (error) {
                    this.$message.error("订阅rtcdata超时")
                    return
                }
            })
        },




        search () {
            this.page = 1;
            this.getList();
        },
        //获取部门tree
        async getUserTreeFun () {
            this.treeLoading = true;
            let { data, code } = await getUserTree();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = [data] || [];
                this.$nextTick(() => {
                    this.$refs.treeDom.setCurrentKey(this.searchObj.id)
                })
            }
        },

        //根据部门id获取设备列表
        async getList (id) {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            this.loading = true;
            let { code, data } = await getModelFindId(req);
            this.loading = false;
            if (code == 200) {
                this.list = (data.list || []).map(item => {
                    let objItem = { ...item };
                    objItem.electricity = '';//电流
                    objItem.voltage = '';//电压
                    objItem.weldIp = '';//IP
                    objItem.weldStatus = -1;//状态
                    objItem.welderName = '';//操作人
                    objItem.taskNo = '';//任务编号
                    return objItem;
                });
                this.total = data.total;
                this.createConnection();
            }
        },

        //分页切换
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },

        currentChangeTree (v) {
            this.searchObj.id = v.id;
            this.search();
        },

        //点击焊机
        handlerWeld(){
            this.drawer = true;
        },


        //焊机状态
        statusText (v) {
            let str = '关机';
            let imgN = '';
            switch (v) {
                case -1:
                    str = '关机';
                    imgN = '';
                    break;
                case 0:
                    str = '待机';
                    imgN = 'S';
                    break;
                case 3: case 5: case 7:
                    str = '焊接';
                    imgN = 'W';
                    break;
                default:
                    str = '故障';
                    imgN = 'O';
                    break;
            }
            return {
                str: str,
                imgN: imgN
            };
        },
        //图片类型
        imgType (v) {
            let str = '';
            switch (v) {
                case '手工焊':
                    str = 'AT';
                    break;
                case '气保焊':
                    str = 'FR';
                    break;
                default:
                    str = 'GL';
                    break;
            }
            return str;
        },

    }
}
</script>

<style>
.user-l * {
    font-size: 12px;
}
.organizational-tit {
    height: 34px;
    padding: 0 10px;
    line-height: 34px;
    background: #f8f8f8;
    font-weight: bold;
    border-bottom: 1px solid #ddd;
    border-left: 1px solid #ddd;
}
.real-tit {
    border-top: 1px solid #ddd;
}
.real-con {
    flex: 1;
    height: 0;
    overflow-y: scroll;
    border: 1px solid #ddd;
    border-top: none;
}
.real-con-item {
    line-height: 20px;
    font-size: 12px;
    align-items: center;
    padding: 10px;
    width: 230px;
}
.real-con-item .real-con-item-img {
    margin-right: 6px;
}
.real-con-item .real-con-item-txt p {
    margin: 0px;
}
.real-con-item .real-con-item-txt p span {
    color: #666;
}
</style>
