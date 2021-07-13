<template>
    <div
        style="height:100%;flex:1"
        ref="electricity"
    ></div>
</template>

<script>
import * as echarts from 'echarts';
export default {
    name: 'lineCom',
    components: {},
    props: {},
    data () {
        return {
        }
    },
    watch: {},
    computed: {},
    methods: {},
    created () { },
    mounted () {
        let myChart = echarts.init(this.$refs.electricity);
        var option;

        function randomData () {
            now = new Date(+now + oneDay);
            value = value + Math.random() * 21 - 10;
            return {
                name: now.toString(),
                value: [
                    [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'),
                    Math.round(value)
                ]
            };
        }

        var data = [];
        var now = +new Date(1997, 9, 3);
        var oneDay = 24 * 3600 * 1000;
        var value = Math.random() * 1000;
        for (var i = 0; i < 1000; i++) {
            data.push(randomData());
        }

        option = {
            title: {
                text: '电流'
            },
            legend: {
                top: '0',
                right: '0',
                bottom: '0',
                left: '0',
                show: false
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
                type: 'time',
                splitLine: {
                    show: false
                }
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                splitLine: {
                    show: false
                }
            },
            dataZoom: [{
                type: 'slider',
                show: true,
                start: 0,
                end: 10,
                handleSize: 8
            }],
            series: [{
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: data
            }]
        };

        setInterval(function () {

            for (var i = 0; i < 5; i++) {
                data.shift();
                data.push(randomData());
            }

            myChart.setOption({
                series: [{
                    data: data
                }]
            });
        }, 1000);

        option && myChart.setOption(option);
        window.addEventListener('resize', function () {
            myChart.resize()
        });
    }
}
</script>
<style lang="scss" scoped>
</style>