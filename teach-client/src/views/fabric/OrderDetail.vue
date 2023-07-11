<template>

    <div class="hello">
      <el-dialog title="详细信息" v-model="dialogVisible" width="50%" :before-close="close">
        <div><span>金额：</span>
          <span>{{am}}</span>
        </div>
        <h1></h1>
        <div><span>数量：</span>
          <span>{{qu}}</span></div>
          <h1></h1>
        <div><span>交易时间：</span>
          <span>{{ot}}</span></div>
          <h1></h1>
        <div><span>卖家id：</span>
          <span>{{si}}</span></div>
          <h1></h1>
        <div><span>卖家评价：</span>
          <span>{{sr}}</span></div>
          <h1></h1>
        <div><span>交易id：</span>
          <span>{{oi}}</span></div>
          <h1></h1>
        <div><span>名称：</span>
          <span>{{na}}</span></div>
          <h1></h1>
        <div><span>物流：</span>
          <span>{{lo}}</span></div>
          <h1></h1>
        <div><span>买家id：</span>
          <span>{{bi}}</span></div>
          <h1></h1>
        <div><span>买家评价：</span>
          <span>{{br}}</span></div>
          <h1></h1>
        <div><span>状态：</span>
          <span>{{st}}</span>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button type="primary" @click="confirm">关 闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
</template>

<script>
import {  ref, watch } from 'vue'
export default {
      props: {
        visible: {
          type: Boolean,
          default: false
        },
        amount:{
          type:Number,
          default:0
       },
       quantity:{
          type:Number,
          default:0
       },
       orderTime:{
          type:String,
          default:""
       },
       sellerId:{
          type:String,
          default:""
       },
       sellerReview:{
          type:String,
          default:""
       },
       orderId:{
          type:String,
          default:""
       },
       name:{
          type:String,
          default:""
       },
       logistics:{
          type:String,
          default:""
       },
       buyerId:{
          type:String,
          default:""
       },
       buyerReview:{
          type:String,
          default:""
       },
       status:{
          type:String,
          default:""
       },
      },
      data(){
      },
      methods:{
      },
      setup(props, ctx) {

        const dialogVisible = ref(false)
        const am = ref(0)
        const qu = ref(0)
        const ot=ref("")
        const si=ref("")
        const sr=ref("")
        const oi=ref("")
        const na=ref("")
        const lo=ref("")
        const bi=ref("")
        const br=ref("")
        const st=ref("")
        const close = () => {
          ctx.emit("update:visible", false);
        };
        function changeStatus(val){
            st.value=val
        }
        const confirm = () => {
          console.log('你点击了确定按钮')
          ctx.emit("update:visible", false);
        }

        watch(() => [props.visible ,props.amount,props.quantity,props.orderTime,props.sellerId,props.sellerReview,props.orderId,props.name,props.logistics,props.buyerId,props.buyerReview,props.status], (val) => {
          console.log(val);
          dialogVisible.value = val[0]
          am.value=val[1]
          qu.value=val[2]
          ot.value=val[3]
          si.value=val[4]
          sr.value=val[5]
          oi.value=val[6]
          na.value=val[7]
          lo.value=val[8]
          bi.value=val[9]
          br.value=val[10]
          st.value=val[11]
        });

        return {
          dialogVisible: dialogVisible,
          confirm,
          close,
          am,
          qu ,
          ot,
          si,
          sr,
          oi,
          na,
          lo,
          bi,
          br,
          st,
          changeStatus
        }
      }
    }
</script>
