<template>
    <div
        class="home-bg"
        ref="homeBg"
        style="width:100%"
    >
        <!-- 部件 -->
        <div
            class="btn-box"
            :style="styleObj"
        >
          <span class="btn-box-t">部件车间</span>
            <div
                class="btn-box-inner"
                @click="showArea"
            ></div>
            <div class="btn-layer btn-layer3">
                <div class="btn-layer-inner flex">
                    <div
                        class="btn-layer-l"
                        style="flex:1"
                        ref="electricity"
                    ></div>
                    <div
                        class="btn-layer-r flex-n"
                        style="flex:1"
                    >
                        <div class="layer-r-item layer-r-1">
                            <div class="layer-item-inner">
                                <div><span>{{workArrayPer||0}}</span>%</div>
                                <div>工 作</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-2">
                            <div class="layer-item-inner">
                                <div><span>{{warnArrayPer||0}}</span>%</div>
                                <div>故 障</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-3">
                            <div class="layer-item-inner">
                                <div><span>{{standbyArrayPer||0}}</span>%</div>
                                <div>待 机</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-4">
                            <div class="layer-item-inner">
                                <div><span>{{offnumPer||0}}</span>%</div>
                                <div>关 机</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 曲面 -->
        <div
            class="btn-box btn-box2"
            :style="styleObj2"
        >
          <span class="btn-box-t">曲面车间</span>
            <div
                class="btn-box-inner"
                @click="showArea"
            ></div>
            <div class="btn-layer">
                <div class="btn-layer-inner flex">
                    <div
                        class="btn-layer-l"
                        style="flex:1"
                        ref="electricity2"
                    ></div>
                    <div
                        class="btn-layer-r flex-n"
                        style="flex:1"
                    >
                        <div class="layer-r-item layer-r-1">
                            <div class="layer-item-inner">
                                <div><span>{{workArrayPer||0}}</span>%</div>
                                <div>工 作</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-2">
                            <div class="layer-item-inner">
                                <div><span>{{warnArrayPer||0}}</span>%</div>
                                <div>故 障</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-3">
                            <div class="layer-item-inner">
                                <div><span>{{standbyArrayPer||0}}</span>%</div>
                                <div>待 机</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-4">
                            <div class="layer-item-inner">
                                <div><span>{{offnumPer||0}}</span>%</div>
                                <div>关 机</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 平直 -->
        <div
            class="btn-box btn-box3"
            :style="styleObj3"
        >
          <span class="btn-box-t">平直车间</span>
            <div
                class="btn-box-inner"
                @click="showArea"
            ></div>
            <div class="btn-layer btn-layer2">
                <div class="btn-layer-inner flex">
                    <div
                        class="btn-layer-l"
                        style="flex:1"
                        ref="electricity3"
                    ></div>
                    <div
                        class="btn-layer-r flex-n"
                        style="flex:1"
                    >
                        <div class="layer-r-item layer-r-1">
                            <div class="layer-item-inner">
                                <div><span>{{workArrayPer||0}}</span>%</div>
                                <div>工 作</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-2">
                            <div class="layer-item-inner">
                                <div><span>{{warnArrayPer||0}}</span>%</div>
                                <div>故 障</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-3">
                            <div class="layer-item-inner">
                                <div><span>{{standbyArrayPer||0}}</span>%</div>
                                <div>待 机</div>
                            </div>
                        </div>
                        <div class="layer-r-item layer-r-4">
                            <div class="layer-item-inner">
                                <div><span>{{offnumPer||0}}</span>%</div>
                                <div>关 机</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <img
            width="100%"
            src="../../assets/home_images/home2.jpg"
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
                <span
                    class="mr20"
                    style="font-size:16px"
                >关机:<strong>{{offnum||0}}</strong></span>
                <span
                    class="mr20"
                    style="color:#f00;font-size:16px"
                >故障:<strong>{{warnArray.length||0}}</strong></span>
                <span
                    class="mr20"
                    style="color:rgb(1, 209, 95);font-size:16px"
                >待机:<strong>{{standbyArray.length||0}}</strong></span>
                <span
                    class="mr20"
                    style="color:green;font-size:16px"
                >焊接:<strong>{{workArray.length||0}}</strong></span>
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
                        <span
                            class="cur c-blue"
                            @click="goPage(row)"
                        >查看详情</span>
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
import * as echarts from 'echarts';
export default {
    components: {},
    props: {},
    data () {
        return {
            //mqtt
            newClientMq: {},
            img: {},
            set: '',
            //部件
            styleObj: {},
            styleC: {
                width: 133,
                height: 194,
                top: 910,
                left: 233
            },
            //曲面
            styleObj2: {},
            styleC2: {
                width: 427,
                height: 201,
                top: 251,
                left: 155
            },
            //平直
            styleObj3: {},
            styleC3: {
                width: 380,
                height: 215,
                top: 476,
                left: 152
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

            loading: false,
            //部件模块
            myChart: {},
            //曲面模块
            myChart2: {},
            //平直模块
            myChart3: {},
            option: {},
          option2: {},
          option3: {},
        }
    },
    watch: {

    },
    computed: {

        offnum () {
            return ((this.list.length || 0) - (this.workArray.length + this.standbyArray.length + this.warnArray.length)) || 0
        },

        //关机百分比
        offnumPer () {
            let newNum = isNaN(this.offnum / this.list.length) ? 0 : this.offnum / this.list.length;
            return (newNum * 100).toFixed(1);
        },

        //故障百分比
        warnArrayPer () {
            let newNum = isNaN(this.warnArray.length / this.list.length) ? 0 : this.warnArray.length / this.list.length;
            return (newNum * 100).toFixed(1);
        },

        //待机百分比
        standbyArrayPer () {
            let newNum = isNaN(this.standbyArray.length / this.list.length) ? 0 : this.standbyArray.length / this.list.length;
            return (newNum * 100).toFixed(1);
        },

        //工作百分比
        workArrayPer () {
            let newNum = isNaN(this.workArray.length / this.list.length) ? 0 : this.workArray.length / this.list.length;
            return (newNum * 100).toFixed(1);
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
                //部件
                this.styleObj = { ...this.styleC };
                this.styleObj.width = this.styleObj.width / scale + 'px';
                this.styleObj.height = this.styleObj.height / scale + 'px';
                this.styleObj.left = this.styleObj.left / scale + 'px';
                this.styleObj.top = this.styleObj.top / scale + 'px';
                //曲面
                this.styleObj2 = { ...this.styleC2 };
                this.styleObj2.width = this.styleObj2.width / scale + 'px';
                this.styleObj2.height = this.styleObj2.height / scale + 'px';
                this.styleObj2.left = this.styleObj2.left / scale + 'px';
                this.styleObj2.top = this.styleObj2.top / scale + 'px';
                //平直
                this.styleObj3 = { ...this.styleC3 };
                this.styleObj3.width = this.styleObj3.width / scale + 'px';
                this.styleObj3.height = this.styleObj3.height / scale + 'px';
                this.styleObj3.left = this.styleObj3.left / scale + 'px';
                this.styleObj3.top = this.styleObj3.top / scale + 'px';
            }
        },

        showArea () {
            this.visable1 = true;
            this.getAreaFun();
            this.cellClick({ row: {} });
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
        //鼠标经过显示
        btnBoxOver (id) {
            this.cellClick({ row: { areaId: id } })
        },

        cellClick ({ row }) {
            this.workArray = [];//焊接
            this.standbyArray = [];//待机
            this.warnArray = [];//故障
            this.searchObj.area = row.areaId || "";
            this.searchObj.bay = row.bayId || "";
            this.getList(row.areaId || "",row.bayId || "");
        },


        async getList (areaId,bayId) {
            let req = {
              area:areaId,
              bay:bayId
            }
            let { code, data } = await getWelderListNoPage(req);
            if (code == 200) {
                this.list = (data || []).map(item => {
                    let objItem = { ...item };
                    return objItem;
                });
                this.setPieData(areaId);
            }
        },

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
                    this.newClientMq.subscribe('jnOtcV1RtData', {
                        qos: 0,
                        onSuccess: function (e) {
                            console.log("下发返回主题订阅成功：jnOtcV1RtData");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    })
                }
            })
            this.newClientMq.onMessageArrived = ({ destinationName, payloadString }) => {
                if (destinationName == 'jnOtcV1RtData') {
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
                let isThat = this.list.filter(item => parseInt(b.gatherNo) == parseInt(item.machineGatherInfo.gatherNo));
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
            this.setPieData();
        },

        //设置饼图值
        setPieData (areaId) {
          //部件
          if(areaId =='58'){
            this.option.series[1].data[0].value = this.list.length || 0;
            this.option.series[0].data = [
              { value: this.workArrayPer, name: '工作' },
              { value: this.warnArrayPer, name: '故障' },
              { value: this.standbyArrayPer, name: '待机' },
              { value: this.offnumPer, name: '关机' },
            ];
            this.myChart.setOption(this.option);
          }
          //曲面
          if(areaId =='60'){
            this.option2.series[1].data[0].value = this.list.length || 0;
            this.option2.series[0].data = [
              { value: this.workArrayPer, name: '工作' },
              { value: this.warnArrayPer, name: '故障' },
              { value: this.standbyArrayPer, name: '待机' },
              { value: this.offnumPer, name: '关机' },
            ];
            this.myChart2.setOption(this.option2);
          }
          //平直
          if(areaId =='59'){
            this.option3.series[1].data[0].value = this.list.length || 0;
            this.option3.series[0].data = [
              { value: this.workArrayPer, name: '工作' },
              { value: this.warnArrayPer, name: '故障' },
              { value: this.standbyArrayPer, name: '待机' },
              { value: this.offnumPer, name: '关机' },
            ];
            this.myChart3.setOption(this.option3);
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
        goPage (row) {
            this.$router.push({
                path: '/monitor-manager/real-time-area-detail',
                query: {
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

    },
    mounted () {
        // 图片地址
        var img_url = '/home_images/home2.jpg';
        // 创建对象
        this.img = new Image();
        // 改变图片的src
        this.img.src = img_url;
        this.set = setInterval(() => {
            this.check();
        }, 40);

        this.newMqtt();
        this.myChart = echarts.init(this.$refs.electricity);
        this.myChart2 = echarts.init(this.$refs.electricity2);
        this.myChart3 = echarts.init(this.$refs.electricity3);
        this.option = {
            tooltip: {
                show: false,
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            color: ['#1efbe9', '#07f001', '#fbd51e', '#017dfc'],
            legend: {
                orient: 'vertical',
                left: 10,
                show: false
            },
            series: [
                {
                    name: '实时监测',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    hoverAnimation: false,
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: false,
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: [
                        { value: 0, name: '工作' },
                        { value: 0, name: '故障' },
                        { value: 0, name: '待机' },
                        { value: 0, name: '关机' },
                    ]
                },
                {
                    name: '焊机总数',
                    type: 'pie',
                    radius: ['0%', '30%'],
                    avoidLabelOverlap: false,
                    hoverAnimation: false,
                    label: {
                        show: true,
                        position: 'center',
                        verticalAlign: 'bottom',
                        formatter: `{c}`,
                        lineHeight: 30,
                        color: '#1efbe9',
                        fontSize: 18,
                        fontWeight: '700',
                    },
                    labelLine: {
                        show: false
                    },
                    data: [
                        { value: 0, name: '焊机总数' }
                    ],
                    itemStyle: {
                        color: 'rgba(255, 0, 0, 0)'
                    }
                }
            ]
        };
      this.option2 = {
        tooltip: {
          show: false,
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        color: ['#1efbe9', '#07f001', '#fbd51e', '#017dfc'],
        legend: {
          orient: 'vertical',
          left: 10,
          show: false
        },
        series: [
          {
            name: '实时监测',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: false,
                fontSize: '30',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 0, name: '工作' },
              { value: 0, name: '故障' },
              { value: 0, name: '待机' },
              { value: 0, name: '关机' },
            ]
          },
          {
            name: '焊机总数',
            type: 'pie',
            radius: ['0%', '30%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              show: true,
              position: 'center',
              verticalAlign: 'bottom',
              formatter: `{c}`,
              lineHeight: 30,
              color: '#1efbe9',
              fontSize: 18,
              fontWeight: '700',
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 0, name: '焊机总数' }
            ],
            itemStyle: {
              color: 'rgba(255, 0, 0, 0)'
            }
          }
        ]
      };
      this.option3 = {
        tooltip: {
          show: false,
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        color: ['#1efbe9', '#07f001', '#fbd51e', '#017dfc'],
        legend: {
          orient: 'vertical',
          left: 10,
          show: false
        },
        series: [
          {
            name: '实时监测',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: false,
                fontSize: '30',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 0, name: '工作' },
              { value: 0, name: '故障' },
              { value: 0, name: '待机' },
              { value: 0, name: '关机' },
            ]
          },
          {
            name: '焊机总数',
            type: 'pie',
            radius: ['0%', '30%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              show: true,
              position: 'center',
              verticalAlign: 'bottom',
              formatter: `{c}`,
              lineHeight: 30,
              color: '#1efbe9',
              fontSize: 18,
              fontWeight: '700',
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 0, name: '焊机总数' }
            ],
            itemStyle: {
              color: 'rgba(255, 0, 0, 0)'
            }
          }
        ]
      };
        this.myChart.setOption(this.option);
        this.myChart2.setOption(this.option2);
        this.myChart3.setOption(this.option3);


        this.cellClick({ row: { areaId: '59' } })
      this.cellClick({ row: { areaId: '60' } })
      this.cellClick({ row: { areaId: '58' } })

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
  background: rgba(0,0,0,0.5);
  border: 2px solid #017dfc;
  box-shadow: 0 0 5px rgba(1,125,252,1);

}
.btn-box-t{
  color: #1efbe9;
  font-size: 16px;
  height: 100%;
  display: flex;
  flex-flow: row;
  width: 100%;
  align-items: center;
  justify-content: center;
}

// .btn-box2{
//     background: #f00;
// }

// .btn-box3{
//     background: #000;
// }

.btn-box .btn-box-inner {
    width: 100%;
    height: 100%;
    cursor: pointer;
}
.btn-layer {
    background: url("/home_images/home-layer.png") no-repeat left top;
    width: 400px;
    height: 160px;
    background-size: 400px auto;
    position: absolute;
    display: block;
  top:0px;
  right: -380px;
}

.btn-layer2 {
  background: url("/home_images/home-layer2.png") no-repeat left top;
  width: 400px;
  height: 194px;
  background-size: 400px auto;
  position: absolute;
  display: block;
  top:55px;
  right: -356px;
}
.btn-layer3 {
  background: url("/home_images/home-layer3.png") no-repeat left top;
  width: 350px;
  height: 262px;
  background-size: 350px auto;
  position: absolute;
  display: block;
  top:50px;
  right: -320px;
}

.btn-layer-inner {
    height: 136px;
    width: 326px;
    margin-top: 16px;
    margin-left: 60px;
}

.btn-layer2 .btn-layer-inner {
  height: 136px;
  width: 326px;
  margin-top: 52px;
  margin-left: 60px;
}

.btn-layer3 .btn-layer-inner {
  height: 136px;
  width: 326px;
  margin-top: 83px;
  margin-left: 2px;
}

.layer-r-item {
    width: 50%;
    color: #fff;
    font-size: 14px;
    display: flex;
    flex-flow: column;
    justify-content: center;
}
.layer-r-item .layer-item-inner {
    border-left: 4px solid #1efbe9;
    padding-left: 8px;
    line-height: 16px;
    font-size: 12px;
}
.layer-r-item .layer-item-inner div:first-child {
    padding-bottom: 2px;
}
.layer-r-item .layer-item-inner span {
    font-size: 18px;
    margin-right: 6px;
    color: #1efbe9;
    font-weight: bold;
}

.layer-r-2 .layer-item-inner {
    border-color: #07f001;
}
.layer-r-2 .layer-item-inner span {
    color: #07f001;
}

.layer-r-3 .layer-item-inner {
    border-color: #fbd51e;
}
.layer-r-3 .layer-item-inner span {
    color: #fbd51e;
}

.layer-r-4 .layer-item-inner {
    border-color: #017dfc;
}
.layer-r-4 .layer-item-inner span {
    color: #017dfc;
}
</style>
