import router from '@system.router';
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
const BUNDLE_NAME = 'com.example.testability';
const ABILITY_NAME = 'com.example.testability.ServiceStub';
const SAVE_RECORD = 1;
export default {
    data: {
        titleStr: '',
        titleContent:''
    },
    onInit(){

    },
    onChange(e){
        this.titleContent = e.value;
    },
    async saveRecords() {
        console.info("save record");
        // JS FA 调用 PA 发送方法调用获取数据列表
        var action = {};
        action.bundleName = 'com.example.testmemo';
        action.abilityName = 'com.example.testmemo.ComputeInternalAbility';
        action.messageCode = SAVE_RECORD;
        action.data = {
            key: this.titleStr,
            value: this.titleContent
        };
        action.abilityType = ABILITY_TYPE_INTERNAL;
        action.syncOption = ACTION_SYNC;
        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);
        console.info("getList result ");
        if (ret.code == 0) {
            // 数据处理
            console.info('save success!')
            router.back();
        } else {
            console.info('getList error code:' + JSON.stringify(ret.code));
        }
    },
    async migrate() {
        console.info("click migrate ");
        // 请求迁移
        var result = await FeatureAbility.continueAbility();
        console.info("result:" + JSON.stringify(result)); },
    onStartContinuation: function () {
        // 判断当前的状态是不是适合迁移
        console.info("onStartContinuation");
        return true; },
    onCompleteContinuation: function (code) {
        // 迁移操作完成，code 返回结果
        console.info("nCompleteContinuation: code = " + code); },
    onSaveData: function (saveData) {
        // 数据保存到 savedData 中进行迁移。
        this.continueAbilityData.titleStr = this.titleStr;
        this.continueAbilityData.titleContent = this.titleContent;
        console.info("onSaveData: string = " + this.titleStr + "," + this.titleContent);
        var data = this.continueAbilityData;
        Object.assign(saveData, data)
        console.info("onSaveData: data = " + data.titleStr + data.titleContent); },
    onRestoreData: function (restoreData) {
        // 收到迁移数据，恢复。
        this.isRestore = true;
        this.continueAbilityData = restoreData;
        this.titleStr = this.continueAbilityData.titleStr;
        this.titleContent = this.continueAbilityData.titleContent;
        this.$refs.content.value = this.titleContent;
        console.info("onRestoreData: data = " + this.titleStr + ", " + this.titleContent);
    }
}
