<script setup lang="ts">
import {defineComponent, ref} from "vue";

const count = ref(0);
console.log(count);
</script>

<template>
  <div>
    <input type="text" placeholder="제목 입력해주세요."/>
    <textarea rows="15"></textarea>
  </div>
  <button>글 작성!</button>
</template>

<style scoped>

</style>