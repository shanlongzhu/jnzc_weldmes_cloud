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
    name: 'lineCom2',
    components: {},
    props: {
        voltageArr: {
            type: Array,
            default: []
        },
        timeArr: {
            type: Array,
            default: []
        }
    },
    data () {
        return {
            myChart: {},
            option: {}
        }
    },
    watch: {
        voltageArr: {
            handler: function (val, oldval) {
                setTimeout(() => {
                    let option = this.myChart.getOption();
                    console.log(option)
                    option.series[0].data = val;
                    this.myChart.setOption(option)
                }, 0)

            },
        }
    },
    computed: {},
    methods: {},
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
                right: '15%',
                bottom: '70'
            },
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    params = params[0];
                    var date = new Date(params.name);
                    return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
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
                data: this.timeArr,
                axisLabel: {
                    color: '#2b6bba',
                    formatter: function (data) {
                        return moment(data).format("YYYY/MM/DD") + "\n" + moment(data).format("HH:mm:ss:SSS")
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
                end: 10
            },
            {
                type: 'slider',
                start: 0,
                end: 10
            }],
            series: [{
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: this.voltageArr
            }]
        };
        this.myChart.setOption(this.option)

        window.addEventListener('resize', function () {
            this.myChart.resize()
        });
    }
}
</script>
<style lang="scss" scoped>
</style>