<template>

    <div class="hello">
      <el-dialog title="创建" v-model="dialogVisble" width="50%" :before-close="close">
        <div><span>   金额：</span>
          <el-input v-model="am" placeholder="" style="width: 240px" @input="(v)=>(am=v.replace(/[^\d.]/g,''))" />
        </div>
        <h1></h1>
        <div><span>   数量：</span>
          <el-input v-model="qu" placeholder="" style="width: 240px" @input="(v)=>(qu=v.replace(/[^\d]/g,''))"/>
          </div>
          <h1></h1>
        <div><span>   名称：</span>
          <el-input v-model="na" placeholder="" style="width: 240px"/>
          </div>
          <h1></h1>
        <div><span>交易时间：</span>
            <el-date-picker v-model="ot"></el-date-picker>
          </div>
          <h1></h1>
        <div><span> 卖家ID：</span>
          <el-input v-model="si" placeholder="" style="width: 240px"/>
          </div>
          <h1></h1>
        <div><span> 交易ID：</span>
          <el-input v-model="oi" placeholder="" style="width: 240px"/>
          </div>
          <h1></h1>
        <div><span> 买家ID：</span>
          <el-input v-model="bi" placeholder="" style="width: 240px"/>
          </div>
          <h1></h1>
          <div>
            <el-switch v-model="v"  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                       active-text="加密"
                       inactive-text="不加密"/>
          </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">创 建</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>

  <script>
  import {  ref, watch } from 'vue'
  import { ElMessageBox } from "element-plus";
  import { getCurrentInstance } from "vue"
  import {createOrder} from "@/service/fabricServ";

  export default {
      props: {
        visible: {
          type: Boolean,
          default: false
        }
      },
      data(){
        return {
            v:false
        }
      },
      methods:{
      },
      setup(props, ctx) {

        const dialogVisible = ref(false)
        const am = ref(0)
        const qu = ref(0)
        const ot=ref("")
        const si=ref("")
        const oi=ref("")
        const na=ref("")
        const bi=ref("")
        const vis=ref(false)
        const close = () => {
          ctx.emit("update:visible", false);
        };

        const confirm = () => {
          if(ot.value==""||si.value==""||oi.value==""||bi.value==""||na.value=="") {
            ElMessageBox.confirm("缺少必填项", "提示", {
              confirmButtonText: "确认",
              cancelButtonText: "取消",
              type: "warning",
            }).then(() => {
            }).catch(() => {
                // catch error
            });
          }else{
            createOrder({
              "orderId":oi.value,
              "name":na.value,
              "quantity":qu.value,
              "amount":am.value,
              "orderTime":ot.value,
              "buyerId":bi.value,
              "sellerId":si.value,
              "encrypt":getCurrentInstance().data.v
            })
            ctx.emit("update:visible", false);
          }
        }
        watch(() => props.visible,(val) => {
          console.log(val);
          dialogVisible.value = val
        });

        return {
          dialogVisble: dialogVisible,
          confirm,
          close,
          am,
          qu ,
         ot,
         si,
         oi,
         na,
         buyerId,
         vis
        }
      }
    }
</script>
