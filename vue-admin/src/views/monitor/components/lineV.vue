<template>
    <div
        style="height:100%;flex:1"
        ref="electricity"
    ></div>
</template>

<script>
import moment from 'moment'
import * as echarts from 'echarts';
export default {
    name: 'lineV',
    components: {},
    props: {
    },
    data () {
        return {
            myChart: {},
            option: {},
        }
    },
    watch: {

    },
    computed: {},
    methods: {
        init (v, t) {
            this.option.series[0].data = v;
            this.option.xAxis.data = t;

            this.option.series[0].markLine.data[0][0].yAxis =  v.slice(-1)[0];
            this.option.series[0].markLine.data[0][1].yAxis =  v.slice(-1)[0];
            this.option.series[0].markLine.data[0][1].value =  v.slice(-1)[0]; 
            this.option.series[0].markLine.data[0][1].xAxis =  t.slice(-1)[0];       

            this.myChart.setOption(this.option);
            this.myChart.hideLoading();

        },
        echartsLoading () {
            this.myChart.showLoading();
        },
        echartsClear(){
            this.myChart.clear()
        }
    },
    created () {

    },
    mounted () {
        this.myChart = echarts.init(this.$refs.electricity);
        this.option = {
            grid: {
                top: '20',
                right: '80',
                left: '40',
                bottom: '30'
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
                max: 100,
                splitLine: {
                    show: false
                }
            },
            // visualMap: {
            //     top: 10,
            //     right: 10,
            //     textGap: 5,
            //     itemWidth: 15,
            //     show: false,
            //     orient: 'horizontal',
            //     left: 'center',
            //     pieces: [{
            //         lte: 12,
            //         color: '#f00',
            //         // areaStyle: 'rgba(82,209,176,.2)'
            //     }, {
            //         gt: 12,
            //         lte: 50,
            //         color: '#00A1EA',
            //         // areaStyle: 'rgba(231,177,48,.2)'
            //     },
            //     {
            //         gt: 50,
            //         color: '#f00'
            //     }
            //     ],
            // },
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
                        [{
                            symbol: 'none',
                            x: '92%',
                            yAxis: ''
                        }, {
                            symbol: 'circle',
                            label: {
                                normal: {
                                    position: 'start',
                                    formatter: '{c}V 实时数据'
                                }
                            },
                            name: '实时数据',
                            value: '',
                            xAxis: '',
                            yAxis: ''
                        }],
                        // {
                        //     yAxis: 50,
                        //     label: {
                        //         show: 'true',
                        //         position: 'end',
                        //         formatter: '50(V)'
                        //     },
                        //     lineStyle: {
                        //         normal: {
                        //             color: "#fe460d",
                        //             width: 1,
                        //             type: "dashed"
                        //         }
                        //     },

                        // },
                        // {
                        //     yAxis: 12,
                        //     lineStyle: {
                        //         normal: {
                        //             color: "#fe460d",
                        //             width: 1,
                        //             type: "dashed"
                        //         }
                        //     },
                        //     label: {
                        //         show: 'true',
                        //         position: 'end',
                        //         formatter: '12(V)'
                        //     },
                        // }
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
</style>