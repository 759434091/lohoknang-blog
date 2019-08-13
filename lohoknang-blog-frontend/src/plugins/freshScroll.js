import Vue from "vue";
//滚动距离
function getScrollTop()
{
    let scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
    if(document.body){
        bodyScrollTop = document.body.scrollTop;
    }
    if(document.documentElement){
        documentScrollTop = document.documentElement.scrollTop;
    }
    scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
    return scrollTop;
}
//文档总高度
function getScrollHeight(){
    let scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
    if(document.body){
        bodyScrollHeight = document.body.scrollHeight;
    }
    if(document.documentElement){
        documentScrollHeight = document.documentElement.scrollHeight;
    }
    scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight ;
    return scrollHeight;
}
//视口高度
function getWindowHeight(){
    let windowHeight = 0;
    if(document.compatMode == "CSS1Compat"){
        windowHeight = document.documentElement.clientHeight;
    }else{
        windowHeight = document.body.clientHeight;
    }
    return windowHeight;
}
Vue.directive("freshScroll", {
    inserted(el, binding) {
        console.log("滚动指令运行");
        let fn = null;
        if (typeof binding.value === "function") {
            fn = binding.value;
        } else {
            throw new Error("v-fresh-scroll directive need a function");
        }
        const tellBottom = function () {
            if (getScrollTop() + getWindowHeight() == getScrollHeight()) {
                console.log('滚动到底部了')
                fn()
            }
        }
        tellBottom()
        el._freshScroll = tellBottom;
        window.addEventListener("scroll", tellBottom, true);
    },
    unbind(el) {
        const { callback, ob } = el._viewport;
        // const obType = el.dataset['obtype'] || 'static'
        console.log("指令解绑l ", ob, "dddd");
        window.removeEventListener("scroll", callback, true);
        Reflect.deleteProperty(el, "_viewport");
    }
});
