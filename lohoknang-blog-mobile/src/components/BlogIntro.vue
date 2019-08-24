<template>
    <mu-card class="bi-card"
             :raised="false">
        <mu-card-title
                :title="`${blogIntro.title.length > 15 ? `${blogIntro.title.substr(0,15)}...` : blogIntro.title}`"
                :sub-title="`${blogIntro.category} |
                             ${new Date(blogIntro.createdAt).toDateString()} |
                             ${blogIntro.author} |
                             ${blogIntro.videoNum}阅读`"
                @click="$router.push(`/blogs/${blogIntro.id}`)"></mu-card-title>
        <mu-card-text class="bi-card-text" v-text="`${blogIntro.intro}...`"></mu-card-text>
        <mu-card-actions>
            <mu-row justify-content="end">
                <mu-button flat :to="`/blogs/${blogIntro.id}`">CLICK TO READ</mu-button>
            </mu-row>
        </mu-card-actions>
    </mu-card>
</template>

<script>
    export default {
        name: 'BlogIntro',
        props: {
            blogIntro: Object
        },
    }
</script>

<style>
    .bi-card {
        margin: 1rem 1rem 1.5rem;
        box-shadow: 0 0 1px -1px rgba(0, 0, 0, .2), 0 1px 1px 0 rgba(0, 0, 0, .14), 0 1px 3px 0 rgba(0, 0, 0, .12);
    }

    .bi-card-text {
        padding-top: 8px;
        padding-bottom: 0;
    }
</style>
