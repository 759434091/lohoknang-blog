<template>
    <mu-container class="bc-container" v-loading="loading">
        <mu-container v-show="!loading">
            <h1>{{blogContent.title}}</h1>
            <p
                    v-text="`分类 ${blogContent.category} |
                    作者 ${blogContent.author} |
                    发布于 ${new Date(blogContent.createdAt).toLocaleString()} |
                    最后修改于 ${new Date(blogContent.updatedAt).toLocaleString()} |
                    ${blogContent.viewNum} 阅读`"></p>
            <component v-bind:is="component " class="html-content" v-highlight/>
            <mu-flex justify-content="end">
                <mu-button class="bc-back" flat @click="goBack">
                    &lt;&lt;&nbsp;BACK
                </mu-button>
            </mu-flex>
        </mu-container>
        <mu-divider class="bc-divider"></mu-divider>
        <div v-show="commentShow && !loading" ref="comments" id="comments"></div>
    </mu-container>
</template>

<script>
    import showdown from 'showdown'
    import Vue from 'vue'
    import Gitment from 'gitment'

    const pattern = new RegExp('src=(\'.+\'|".+")')

    export default {
        name: 'BlogContent',
        props: {
            blogId: String
        },
        watch: {
            component(component) {
                if (null != component) {
                    this.loading = false
                }
            }
        },
        data() {
            return {
                loading: true,
                commentShow: false,
                blogContent: {},
                component: null
            }
        },
        methods: {
            goBack() {
                this.$router.back()
            }
        },
        created() {
            this.$request.getBlog(this.blogId)
                .then((res) => {
                    this.blogContent = res.data
                    const converter = new showdown.Converter()
                    const html = `${
                        converter
                            .makeHtml(res.data.content)
                            .split(' ')
                            .map((el) => {
                                if (pattern.test(el)) return `${el} class="img-small"`
                                return el
                            })
                            .join(' ')}`
                    const compiled = Vue.compile(`<div>${html}</div>`)
                    const cmp = {
                        render: compiled.render,
                        staticRenderFns: compiled.staticRenderFns
                    }
                    this.component = Vue.component('htmlContent', cmp)
                })

            //gitment
            const gitment = new Gitment({
                owner: '190434957',
                repo: 'a9043-blog-comment',
                oauth: {
                    client_id: 'ae6ef39965ee5356fb3b',
                    client_secret: '26d8d93fd3f0bf15d4b1b8f298011ccf6f454b56'
                }
            })
            this.$nextTick(() => {
                gitment.render(this.$refs.comments)
            })
            this.commentShow = true
        }
    }
</script>

<style>
    .bc-container {
        overflow-x: hidden;
        padding: 0.5rem;
    }

    .bc-back {
        padding: 0;
    }

    .bc-divider {
        margin: 1rem 0 1rem 0;
    }

    .img-small {
        width: 100%;
    }
</style>
