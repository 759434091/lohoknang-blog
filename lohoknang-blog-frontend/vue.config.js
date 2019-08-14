const CompressionWebpackPlugin = require("compression-webpack-plugin");
const zopfli = require("@gfx/zopfli");
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i;
const BrotliPlugin = require("brotli-webpack-plugin");

// noinspection JSUnusedGlobalSymbols
module.exports = {
  configureWebpack: {
    externals: {
      vue: "Vue",
      "vue-router": "VueRouter",
      vuex: "Vuex",
      axios: "axios",
      "element-ui": "ELEMENT",
      marked: "marked"
    },
    plugins: [
      new CompressionWebpackPlugin({
        algorithm(input, compressionOptions, callback) {
          return zopfli.gzip(input, compressionOptions, callback);
        },
        compressionOptions: {
          numiterations: 15
        },
        minRatio: 0.99,
        test: productionGzipExtensions
      }),
      new BrotliPlugin({
        test: productionGzipExtensions,
        minRatio: 0.99
      })
    ]
  }
};
