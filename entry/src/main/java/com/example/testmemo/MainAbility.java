package com.example.testmemo;

import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends AceAbility {
    private HiLogLabel mylabel = new HiLogLabel(HiLog.LOG_APP,101,"cqupt");

    @Override
    public void onStart(Intent intent) {
        ComputeInternalAbility.register(this);
        super.onStart(intent);
    }

    @Override
    public void onActive() {
        super.onActive();
        if (verifyCallingPermission("ohos.permission.DISTRIBUTED_DATASYNC") != IBundleManager.PERMISSION_GRANTED) {
            // 调用者无权限，做错误处理
            HiLog.debug(mylabel,"调用者无权限，做错误处理!");
            /*if (canRequestPermission("ohos.permission.DISTRIBUTED_DATASYNC")) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                requestPermissionsFromUser(
                        new String[] { "ohos.permission.DISTRIBUTED_DATASYNC" } , 0);
                HiLog.debug(mylabel,"首次申请或者用户未选择禁止且不再提示!");
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
                HiLog.debug(mylabel,"显示应用需要权限的理由，提示用户进入设置授权!");
            }*/
        }
        else
        {
            HiLog.debug(mylabel,"调用者有权限!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
