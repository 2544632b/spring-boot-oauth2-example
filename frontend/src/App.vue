<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <span class="title">
      OAuth2 Provider
    </span>
  </header>
  <RouterView />
</template>
<style>
  header {
    display: block;
    padding: 15px;
    position: fixed;
    .title {
      color: #ffffff;
      font-size: 1.5em;
    }
  }
</style>