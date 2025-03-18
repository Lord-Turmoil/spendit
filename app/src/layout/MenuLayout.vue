<template>
    <div class="MenuLayout">
        <ProfileCard />
        <div class="MenuLayout__divider"></div>
        <PanelView />
    </div>
</template>

<style scoped>
.MenuLayout__divider {
    height: 6px;
    margin: 5px auto;
}
</style>

<script setup lang="ts">
import ProfileCard from '~/components/ProfileCard.vue';
import PanelView from '~/view/PanelView.vue';
</script>
