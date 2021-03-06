/**元素自治的可视判断指令
 * 该指令用于滚动曝光元素的打点,使用方法
 *<div v-viewport="func" data-xxx="item" data-obtype="static | dynamic"></div>
 *func为曝光时的回调函数，元素的dataset属性将作为回调函数的参数
 *dataset中的obtype为static适合不动的元素, 仅页面滚动时进行可视判断, 默认为该类型
 *dataset中的obtype为dynamic适合动态的元素, 元素有变动即进行一次可视判断
 */
import Vue from "vue";

const inPostViewport = el => {
  let rect = el.getBoundingClientRect();
  return (
    rect.left >= 0 &&
    rect.top <=
      (window.innerHeight || document.documentElement.clientHeight) +
        rect.height -
        80 /*or $(window).height() */ &&
    rect.right <=
      (window.innerWidth ||
        document.documentElement.clientWidth) /*or $(window).width() */
  );
};

const setOb = (el, callback) => {
  if (MutationObserver) {
    const ob = new MutationObserver(callback);
    const obOptions = {
      attributes: true,
      childList: true
    };
    el._viewport.ob = ob;
    ob.observe(el, obOptions);
  } else {
    throw new Error("the MutationObserver is not support");
  }
};

Vue.directive("viewport", {
  inserted(el, binding) {
    let fn = () => {};
    if (typeof binding.value === "function") {
      fn = binding.value;
    } else {
      return;
    }

    let ifStill = Symbol.for("still");
    const PostViewportFunc = () => {
      if (inPostViewport(el)) {
        if (!el.dataset[ifStill]) {
          fn(el.dataset, true);
          el.dataset[ifStill] = true;
        }
      } else {
        el.dataset[ifStill] = false;
        fn(el.dataset, false);
      }
    };

    const obCallback = () => {
      if (inPostViewport(el)) {
        fn(el.dataset, true);
      } else {
        fn(el.dataset, false);
      }
    };

    PostViewportFunc();
    const obType = el.dataset["obtype"] || "static";
    el._viewport = {
      callback: PostViewportFunc
    };
    if (obType === "static") {
      window.addEventListener("scroll", PostViewportFunc, true);
    } else if (obType === "dynamic") {
      setOb(el, obCallback);
    } else {
      throw new Error(`the obtype in dataset don't have ${obType}`);
    }
  },
  unbind(el) {
    if (el._viewport != null) {
      const { callback } = el._viewport;
      window.removeEventListener("scroll", callback, true);
    }
    Reflect.deleteProperty(el, "_viewport");
  }
});
