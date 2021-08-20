<template>
    <div>
        <router-view
            :key="key"
            v-if="$route.meta.noCache"
        />
        <keep-alive>
            <router-view
                :key="key"
                v-if="!$route.meta.noCache"
            />
        </keep-alive>
    </div>
</template>

<script>
export default {
    name: 'parentMain',
    computed: {
        cachedViews () {
            return this.$store.state.tagsView.cachedViews
        },
        key () {
            return this.$route.path
        }
    }
}
</script>