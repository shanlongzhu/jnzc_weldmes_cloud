<template>
    <div
        class="home-bg"
        ref="homeBg"
        style="width:100%"
    >
        <div
            class="btn-box"
            :style="styleObj"
            @click="showArea"
        ></div>
        <img
            width="100%"
            src="../../assets/home_images/home.jpg"
        />

        <!-- 区域跨间 -->
        <el-dialog
            title="区域"
            :visible.sync="visable1"
            :close-on-click-modal="false"
            :modal-append-to-body="false"
            :append-to-body="false"
            width="800px"
        >
            <div class="pb10">
                <span class="mr20" style="font-size:16px">关机:<strong>{{offnum||0}}</strong></span>
                <span class="mr20" style="color:#f00;font-size:16px">故障:<strong>{{warnArray.length||0}}</strong></span>
                <span class="mr20" style="color:rgb(1, 209, 95);font-size:16px">待机:<strong>{{standbyArray.length||0}}</strong></span>
                <span class="mr20" style="color:green;font-size:16px">焊接:<strong>{{workArray.length||0}}</strong></span>
            </div>
            <vxe-table
                border
                :data="listAreaData"
                size="mini"
                height="400px"
                :loading="loading"
                highlight-current-row
                auto-resize
                @current-change="cellClick"
                :span-method="mergeRowMethod"
            >
                <vxe-table-column
                    type="seq"
                    title="序号"
                    width="50"
                    fixed="left"
                ></vxe-table-column>
                <vxe-table-column
                    field="areaName"
                    title="区域"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="areaBayName"
                    title="跨间"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="areaBayName"
                    title="操作"
                    width="100"
                >
                <template #default="{row}">
                    <span class="cur c-blue" @click="goPage(row)">查看详情</span>
                </template>
                </vxe-table-column>

            </vxe-table>
        </el-dialog>

    </div>

    <!-- <canvas id="cvs" style="width:100%;height:100%">当前浏览器不支持canvas</canvas> -->
</template>

