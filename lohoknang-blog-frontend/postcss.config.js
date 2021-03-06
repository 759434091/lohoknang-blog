// noinspection JSUnusedGlobalSymbols,SpellCheckingInspection
module.exports = {
  plugins: {
    autoprefixer: {},
    "postcss-px-to-viewport": {
      viewportWidth: 1440,
      viewportHeight: 900,
      unitPrecision: 3,
      viewportUnit: "vw",
      selectorBlackList: [".ignore", ".hairlines"],
      minPixelValue: 1,
      mediaQuery: false
    },
    "postcss-viewport-units": {
      filterRule: rule => rule.nodes.findIndex(i => i.prop === "content") === -1
    }
  }
};
