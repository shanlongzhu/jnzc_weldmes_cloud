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
        addData(v, t){
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
            }else{
                this.option.dataZoom[0].end = 0.5;
                this.option.dataZoom[1].end = 0.5;
            }
            this.myChart.setOption(this.option);
            this.myChart.hideLoading();

        },
        echartsLoading () {
            this.myChart.showLoading();
        }
    },
    created () {

    },
    mounted () {
        this.myChart = echarts.init(this.$refs.electricity);
        this.option = {
            grid: {
                top: '10',
                right: '50',
                left:'40',
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
                    lte: 12,
                    color: '#f00',
                    // areaStyle: 'rgba(82,209,176,.2)'
                }, {
                    gt: 12,
                    lte: 50,
                    color: '#00A1EA',
                    // areaStyle: 'rgba(231,177,48,.2)'
                },
                {
                    gt: 50,
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
                            yAxis: 50,
                            label: {
                                show: 'true',
                                position: 'end',
                                formatter: '50(V)'
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
                            yAxis: 12,
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
                                formatter: '12(V)'
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
</style>