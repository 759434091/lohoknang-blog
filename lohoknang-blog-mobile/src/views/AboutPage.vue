<template>
    <mu-container class="ap-container">
        <mu-row class="ap-row" align-items="center">
            <mu-card :raised="true">
                <mu-card-header title="ABOUT" sub-title="ABOUT A9043's BLOG">
                    <mu-avatar slot="avatar">
                        <img src="../assets/myLogo.jpg">
                    </mu-avatar>
                </mu-card-header>
                <mu-card-media style="padding: 16px 0 16px 0">
                    <img src="../assets/blog_logo.jpg">
                </mu-card-media>
                <mu-card-text>
                    Aha~ 喜爱钻研，喜爱java，然而业余什么都学习一些
                </mu-card-text>
                <mu-card-text style="padding-bottom: 0">
                    <mu-button class="ap-github-link" flat color="primary"
                               href="https://github.com/759434091/a9043-blog">
                        GITHUB: a9043-blog
                    </mu-button>
                </mu-card-text>
                <mu-card-text style="padding-top: 0">
                    <mu-button class="ap-github-link" flat color="primary"
                               href="https://github.com/759434091/a9043-blog-back-end">
                        GITHUB: a9043-blog-back-end
                    </mu-button>
                </mu-card-text>
            </mu-card>
        </mu-row>
    </mu-container>
</template>

<script>
    export default {
        name: 'AboutPage'
    }
</script>

<style>
    .ap-container {
        padding: 2rem 1rem 0 1rem;
    }

    .ap-github-link {
        width: 100%;
        align-items: start;
    }
</style>
