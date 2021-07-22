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
                  readonly
                    style="width:150px"
                    v-model="carNo"
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
                    >
                        <img src="/swipes/AT.png" />
                        <span>{{item.machineNo}}</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
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
            carNo: ''//10003118
        }
    },
    watch: {},
    computed: {},
    methods: {
        async getList () {
            let req = {
                area: this.area,
                bay: this.bay
            }
            this.loading = true;
            let { data, code } = await getWelderListNoPage(req);
            this.loading = false;
            if (code == 200) {
                this.list = data || [];

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
        //获取焊工信息
        async getWelderInfo (welderNo) {
            if(!welderNo||welderNo==""){
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
    li {
        width: 100px;
        margin: 0 10px 10px;
        cursor: pointer;
        border: 1px solid #ddd;
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
