<template>
    <div style="height:100%;flex:1;position:relative">
        <div class="input-box tc">
            <el-input-number
                size="mini"
                style="width:60px"
                v-model="min"
                :min="0"
                :max="max"
                placeholder=""
                :controls="false"
            ></el-input-number>
            <span style="padding:0 10px">-</span>
            <el-input-number
                size="mini"
                style="width:60px;margin-right:10px"
                v-model="max"
                :min="min"
                :max="62"
                placeholder=""
                :controls="false"
            ></el-input-number>
            <el-button
                size="mini"
                @click="setMaxMin"
            >调整</el-button>
        </div>
        <div
            ref="electricity"
            style="height:100%;flex:1;"
        >
        </div>
    </div>
</template>

<script>
import moment from 'moment'
import * as echarts from 'echarts';
export default {
    name: 'lineCom2',
    components: {},
    props: {
    },
    data () {
        return {
            myChart: {},
            option: {},
            min: 12,
            max: 50,
        }
    },
    watch: {

    },
    computed: {},
    methods: {
        addData (v, t) {
            this.option.series[0].data = this.option.series[0].data.concat(v)
            this.option.xAxis.data = this.option.xAxis.data.concat(t)
            this.myChart.setOption(this.option);

        },
        init (v, t) {
            this.myChart.clear()
            this.option.series[0].data = v;
            this.option.xAxis.data = t;
            if (v.length <= 100) {
                this.option.dataZoom[0].end = 100;
                this.option.dataZoom[1].end = 100;
            } else if (v.length <= 500) {
                this.option.dataZoom[0].end = 70;
                this.option.dataZoom[1].end = 70;
            } else if (v.length <= 2000) {
                this.option.dataZoom[0].end = 30;
                this.option.dataZoom[1].end = 30;
            } else {
                this.option.dataZoom[0].end = 0.5;
                this.option.dataZoom[1].end = 0.5;
            }
            this.myChart.setOption(this.option);
            this.myChart.hideLoading();

        },
        echartsLoading () {
            this.myChart.showLoading();
        },
        setMaxMin () {
            console.log(this.option)
            this.myChart.clear()
            this.option.series[0].markLine.data[0].yAxis = this.max;
            this.option.series[0].markLine.data[0].label.formatter = `${this.max}(A)`;
            this.option.series[0].markLine.data[1].yAxis = this.min;
            this.option.series[0].markLine.data[1].label.formatter = `${this.min}(A)`;
            this.option.visualMap.pieces[0].lte = this.min;
            this.option.visualMap.pieces[1].gt = this.min;
            this.option.visualMap.pieces[1].lte = this.max;
            this.option.visualMap.pieces[2].gt = this.max;
            this.myChart.setOption(this.option);

        }
    },
    created () {

    },
    mounted () {
        this.myChart = echarts.init(this.$refs.electricity);
        this.option = {
            title: {
                text: '电压'
            },
            grid: {
                top: '40',
                right: '50',
                left: '40',
                bottom: '70'
            },
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    return '日期：' + params[0].name + '<br/>电压：' + params[0].value + ' v';
                },
                axisPointer: {
                    animation: false
                }
            },
            xAxis: {
                type: 'category',
                splitLine: {
                    show: false
                },
                splitNumber: 1,
                data: this.xData,
                axisLabel: {
                    color: '#2b6bba',
                    formatter: function (data) {
                        return moment(data).format("YYYY/MM/DD") + "\n" + moment(data).format("HH:mm:ss")
                    },
                },
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                min: 0,
                max: 62,
                splitLine: {
                    show: false
                }
            },
            dataZoom: [{
                type: 'inside',
                start: 0,
                end: 30
            },
            {
                id: 'dataZoomX',
                type: 'slider',
                start: 0,
                end: 30
            }],
            visualMap: {
                top: 10,
                right: 10,
                textGap: 5,
                itemWidth: 15,
                show: false,
                orient: 'horizontal',
                left: 'center',
                pieces: [{
                    lte: this.min,
                    color: '#f00',
                    // areaStyle: 'rgba(82,209,176,.2)'
                }, {
                    gt: this.min,
                    lte: this.max,
                    color: '#00A1EA',
                    // areaStyle: 'rgba(231,177,48,.2)'
                },
                {
                    gt: this.max,
                    color: '#f00'
                }
                ],
            },
            series: [{
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: this.yData,
                lineStyle: {
                    normal: {
                        width: 1
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#00A1EA'
                    }
                },

                markLine: {
                    symbol: "none",
                    data: [
                        {
                            yAxis: this.max,
                            label: {
                                show: 'true',
                                position: 'end',
                                formatter: `${this.max}(V)`
                            },
                            lineStyle: {
                                normal: {
                                    color: "#fe460d",
                                    width: 1,
                                    type: "dashed"
                                }
                            },

                        },
                        {
                            yAxis: this.min,
                            lineStyle: {
                                normal: {
                                    color: "#fe460d",
                                    width: 1,
                                    type: "dashed"
                                }
                            },
                            label: {
                                show: 'true',
                                position: 'end',
                                formatter: `${this.min}(V)`
                            },
                        }
                    ]
                }

            }]
        };
        this.myChart.setOption(this.option)
        window.addEventListener('resize', () => {
            this.myChart.resize()
        });

    }
}
</script>
<style lang="scss" scoped>
.input-box {
    position: absolute;
    width: 100%;
    z-index: 20;
}
</style>