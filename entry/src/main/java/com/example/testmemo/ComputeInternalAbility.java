package com.example.testmemo;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceInternalAbility;
import ohos.app.AbilityContext;
import ohos.data.DatabaseHelper;
import ohos.data.distributed.common.Value;
import ohos.data.preferences.Preferences;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComputeInternalAbility extends AceInternalAbility {
  public static final String BUNDLE_NAME = "com.example.testmemo";

  public static final String ABILITY_NAME = "com.example.testmemo.ComputeInternalAbility";

  private HiLogLabel mylabel = new HiLogLabel(HiLog.LOG_APP,101,"cqupt");

  public static final int ERROR = -1;

  public static final int SUCCESS = 0;

  public static final int GET_RECORD = 0;

  public static final int SAVE_RECORD = 1;

  private static ComputeInternalAbility instance;


  private AbilityContext abilityContext;



  public ComputeInternalAbility() {


    super(BUNDLE_NAME, ABILITY_NAME);
  }

  public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
    HiLog.error(mylabel, "onRemoteRequest ." + code);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("code", SUCCESS);
    switch (code) {
      case SAVE_RECORD:
        String dataStr = data.readString();
        try {
          ZSONObject object = ZSONObject.stringToZSON(dataStr);
          String key = object.getString("key");
          String value = object.getString("value");
          HiLog.error(mylabel, "onRemoteRequest SAVE_RECORD " + key + "," + value);
          DatabaseHelper databaseHelper = new DatabaseHelper(abilityContext);
          String filename = "cqupt";
          Preferences preferences = databaseHelper.getPreferences(filename);
          preferences.putString(key,value);
        } catch (RuntimeException e) {
          HiLog.error(mylabel, "convert failed.");
          result.put("code", ERROR);
        }
        sendResult(reply, result,true);
        break;
      case GET_RECORD:
        try {
          DatabaseHelper databaseHelper = new DatabaseHelper(abilityContext);
          String filename = "cqupt";
          Preferences preferences = databaseHelper.getPreferences(filename);
          Map<String, ?> records = preferences.getAll();

          ArrayList<SampleItem> list = new ArrayList<>();
          for (String key : records.keySet()) {
            list.add(new SampleItem(key, (String)records.get(key)));
          }

          result.put("recordList", list);
          HiLog.error(mylabel, "onRemoteRequest GET_RECORD " + list.toString());
        } catch (RuntimeException e) {
          HiLog.error(mylabel, "convert failed.");
          result.put("code", ERROR);
        }
        sendResult(reply, result,true);
        break;
      default: {
        result.put("code", ERROR);
        sendResult(reply, result,true);
        return false;
      }
    }
    return true;
  }

  private boolean sendResult(MessageParcel reply, Map<String, Object> result, boolean isSync) {
    if (isSync) {
      String temp = ZSONObject.toZSONString(result);
      HiLog.debug(mylabel,temp);
      reply.writeString(ZSONObject.toZSONString(result));
    } else {
      MessageParcel response = MessageParcel.obtain();
      response.writeString(ZSONObject.toZSONString(result));
      IRemoteObject remoteReply = reply.readRemoteObject();
      try {
        remoteReply.sendRequest(0, response, MessageParcel.obtain(), new MessageOption());
        response.reclaim();
      } catch (RemoteException exception) {
        return false;
      }
    }
    return true;
  }

  public static void register(AbilityContext abilityContext) {
    instance = new ComputeInternalAbility();
    instance.onRegister(abilityContext);
  }

  private void onRegister(AbilityContext abilityContext) {
    HiLog.debug(mylabel,"service register!");
    this.abilityContext = abilityContext;
    this.setInternalAbilityHandler(this::onRemoteRequest);
  }

  public static void deregister() {
    instance.onDeregister();
  }

  private void onDeregister() {
    abilityContext = null;
    this.setInternalAbilityHandler(null);
    HiLog.debug(mylabel,"service deregister!");
  }

  class SampleItem
  {
    String mTitle;
    String mString;

    public SampleItem(String t,String s) {
      this.mTitle = t;
      this.mString = s;
    }

    String getmTitle() {
      return mTitle;
    }

    void setmTitle(String t){
      this.mTitle = t;
    }

    String getmString(){
      return mString;
    }

    void setmString(String s){
      this.mString = s;
    }

  }

}
