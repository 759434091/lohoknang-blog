<template>
    <mu-container class="ap-container">
        <mu-row class="ap-row" align-items="center">
            <mu-card :raised="true">
                <mu-card-header title="ABOUT" sub-title="lohoknang.blog">
                    <mu-avatar slot="avatar">
                        <img src="../assets/myLogo.jpg">
                    </mu-avatar>
                </mu-card-header>
                <mu-card-text>
                    UESTC在读, 准入职。<br/>
                    <br/>
                    全栈开发，熟悉 Vue 全家桶 + Spring Boot 全家桶。会点Node.JS/Py写写爬虫之类好玩的。<br/>
                    <br/>
                    算法极其乐色。涉猎比较广泛，不定时更新。<br/>
                </mu-card-text>
                <mu-card-text style="padding-bottom: 0">
                    <mu-button class="ap-github-link" flat color="primary"
                               href="https://github.com/759434091/">
                        GITHUB: HOKNANG_LO
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
