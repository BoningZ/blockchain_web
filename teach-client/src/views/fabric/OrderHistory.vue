<template>
  <div class="hello">
      <el-dialog title="历史订单" v-model="dialogVisible" width="100%" :before-close="close">
        <el-main>
            <el-table :data="tableData"  height="680px" style="width: 100%">
              <el-table-column label="修改时间"  prop="timestamp">
              </el-table-column>
              <el-table-column label="金额" width="80" prop="amount">
              </el-table-column>
              <el-table-column  label="数量"  prop="quantity"  width="80">
              </el-table-column>
              <el-table-column  label="交易时间"  prop="orderTime"  width="150">
              </el-table-column>
              <el-table-column  label="卖家id"  prop="sellerId"  width="120">
              </el-table-column>
              <el-table-column  label="卖家评价"  prop="sellerReview"  width="100">
              </el-table-column>
              <el-table-column  label="交易id"  prop="orderId"  width="120">
              </el-table-column>
              <el-table-column  label="名称"  prop="name"  width="80">
              </el-table-column>
              <el-table-column  label="物流"  prop="logistics"  width="80">
              </el-table-column>
              <el-table-column  label="买家id"  prop="buyerId"  width="120">
              </el-table-column>
              <el-table-column  label="买家评价"  prop="buyerReview"  width="100">
              </el-table-column>
              <el-table-column  label="状态"  prop="status"  width="90">
              </el-table-column>
            </el-table>
          <!-- 分页底部 -->
        </el-main>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="close">关 闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>

<script>
  import { reactive, ref, watch } from 'vue'
  import {getHistoryOrder} from "@/service/fabricServ";

  export default {
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      orderId:{
          type:String,
          default:""
      }
    },
    setup(props, ctx) {
      const dialogVisible = ref(false)
      const orderId=ref("")
      const tableData=reactive([])
      const close = () => {
        ctx.emit("update:visible", false);
      };
      watch(() => [props.visible,props.orderId] ,(val) => {
        dialogVisible.value = val[0]
        orderId.value=val[1]
        getHistoryOrder({'orderId':orderId.value}).then(response=>{
          tableData.length=0
          response.data[0].forEach(val=>{
            tableData.push(val)
          })
          tableData.forEach((v, i) => {
            tableData[i]=Object.assign({'timestamp':v.timestamp},v.order);
          })
        })
      });

      return {
        dialogVisible: dialogVisible,
        confirm,
        close,
        oi: orderId,
        tableData
      }
    }
  }
</script>
