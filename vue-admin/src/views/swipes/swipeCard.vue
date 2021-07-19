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
                <el-input style="width:150px"></el-input>
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

                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import moment from 'moment'
import {showLoading,hideLoading} from '@/utils/utilsCom'
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
            time:'',
            date:'',
            timer:'',
        }
    },
    watch: {},
    computed: {},
    methods: {},
    created () {
        showLoading();
        this.timer = setInterval(()=>{
            this.time = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
        },1000)
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
</style>