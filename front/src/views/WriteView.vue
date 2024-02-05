<script setup lang="ts">
import {defineComponent, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
const title = ref("");
const content = ref();
const router = useRouter();

const write = function () {
  axios
      .post("/api/posts", {
        title: title.value,
        content: content.value,
      })
      .then(() => {
        router.replace({ name: "home" });
      });
};
</script>

<template class="">
  <div>
    <el-input v-model="title" placeholder="제목 입력해주세요."/>
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"></el-input>
  </div>
  <el-button type="primary" class="mt-2" @click="write()">글 작성!</el-button>
</template>

<style scoped>

</style>