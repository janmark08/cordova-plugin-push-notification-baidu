package com.cordova.plugins.push.baidu;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Baidu Push Receiver for handling Baidu Push SDK callbacks.
 */
public class BaiduPushReceiver extends PushMessageReceiver {
    /** Log TAG */
    public static final String TAG = BaiduPushReceiver.class.getSimpleName();

    /** Callback types */
    private enum CB_TYPE {
        onBind,
        onUnbind,
        onSetTags,
        onDelTags,
        onListTags,
        onMessage,
        onNotificationClicked,
        onNotificationArrived
    }

    /** Queues for storing pending plugin results */
    private static final ArrayList<PluginResult> queuePushCallbackContext = new ArrayList<>();
    private static final ArrayList<PluginResult> queueOnMessageCallbackContext = new ArrayList<>();
    private static final ArrayList<PluginResult> queueOnNotificationClickedCallbackContext = new ArrayList<>();
    private static final ArrayList<PluginResult> queueOnNotificationArrivedCallbackContext = new ArrayList<>();

    /**
     * Called when the Baidu Push SDK binds to the server.
     */
    @Override
    public void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (errorCode == 0) {
                setStringData(data, "appId", appId);
                setStringData(data, "userId", userId);
                setStringData(data, "channelId", channelId);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onBind);
                sendSuccessData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", "Binding failed");
                jsonObject.put("code", errorCode);
                sendErrorData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.e(TAG, "Binding failed with errorCode: " + errorCode);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when a push message is received.
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString, int notifyId) {

        String responseString = "onMessage message=\"" + message
                + "\" customContentString=" + customContentString + "\" notifyId=" + notifyId;
        Log.d(TAG, "透传消息到达");
        Log.d(TAG, responseString);

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (!TextUtils.isEmpty(customContentString)) {
                setStringData(data, "message", message);
                setStringData(data, "customContentString", customContentString);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onMessage);
                sendSuccessData(queueOnMessageCallbackContext, BaiduPush.onMessageCallbackContext, jsonObject, true);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", "Empty push message");
                sendErrorData(queueOnMessageCallbackContext, BaiduPush.onMessageCallbackContext, jsonObject, true);
                Log.e(TAG, "Empty push message received");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when a notification is clicked.
     */
    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (!TextUtils.isEmpty(title)) {
                setStringData(data, "title", title);
                setStringData(data, "description", description);
                setStringData(data, "customContentString", customContentString);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onNotificationClicked);
                sendSuccessData(queueOnNotificationClickedCallbackContext, BaiduPush.onNotificationClickedCallbackContext, jsonObject, true);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", "Empty notification click content");
                sendErrorData(queueOnNotificationClickedCallbackContext, BaiduPush.onNotificationClickedCallbackContext, jsonObject, true);
                Log.e(TAG, "Empty notification click content");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when a notification arrives.
     */
    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContentString) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (!TextUtils.isEmpty(title)) {
                setStringData(data, "title", title);
                setStringData(data, "description", description);
                setStringData(data, "customContentString", customContentString);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onNotificationArrived);
                sendSuccessData(queueOnNotificationArrivedCallbackContext, BaiduPush.onNotificationArrivedCallbackContext, jsonObject, true);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", "Empty notification content");
                sendErrorData(queueOnNotificationArrivedCallbackContext, BaiduPush.onNotificationArrivedCallbackContext, jsonObject, true);
                Log.e(TAG, "Empty notification content");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when tags are set successfully or fail.
     */
    @Override
    public void onSetTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (errorCode == 0) {
                setArrayData(data, "successTags", successTags);
                setArrayData(data, "failTags", failTags);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onSetTags);
                sendSuccessData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", String.valueOf(errorCode));
                sendErrorData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.e(TAG, "Failed to set tags with errorCode: " + errorCode);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when tags are deleted successfully or fail.
     */
    @Override
    public void onDelTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (errorCode == 0) {
                setArrayData(data, "successTags", successTags);
                setArrayData(data, "failTags", failTags);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onDelTags);
                sendSuccessData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", String.valueOf(errorCode));
                sendErrorData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.e(TAG, "Failed to delete tags with errorCode: " + errorCode);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when tags are listed successfully or fail.
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (errorCode == 0) {
                setArrayData(data, "tags", tags);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onListTags);
                sendSuccessData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", String.valueOf(errorCode));
                sendErrorData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.e(TAG, "Failed to list tags with errorCode: " + errorCode);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Called when the Baidu Push SDK unbinds from the server.
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            if (errorCode == 0) {
                setStringData(data, "requestId", requestId);
                jsonObject.put("data", data);
                jsonObject.put("type", CB_TYPE.onUnbind);
                sendSuccessData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.d(TAG, jsonObject.toString());
            } else {
                setStringData(data, "errorCode", String.valueOf(errorCode));
                sendErrorData(queuePushCallbackContext, BaiduPush.pushCallbackContext, jsonObject, false);
                Log.e(TAG, "Failed to unbind with errorCode: " + errorCode);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Sends success data to the JavaScript callback.
     */
    private void sendSuccessData(ArrayList<PluginResult> queue, CallbackContext callbackContext, JSONObject jsonObject, boolean keepCallback) {
        Log.d(TAG, "BaiduPushReceiver#sendSuccessData: " + jsonObject.toString());
        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
        result.setKeepCallback(keepCallback);
        sendResultWithQueue(queue, callbackContext, result);
    }

    /**
     * Sends error data to the JavaScript callback.
     */
    private void sendErrorData(ArrayList<PluginResult> queue, CallbackContext callbackContext, JSONObject jsonObject, boolean keepCallback) {
        Log.d(TAG, "BaiduPushReceiver#sendErrorData: " + jsonObject.toString());
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, jsonObject);
        result.setKeepCallback(keepCallback);
        sendResultWithQueue(queue, callbackContext, result);
    }

    /**
     * Sends the result to the callback or queues it if the callback is not available.
     */
    private void sendResultWithQueue(ArrayList<PluginResult> queue, CallbackContext callbackContext, PluginResult result) {
        if (callbackContext == null) {
            Log.d(TAG, "BaiduPushReceiver#sendResultWithQueue: callbackContext is null, adding to queue");
            queue.add(result);
        } else {
            callbackContext.sendPluginResult(result);
            sendQueue(queuePushCallbackContext, BaiduPush.pushCallbackContext);
            sendQueue(queueOnMessageCallbackContext, BaiduPush.onMessageCallbackContext);
            sendQueue(queueOnNotificationClickedCallbackContext, BaiduPush.onNotificationClickedCallbackContext);
            sendQueue(queueOnNotificationArrivedCallbackContext, BaiduPush.onNotificationArrivedCallbackContext);
        }
    }

    /**
     * Sends queued results to the callback.
     */
    private void sendQueue(ArrayList<PluginResult> queue, CallbackContext callbackContext) {
        if (callbackContext != null) {
            for (PluginResult result : queue) {
                callbackContext.sendPluginResult(result);
            }
            queue.clear();
        }
    }

    /**
     * Adds a string value to a JSON object if the value is not empty.
     */
    private void setStringData(JSONObject jsonObject, String name, String value) throws JSONException {
        if (!TextUtils.isEmpty(value)) {
            jsonObject.put(name, value);
        }
    }

    /**
     * Adds an array value to a JSON object if the value is not null.
     */
    private void setArrayData(JSONObject jsonObject, String name, List<String> value) throws JSONException {
        if (value != null) {
            jsonObject.put(name, new JSONArray(value));
        }
    }
}