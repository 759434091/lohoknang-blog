<template>
    <mu-container class="bp-container">
        <mu-load-more @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
            <BlogIntro v-for="blogIntro in blogs"
                       :key="`bi_${blogIntro.id}`"
                       :blogIntro="blogIntro"
                       track-by="$index"/>
        </mu-load-more>
    </mu-container>
</template>

<script>
    import BlogIntro from '../components/BlogIntro'

    export default {
        name: 'BlogPage',
        data() {
            return {
                refreshing: false,
                loading: false,
                nextPage: 0,
                blogs: []
            }
        },
        created() {
            const blogs = JSON.parse(localStorage.getItem('blogs'))
            const nextPage = parseInt(localStorage.getItem('nextPage'))
            const scrollTop = parseInt(localStorage.getItem('scrollTop'))
            if (!blogs || !nextPage) {
                this.getPage()
                return
            }
            this.blogs = blogs
            this.nextPage = nextPage
            this.$nextTick(() => {
                document.getElementsByTagName('html')[0].scrollTop = scrollTop
            })
        },
        components: {
            BlogIntro
        },
        methods: {
            getPage() {
                this.loading = true;
                this.$request.getBlogs(this.nextPage)
                    .then((res) => {
                        this.blogs.push(...res.data)
                        this.nextPage += 1
                    })
                    .finally(() => this.loading = false)
            },
            refresh() {
                this.refreshing = true;
                this.nextPage = 0;
                this.$request.getBlogs(this.nextPage)
                    .then((res) => {
                        this.blogs = []
                        this.blogs.push(...res.data)
                        this.nextPage += 1
                    })
                    .finally(() => this.refreshing = false)
            },
            load() {
                this.getPage()
            }
        },
        beforeDestory() {
            debugger
            localStorage.setItem('scrollTop', document.getElementsByTagName('html')[0].scrollTop.toString());
            localStorage.setItem('nextPage', this.nextPage.toString());
            localStorage.setItem('blogs', JSON.stringify(this.blogs));
        }
    }
</script>

<style>
    .bp-container {
        margin-top: 1rem;
    }
</style>