<template>
    <div class="swipe-wrap flex-c">
        <section class="swipe-top flex">
            <h1 class="swipe-top-l">WELDMES信息化焊接派工终端</h1>
            <div class="swipe-top-r"><img
                    height="34"
                    src="/swipes/firsttop2.png"
                /></div>
        </section>
        <section class="swipe-middle flex">
            <div class="swipe-middle-con flex">
                <span>焊工编号：</span>
                <el-input
                    style="width:150px"
                    v-model="carNo"
                    @change="searchInput"
                ></el-input>
            </div>
        </section>
        <div class="swipe-bottom flex-c">
            <div class="swipe-bottom-t flex">
                <span class="swipe-bottom-t-l">{{time}}</span>
                <h2 class="swipe-bottom-t-m">设备运行</h2>
                <div class="swipe-bottom-t-r">
                    <el-select
                        size="mini"
                        v-model="area"
                        placeholder="请选择"
                        style="width:100px;margin-right:10px"
                        @change="changeArea"
                        clearable
                    >
                        <el-option
                            v-for="item in areaArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                    <el-select
                        v-model="bay"
                        size="mini"
                        placeholder="请选择"
                        style="width:130px"
                        @change="getList"
                        clearable
                    >
                        <el-option
                            v-for="item in straddleArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </div>
            </div>
            <div
                class="swipe-bottom-b"
                v-loading="loading"
            >

                <ul class="swipe-pro flex-n">
                    <li
                        v-for="item in list"
                        :key="item.id"
                        :class="{'taskCur':item.taskFlag}"
                    >
                        <i class="bind-tip">{{item.taskFlag?'已绑定':'空闲'}}</i>
                        <img :src="`/swipes/${imgType(item.typeStr)}${statusText(item.weldStatus).imgN}.png`" />
                        <span>({{item.macFlag===0?'OTC':'松下'}}) {{item.machineNo}}--{{item.gatherNo}}</span>
                    </li>
                </ul>

            </div>
        </div>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import moment from 'moment'
