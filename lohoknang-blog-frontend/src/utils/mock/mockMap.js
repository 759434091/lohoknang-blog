export default {
    '/blogs[^/]': {
        enable: true,
        data: [
            {
                "id": "5d4a74a98bd8238fbd8b59ed",
                "category": "java",
                "author": "a9043",
                "title": "Reactive Rpc4J",
                "intro": "\\n一个基于`reactor-netty`和`jProtobuf`开发的, 遵循 Protobuf RPC协议 实现的 简单响应式RPC框架\\n\\n[reactive-rpc4j-spring-boot-starter]",
                "videoNum": 4,
                "createdAt": "2019-04-18T10:55:01",
                "updatedAt": "2019-05-24T15:57:22"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59ec",
                "category": "java",
                "author": "a9043",
                "title": "函数式编程 & 响应式编程",
                "intro": "\\n## 函数式编程\\n\\n在函数式编程里面,  函数为第一等公民, 或者 纯函数是唯一的第一等公民(Pure function is the one and only first-class citizen). 函数式",
                "videoNum": 6,
                "createdAt": "2019-04-18T10:54:10",
                "updatedAt": "2019-05-24T15:57:38"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59eb",
                "category": "java",
                "author": "a9043",
                "title": "Spring Boot + WebFlux + ReactiveData 组合实践",
                "intro": "\\n\\n本文使用有支持非阻塞驱动的MongoDB作为数据库, 并且以MongoDB初始模式运行, 没有任何认证\\n\\n使用此组合你需要或者最好需要以下技能知识\\n\\n- Spring 框架\\n- Java 8 Strea",
                "videoNum": 116,
                "createdAt": "2019-03-29T09:18:26",
                "updatedAt": "2019-05-31T07:06:34"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59ea",
                "category": "java",
                "author": "a9043",
                "title": "基于 UNIX Socket 实现 RestTemplate",
                "intro": "\\n## 方式一 \\n\\n实现`ClientHttpRequestFactory`\\n\\n### 依赖\\n\\n利用了`junixsocket`这个 native 库以及其对 socket 的封装\\n\\n```xml\\n ",
                "videoNum": 163,
                "createdAt": "2019-02-05T15:03:43",
                "updatedAt": "2019-05-01T04:06:44"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e9",
                "category": "web",
                "author": "a9043",
                "title": "抓取虎牙直播弹幕流（WireShark&前端源码）",
                "intro": "<br>\\n网上搜索的时候发现，很多文档和博文都是过时的，目前各大直播网站都已经升级到使用`HTML5`中的`WebSocket`协议，以前的方法很明显是行不通的。\\n\\n## 前言\\n\\n（上个星期面试了一下百度，总共",
                "videoNum": 371,
                "createdAt": "2018-11-11T16:42:34",
                "updatedAt": "2019-05-31T08:29:32"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e8",
                "category": "java",
                "author": "a9043",
                "title": "基于JWT认证的SpringBootStarter模块",
                "intro": "<br>\\n<br>\\n  \\n## 前言\\n\\n一个基于 Spring Security 的 JWT Token认证模块。这是我对之前项目一直在使用的JWT认证的模块进行封装，也是第一次编写Spring-Boot-St",
                "videoNum": 211,
                "createdAt": "2018-10-31T06:43:47",
                "updatedAt": "2019-04-24T08:02:09"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e7",
                "category": "web",
                "author": "a9043",
                "title": "YiluStudio WIKI介绍",
                "intro": "\\n\\n[yilustudio.cn](http://www.yilustudio.cn)\\n\\n这个网站为一路工作室（Yilu Studio）官方WIKI以及论坛，于2018年10月28日建起。\\n\\n该网站为初学者以",
                "videoNum": 44,
                "createdAt": "2018-10-30T01:38:02",
                "updatedAt": "2019-04-03T11:08:29"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e6",
                "category": "java",
                "author": "a9043",
                "title": "SpringMVC 参数注入的两种方式",
                "intro": "# SpringMVC 参数注入的两种方式\\n\\njava\\n\\n之前有一篇博文写到如何用自定义JWT Token来保护你的项目，于是我很快在所有项目运用上了。\\n\\n[Spring Security 初探 & 自定义身",
                "videoNum": 34,
                "createdAt": "2018-10-30T01:23:09",
                "updatedAt": "2019-04-05T20:02:32"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e5",
                "category": "web",
                "author": "a9043",
                "title": "若饭1024程序员节闯关活动解",
                "intro": "讲真1024不应该是，，咳咳的节日嘛？(*^__^*) 嘻嘻……\\n\\n### 安利时间\\n\\n若饭是一个代餐粉品牌，和国外的soylent比较像。从口感上若饭是我觉得比较适口的，我在大一个大三都买过几箱若饭（七袋一箱，",
                "videoNum": 139,
                "createdAt": "2018-10-24T19:01:08",
                "updatedAt": "2019-05-17T07:24:55"
            },
            {
                "id": "5d4a74a98bd8238fbd8b59e4",
                "category": "Linux",
                "author": "a9043",
                "title": "使用 Expect 制作自动部署脚本",
                "intro": "\\n使用Webpack以及Maven将项目打包后需要将其上传到服务器，每次手动操作非常麻烦，使用热部署也不太符合我的使用习惯，于是发现 Linux 上有 expect 这个神程序。\\n\\n## Expect\\n\\nExp",
                "videoNum": 49,
                "createdAt": "2018-10-10T13:43:34",
                "updatedAt": "2019-05-06T09:11:17"
            }
        ]

    },
    '/blogs/': {
        enable: true,
        data: {
            "id":"5d4a74a98bd8238fbd8b59ed",
            "category":"java",
            "author":"a9043",
            "title":"Reactive Rpc4J",
            "content":"\t \n" +
                " \n" +
                "你有时是不是这样，当一切转淡的时候就想到放弃，到最后才知道原来自已一直寻寻觅觅的已经在身边，但是，放弃了，就再没有机会再拥有……\n" +
                "\n" +
                "杯子：“我寂寞，我需要水，给我一点水吧” \n" +
                "\n" +
                "主人：“好吧，拥有了想要的水，你就不寂寞了吗？” \n" +
                "\n" +
                "杯子：“应该是吧。”\n" +
                "\n" +
                "主人把开水倒进了杯子里。 \n" +
                "\n" +
                "水很热，杯子感到自已快被融化了。杯子想，这就是爱情的力量吧。\n" +
                "\n" +
                "水变温了，杯子感到很舒服。杯子想，这就是生活的感觉吧。\n" +
                "\n" +
                "水变凉了，杯子害怕了，怕什么他也不知道。杯子想，就是失去的滋味吧。\n" +
                "\n" +
                "水凉透了，杯子绝望了。杯子想，这就是缘份的“杰作”吧。\n" +
                "\n" +
                "杯子：“主人，快把水倒出来，我不需要了。” \n" +
                "\n" +
                "主人不在。杯子感到自已压抑死了，可恶的水，凉凉的，放在心里，感觉好难过。\n" +
                "\n" +
                "杯子奋力一晃，水终于走出了杯子的心里，杯子好开心。突然，杯子掉在地上。 \n" +
                "\n" +
                "杯子碎了，临死前，看见了它心里的每一个地方都有水的痕迹。它才知道，它爱水，它是如此的爱水，可是，它再也无法把水完整的放在心里了。\n" +
                "\n" +
                "杯子哭了，它的眼泪和水溶在一起，奢望着能用最后的力量再去爱水一次。\n" +
                "\n" +
                "仔细去想~~爱情~~其实长着一样的模样~~~\n" +
                "\n",
            "videoNum":4,
            "createdAt":"2019-04-18T10:55:01",
            "updatedAt":"2019-05-24T15:57:22"
        }
    }
}