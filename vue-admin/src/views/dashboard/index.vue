<template>
    <div class="home-bg" ref="homeBg">
      <div class="btn-box"></div>
        <img
            src="../../assets/home_images/home.jpg"
        />
    </div>

    <!-- <canvas id="cvs" style="width:100%;height:100%">当前浏览器不支持canvas</canvas> -->
</template>

<script>
export default {
    components: {},
    props: {},
    data () {
        return {
          img:{},
          set:''
        }
    },
    watch: {},
    computed: {},
    methods: {

      // 定时执行获取宽高
        check() {
            // 只要任何一方大于0
            // 表示已经服务器已经返回宽高
            if (this.img.width > 0 || this.img.height > 0) {
              console.log(this.$refs.homeBg.clientHeight,this.$refs.homeBg.clientWidth)
              console.log(this.img.width,this.img.height)
               
                clearInterval(this.set);
            }
        }
    },
    created () { },
    mounted () {

        // 图片地址
        var img_url = '/home_images/home.jpg';

        // 创建对象
        this.img = new Image();

        // 改变图片的src
        this.img.src = img_url;

        

        this.set = setInterval(()=>{
          this.check();
        },40);

    }
}
</script>
<style lang="scss" scoped>
.home-bg{
  position: relative;
}
.home-bg img {
    vertical-align: middle;
}
.btn-box{
  position: absolute;
  background: #f00;
  left: 233px;
  top:910px;
  width: 133px;
  height: 194px;
}
</style>
