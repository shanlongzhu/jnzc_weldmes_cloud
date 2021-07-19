<template>
    <div class="swipe-wrap flex-c">
        <section class="swipe-top flex">
            <h1 class="swipe-top-l">WELDMES信息化焊接派工终端</h1>
            <div class="swipe-top-r">{{time}}</div>
        </section>
        <section class="swipe-info flex">
            <div class="swipe-info-l flex-c">
                <div class="swipe-bottom flex-c">
                    <div class="swipe-bottom-t flex">
                        <h2 class="swipe-bottom-t-m">可选焊机</h2>
                        <div class="swipe-bottom-t-r">
                            <el-select
                                size="mini"
                                v-model="area"
                                placeholder="请选择"
                                style="width:100px;margin-right:10px"
                                @change="changeArea"
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
                                style="width:100px"
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
                    <div class="swipe-bottom-b">
                        <ul class="swipe-pro flex-n">
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>
                            <li>
                                <img src="/swipes/AT.png" />
                                <span>123</span>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>
            <div class="swipe-info-r flex-c">
                <div class="swipe-bottom-t flex">
                    <h2 class="swipe-bottom-t-m">可选焊机</h2>
                </div>
                <div class="welding-info">
                    <el-row>
                        <el-col :span="12">焊工姓名：1123</el-col>
                        <el-col :span="12">焊工编号：1123</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">所在班组：</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">焊机编号：1123</el-col>
                        <el-col :span="12">焊机类型：1123</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">任务焊工：</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">任务编号：</el-col>
                    </el-row>
                </div>
                <div class="swipe-bottom-t flex">
                    <h2 class="swipe-bottom-t-m">任务列表</h2>
                    <span>任务数：<b style="color:green;font-size:18px">143</b></span>
                </div>
                <div style="flex:1;height:0">
                    <vxe-table
                        border
                        :data="list"
                        size="mini"
                        height="auto"
                        :loading="loading"
                        auto-resize
                    >
                        <vxe-table-column
                            type="seq"
                            title="序号"
                            width="50"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="name"
                            title="任务号"
                            min-width="100"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="loginName"
                            title="任务状态"
                            width="120"
                        ></vxe-table-column>                        
                    </vxe-table>
                </div>
            </div>
        </section>
        <section class="tc p10"><el-button>确定</el-button><el-button>返回</el-button></section>
    </div>
</template>

<script>
import moment from 'moment'
import './swipe.scss'
export default {
    components: {},
    props: {},
    data () {
        return {
            areaArr: [],//区域
            area: '',
            straddleArr: [],//胯间
            bay: '',
            time: '',
            date: '',
            timer: ''
        }
    },
    watch: {},
    computed: {},
    methods: {},
    created () {
        this.timer = setInterval(() => {
            this.time = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
        }, 1000)
    },
    mounted () {

    }
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
}
.swipe-pro {
    li {
        width: 100px;
        margin: 0 10px 10px;
        cursor: pointer;
        span {
            display: block;
            text-align: center;
            font-size: 12px;
            padding-top: 6px;
            line-height: 18px;
        }
    }
}
.swipe-info {
    flex: 1;
    height: 0px;
    .swipe-bottom-t {
        color: #fff;
        justify-content: space-between;
        align-items: center;
        padding:0px 10px;
        height: 40px;
        background: #fb8136;
        margin-bottom:5px;
        .swipe-bottom-t-l,
        .swipe-bottom-t-m,
        .swipe-bottom-t-r {
            flex: 1;
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
        padding-top: 20px;
    }
    .swipe-bottom-t-m {
        text-align: left!important;
        color: #333;
        font-weight: bold;
    }
}
.swipe-info-l {
    flex: 1;
    width: 0px;
    height: 100%;
    padding: 0 5px;
}
.swipe-info-r {
    width: 400px;
}
.welding-info{
    color: #fff;
    padding:0px 10px;
    margin-bottom: 5px;
    background: #34588C;
    .el-row{
        padding: 10px 0;
    }
}
</style>