import { showLoading, hideLoading } from '@/utils/utilsCom'
import { login } from '_api/user.js'
import { setPublicToken, getPublicToken, removePublicToken, getToken } from '@/utils/auth'
import { getDictionaries, getWelderPeopleList } from '_api/productionProcess/process'
import { findByIdArea, getWelderListNoPage } from '_api/productionEquipment/production'
import './swipe.scss'
export default {
    components: {},
    props: {},
    data () {
        return {
            //mqtt
            newClientMq: {},

            area: '',
            bay: '',
            //区域
            areaArr: [],
            //跨间
            straddleArr: [],
            straddleArr2: [],


            time: '',
            date: '',
            timer: '',
            //设备列表
            list: [],
            loading: true,
            carNo: '',//10003118

            //
            offnum: 0,
            workArray: 0,
            standbyArray: 0,
            warnArray: 0,
        }
    },
    watch: {},
    computed: {},
    methods: {
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
            this.newClientMq.subscribe('jnOtcV1RtData', {
              qos: 0,
              onSuccess: function (e) {
                console.log("返回主题订阅成功：jnOtcV1RtData");
              },
              onFailure: function (e) {
                console.log(e);
              }
            });
            this.newClientMq.subscribe('jnSxRtData', {
              qos: 0,
              onSuccess: function (e) {
                console.log("返回主题订阅成功：jnSxRtData");
              },
              onFailure: function (e) {
                console.log(e);
              }
            })
          }
        })
        this.newClientMq.onMessageArrived = ({ destinationName, payloadString }) => {
          //otc设备实时监测
          if (destinationName == 'jnOtcV1RtData') {
            var datajson = JSON.parse(payloadString);
            console.log(datajson)
            if (datajson.length > 0) {
              if (!this.drawer) {
                //更新列表状态
                this.setData(datajson);
              } else {
                //获取曲线数据
                this.setLineData(datajson);
              }
            }
          }
          //sx设备实时监测
          if (destinationName == 'jnSxRtData') {
            var datajson = JSON.parse(payloadString);
            console.log(datajson)
            if (!this.drawer) {
              //更新列表状态
              this.setDataSx(datajson);
            } else {
              //获取曲线数据
              this.setLineDataSx(datajson);
            }
          }
        }
      },
        //更新列表
        setData (arr) {
            let v1 = arr.slice(-1)[0];
            this.offnum = 0;
            this.warnArray = 0;
            this.standbyArray = 0;
            this.workArray = 0;
            this.list.forEach(item => {
                if (parseInt(v1.gatherNo) == parseInt(item.gatherNo)) {
                    item.voltage = v1.voltage
                    item.electricity = v1.electricity
                    item.welderName = v1.welderName
                    item.taskNo = v1.taskNo
                    item.weldStatus = v1.weldStatus;//状态
                }
                this.totalNum(item.weldStatus);
            })
        },

        //更新松下列表
        setDataSx (b) {
            let v1 = { ...b };
            this.list.forEach(item => {
                if (parseInt(v1.weldCid) == parseInt(item.gatherNo)) {
                    item.voltage = v1.weldVol || '';
                    item.electricity = v1.weldEle || '';
                    item.welderName = v1.welderName || '';
                    item.taskNo = v1.taskNo || ''
                    item.weldStatus = v1.weldStatus;//状态
                }
            })
        },


        totalNum (v) {
            switch (v) {
                case -1:
                    this.offnum++;
                    break;
                case 0:
                    this.standbyArray++;
                    break;
                case 3: case 5: case 7:
                    this.workArray++;
                    break;
                default:
                    this.warnArray++;
                    break;
            }
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
              case 96:
                str = '设备锁定';
                imgN = 'O';
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


        async getList () {
            let req = {
                area: this.area,
                bay: this.bay
            }
            this.loading = true;
            let { data, code } = await getWelderListNoPage(req);
            this.loading = false;
            if (code == 200) {
                this.list = (data || []).map(item => {
                    let objItem = { ...item };
                    objItem.electricity = '';//电流
                    objItem.voltage = '';//电压
                    objItem.weldIp = '';//IP
                    objItem.weldStatus = -1;//状态
                    objItem.welderName = '';//操作人
                    objItem.taskNo = '';//任务编号
                    return objItem;
                });
              this.newMqtt();

            }
        },


        async loginFun () {
            showLoading();
            let { data, code } = await login({ username: 'test2', password: '123456' });
            hideLoading();
            if (code == 200) {
                setPublicToken(data.token);
                setTimeout(() => {
                    this.getDicFun();
                }, 0)

            }
        },
        //获取数据字典
        async getDicFun () {
            this.getList();
            if (this.carNo) {
                this.getWelderInfo(this.carNo);
            }

            let { data, code } = await getDictionaries({ "types": ["16", "17"] });
            if (code == 200) {
                this.areaArr = data['16'] || [];
                this.straddleArr2 = data['17'] || [];
            }
        },
        async changeArea (id) {
            if (id) {
                this.bay = "";
                let { code, data } = await findByIdArea({ id });
                if (code == 200) {
                    this.straddleArr = data || []
                }
            } else {
                this.bay = "";
                this.straddleArr = [];
            }

            this.getList();
        },
        searchInput (v) {
            this.getWelderInfo(v)
        },
        //获取焊工信息
        async getWelderInfo (welderNo) {
            console.log(welderNo)
            if (!welderNo || welderNo == "") {
                return this.$message.error("焊工编号不能为空！");
            }
            this.carNo = welderNo;
            if (!getToken() && !getPublicToken()) {
                this.loginFun();
            } else {
                let { code, data } = await getWelderPeopleList({ welderNo });
                if (code == 200) {
                    if (data.list && data.list.length > 0) {
                        sessionStorage.setItem("welderInfo", JSON.stringify(data.list[0]));
                        this.$router.push({
                            path: '/swipeInfo'
                        })
                    } else {
                        this.carNo = '';
                        this.$message.error("焊工编号不存在");
                    }
                }
            }

        }
    },
    created () {
        if (!getToken() && !getPublicToken()) {
            this.loginFun();
        } else {
            this.getDicFun();
        }
        this.timer = setInterval(() => {
            this.time = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
        }, 1000)
    },
    mounted () {
        window.mainPage = this;
    },
}
</script>
<style lang="scss">
.swipe-middle {
    height: 250px;
    background: url("/swipes/firstmid.png") no-repeat center -1px;
    background-size: cover;
    justify-content: center;
    align-items: center;
    .swipe-middle-con {
        width: 400px;
        height: 150px;
        background-color: rgba(0, 0, 0, 0.5);
        justify-content: center;
        align-items: center;
        color: #fff;
        border: 2px solid #6686c1;
    }
}
.swipe-bottom {
    flex: 1;
    height: 0px;
    .swipe-bottom-t {
        color: #fff;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
        .swipe-bottom-t-l,
        .swipe-bottom-t-m,
        .swipe-bottom-t-r {
            flex: 1;
        }
        .swipe-bottom-t-m {
            text-align: center;
        }
        .swipe-bottom-t-r {
            text-align: right;
        }
    }
    .swipe-bottom-b {
        flex: 1;
        background: #fff;
        height: 0px;
        overflow-y: scroll;
        border: 8px solid #143369;
        border-top: none;
        padding-top: 20px;
    }
}
.swipe-pro {
    padding-left: 10px;
    li {
        width: 122px;
        margin: 0 10px 10px 0px;
        cursor: pointer;
        border: 1px solid #ddd;
        text-align: center;
        padding-top: 6px;
        transition: all 0.3s ease 0s;
        position: relative;
        .bind-tip {
            position: absolute;
            top: 0px;
            right: 0px;
            font-size: 12px;
            color: #fff;
            background: rgb(1, 199, 83);
            padding: 1px 2px;
        }
        span {
            margin-top: 6px;
            display: block;
            font-size: 12px;
            line-height: 22px;
            background: #f8f8f8;
            padding-top: 0px;
            border-top: 1px solid #ddd;
            width: 100%;
            color: #333;
        }
        &.taskCur {
            .bind-tip {
                background: #f00;
            }
        }
        &:hover {
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.6);
            transform: scale(1.02);
        }
        &.current {
            border: 1px solid #aadef7;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.6);
            transform: scale(1.02);
            span {
                background: #aadef7;
            }
        }
    }
}
</style>