<script>
import { getWelderListNoPage } from '_api/productionEquipment/production'
import { getAreaG } from '_api/system/systemApi'
export default {
    components: {},
    props: {},
    data () {
        return {
            //mqtt
            newClientMq: {},
            img: {},
            set: '',
            styleObj: {},
            styleC: {
                width: 133,
                height: 194,
                top: 910,
                left: 233
            },
            visable1: false,
            listAreaData: [],
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj: {
                area: '',
                bay: ''
            },
            workArray: [],//焊接
            standbyArray: [],//待机
            warnArray: [],//故障

            loading: false
        }
    },
    watch: {},
    computed: {
        offnum () {
            return this.list.length - (this.workArray.length + this.standbyArray.length + this.warnArray.length)
        }
    },
    methods: {

        // 定时执行获取宽高
        check () {
            // 只要任何一方大于0
            // 表示已经服务器已经返回宽高
            if (this.img.width > 0 || this.img.height > 0) {
                let scale = this.img.width / this.$refs.homeBg.clientWidth;
                clearInterval(this.set);
                this.styleObj = { ...this.styleC };
                this.styleObj.width = this.styleObj.width / scale + 'px';
                this.styleObj.height = this.styleObj.height / scale + 'px';
                this.styleObj.left = this.styleObj.left / scale + 'px';
                this.styleObj.top = this.styleObj.top / scale + 'px';
            }
        },

        showArea () {
            this.visable1 = true;
            this.getAreaFun();           
        },
        async getAreaFun () {
            this.loading = true;
            let { data, code } = await getAreaG();
            this.loading = false;
            (data || []).map(item => {
                if (item.list && item.list.length > 0) {
                    item.list.map(v => {
                        let objItem = { ...v };
                        objItem.areaName = item.areaBayName;
                        objItem.areaId = item.areaId;
                        this.listAreaData.push(objItem);
                    })
                } else {
                    let objItem = { ...item };
                    objItem.areaName = objItem.areaBayName;
                    this.listAreaData.push(objItem);
                }
            })
        },
        mergeRowMethod ({ row, _rowIndex, column, visibleData }) {
            const fields = ['areaName']
            const cellValue = row.areaId
            if (cellValue && fields.includes(column.property)) {
                const prevRow = visibleData[_rowIndex - 1]
                let nextRow = visibleData[_rowIndex + 1]
                if (prevRow && prevRow.areaId === cellValue) {
                    return { rowspan: 0, colspan: 0 }
                } else {
                    let countRowspan = 1
                    while (nextRow && nextRow.areaId === cellValue) {
                        nextRow = visibleData[++countRowspan + _rowIndex]
                    }
                    if (countRowspan > 1) {
                        return { rowspan: countRowspan, colspan: 1 }
                    }
                }
            }
        },

        cellClick ({ row }) {
            console.log(row)
            this.workArray = [];//焊接
            this.standbyArray = [];//待机
            this.warnArray = [];//故障
            this.searchObj.area = row.areaId || "";
            this.searchObj.bay = row.bayId || "";
            this.getList();
        },


        async getList () {
            let req = {
                ...this.searchObj
            }
            let { code, data } = await getWelderListNoPage(req);
            if (code == 200) {
                this.list = (data || []).map(item => {
                    let objItem = { ...item };
                    return objItem;
                });
            }
        },

        newMqtt () {
            const PahoMQTT = require('paho-mqtt');
            const name = new Date().getTime() + 'client';
            this.newClientMq = new PahoMQTT.Client(`${process.env.VUE_APP_MQTT_API}`, Number(8083), name);
            this.newClientMq.connect({
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: false,
                useSSL: false,
                onFailure: function (e) {
                    console.log(e);
                },
                reconnect: true,
                onSuccess: (res) => {
                    this.newClientMq.subscribe('rtcdata', {
                        qos: 0,
                        onSuccess: function (e) {
                            console.log("下发返回主题订阅成功：rtcdata");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    })
                }
            })
            this.newClientMq.onMessageArrived = ({ destinationName, payloadString }) => {
                if (destinationName == 'rtcdata') {
                    var datajson = JSON.parse(payloadString);
                    if (datajson.length > 0) {
                        this.setData(datajson);
                    }
                }
            }
        },

        //更新列表
        setData (arr) {
            //统计
            for (let b of arr) {
                let isThat = this.list.filter(item => parseInt(b.gatherNo) == parseInt(item.gatherNo));
                if (isThat.length > 0) {
                    this.totalNum(b);
                }
            }
        },

        totalNum (v) {
            switch (v.weldStatus) {
                case -1:
                    this.setOffNum(v);
                    break;
                case 0:
                    this.setStandbyArray(v);
                    break;
                case 3: case 5: case 7:
                    this.setWorkArray(v);
                    break;
                default:
                    this.setWarnArray(v);
                    break;
            }
        },

        //获取数值下标
        isArrNum (str, arr) {
            return arr.indexOf(str);
        },

        //关机状态
        setOffNum (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },

        //焊接状态
        setWorkArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //没有则添加
            if (workInNum === -1) {
                this.workArray.push(v.gatherNo);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },
        //待机状态
        setStandbyArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum === -1) {
                this.standbyArray.push(v.gatherNo);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },

        //故障状态
        setWarnArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum === -1) {
                this.warnArray.push(v.gatherNo);
            }
        },

        //
        goPage(row){
            this.$router.push({
                path:'/monitor-manager/real-time-area-detail',
                query:{
                    ...row
                }
            })
        }


    },
    created () {
        // this.styleObj = { ...this.styleC };
        // this.styleObj.width = this.styleObj.width + 'px';
        // this.styleObj.height = this.styleObj.height + 'px';
        // this.styleObj.left = this.styleObj.left + 'px';
        // this.styleObj.top = this.styleObj.top + 'px';
         this.newMqtt();
    },
    mounted () {

        // 图片地址
        var img_url = '/home_images/home.jpg';

        // 创建对象
        this.img = new Image();

        // 改变图片的src
        this.img.src = img_url;



        this.set = setInterval(() => {
            this.check();
        }, 40);

    }
}
</script>
<style lang="scss" scoped>
.home-bg {
    position: relative;
}
.home-bg img {
    vertical-align: middle;
}
.btn-box {
    position: absolute;
    cursor: pointer;
}
</style>
