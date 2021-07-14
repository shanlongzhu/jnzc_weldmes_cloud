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
            const value = Math.random() * 600 + 10;
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
        for (var i = 0; i < 1000; i++) {
            data.push(randomData());
        }

        option = {
            title: {
                text: '电流'
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
            grid: {
                top:'40',
                right:'15%',
                bottom:'70'
            },
            xAxis: {
                type: 'time',
                splitLine: {
                    show: false
                },
                triggerEvent: true
            },
            yAxis: {
                type: 'value',
                min: 0,
                max: 700,
                // boundaryGap: [0, '100%'],
                splitLine: {
                    show: false
                }
            },
            dataZoom: [{
                type: 'slider',
                start: 0,
                end: 10
            },
            {
                type: 'inside',
                start: 0,
                end: 10
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
                    lte: 30,
                    color: '#f00',
                    // areaStyle: 'rgba(82,209,176,.2)'
                }, {
                    gt: 30,
                    lte: 550,
                    color: '#00A1EA',
                    // areaStyle: 'rgba(231,177,48,.2)'
                },
                {
                    gt: 550,
                    color: '#f00'
                }
                ],
            },
            series: [{
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: data,
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
            }]
        };

        setInterval(function () {            
                data.shift();
                data.push(randomData());            
            //alert(data[999].name)
            myChart.setOption({
                series: [{
                    type: 'line',//增加type字段
                    data: data,
                    markLine: {
                        symbol: "none",
                        data: [
                            [{
                                symbol: 'none',
                                x: '92%',
                                yAxis: data[999].value[1]
                            }, {
                                symbol: 'circle',
                                label: {
                                    normal: {
                                        position: 'start',
                                        formatter: '{c}'
                                    }
                                },
                                name: '实时数据',
                                value: data[999].value[1],
                                xAxis: data[999].value[0],
                                yAxis: data[999].value[1]
                            }],                            
                            {
                                yAxis: 550,
                                label: {
                                    show: 'true',
                                    position: 'end',
                                    formatter: '550(A)'
                                },
                                lineStyle: {
                                    normal: {
                                        color: "#fe460d",
                                        width: 1,
                                        type: "dashed"
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        label: {
                                            // position: 400,
                                            formatter: function (
                                                params
                                            ) {
                                                return (
                                                    params
                                                        .value +
                                                    "%"
                                                );
                                            }
                                        }
                                    }
                                }
                            },
                            {
                                yAxis: 30,
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
                                    formatter: '30(A)'
                                },
                                itemStyle: {
                                    normal: {
                                        label: {
                                            // position: 400,
                                            formatter: function (
                                                params
                                            ) {
                                                return (
                                                    params
                                                        .value +
                                                    "%"
                                                );
                                            }
                                        }
                                    }
                                }
                            }
                        ]
                    }
                }]
            });
        }, 1000);
        option && myChart.setOption(option);
        // window.addEventListener('resize', function () {
        //     myChart.resize()
        // });
    }
}
</script>
<style lang="scss" scoped>
</style>