import router from '@system.router';
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;

const GET_RECORD = 0;
export default {
data: {
    todolist: [{
        title:'title0',
        content:'test'
               }
    ],
    tag: "mainpage"
},
onInit() {
    console.info("onInit " + this.tag);
},
onReady() {
    console.info("onReady " + this.tag);
},
onShow() {
    console.info("onShow " + this.tag);
    this.getList();
},
onHide() {
    console.info("onHide " + this.tag);
},
newNote() {
    console.info("click new Note");
    router.push({
        uri: 'pages/editpage/editpage',
        params: {
            titleStr: 'title'+this.todolist.length,
            titleContent: '',
        },
    });
},
    async getList() {
        console.info("getList ");
        // JS FA 调用 PA 发送方法调用获取数据列表
        var action = {};
        action.bundleName = 'com.example.testmemo';
        action.abilityName = 'com.example.testmemo.ComputeInternalAbility';
        action.messageCode = GET_RECORD;
        action.data = {};
        action.abilityType = ABILITY_TYPE_INTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);
        console.info("getList result ");
        if (ret.code == 0) {
            // 数据处理
            var test;
            test=JSON.stringify(ret.recordList);
            console.info('getList before result is:' + JSON.stringify(ret.recordList) + ",todolist="+this.todolist);
            var list = [];
            for (var index = 0; index < ret.recordList.length; index++) {
                const element = {};
                element.title = ret.recordList[index].mTitle;
                element.content = ret.recordList[index].mString;
                list.push(element);
            }
            this.todolist = list;
            console.info('getList result is:' + JSON.stringify(ret.recordList) +
            ",todolist="+this.todolist+",size="+this.todolist.length);
        } else {
            console.info('getList error code:' + JSON.stringify(ret.code));
        }
    },
    editItem(title, content) {
        console.info("editItem Note " + title + ":" + content);
        router.push({
            uri: 'pages/editpage/editpage',
            params: {
                titleStr: title,
                titleContent: content,
            },
        });
    }
}
