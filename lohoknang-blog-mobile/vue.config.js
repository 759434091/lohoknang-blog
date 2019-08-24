module.exports = {
    outputDir: 'mobile-dist',
    configureWebpack: {
        resolve: {
            alias: {
                'vue$': 'vue/dist/vue.esm.js'
            }
        },
        externals: {
            'hljs': 'hljs',
            'vue': 'Vue',
            'vue-router': 'VueRouter',
            'axios': 'axios',
            'showdown': 'showdown'
        }
    }
}
