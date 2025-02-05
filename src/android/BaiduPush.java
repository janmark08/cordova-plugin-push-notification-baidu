package com.cordova.plugins.push.baidu;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * Baidu Push Plugin for Cordova
 * 
 * This plugin integrates the Baidu Push SDK for Android.
 */
public class BaiduPush extends CordovaPlugin {
    /** Log TAG */
    private static final String LOG_TAG = BaiduPush.class.getSimpleName();

    /** JS callback contexts */
    public static CallbackContext pushCallbackContext = null;
    public static CallbackContext onMessageCallbackContext = null;
    public static CallbackContext onNotificationClickedCallbackContext = null;
    public static CallbackContext onNotificationArrivedCallbackContext = null;

    /**
     * Plugin initialization
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        LOG.d(LOG_TAG, "BaiduPush#initialize");
        super.initialize(cordova, webView);
    }

    /**
     * Plugin entry point for executing actions
     */
    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.d(LOG_TAG, "BaiduPush#execute: " + action);

        boolean ret = false;

        switch (action) {
            case "startWork":
                pushCallbackContext = callbackContext;
                startPushWork(args.getString(0));
                ret = true;
                break;

            case "stopWork":
                pushCallbackContext = callbackContext;
                stopPushWork();
                ret = true;
                break;

            case "resumeWork":
                pushCallbackContext = callbackContext;
                resumePushWork();
                ret = true;
                break;

            case "setTags":
                pushCallbackContext = callbackContext;
                setTags(args);
                ret = true;
                break;

            case "delTags":
                pushCallbackContext = callbackContext;
                delTags(args);
                ret = true;
                break;

            case "onMessage":
                onMessageCallbackContext = callbackContext;
                keepCallbackAlive(callbackContext);
                ret = true;
                break;

            case "onNotificationClicked":
                onNotificationClickedCallbackContext = callbackContext;
                keepCallbackAlive(callbackContext);
                ret = true;
                break;

            case "onNotificationArrived":
                onNotificationArrivedCallbackContext = callbackContext;
                keepCallbackAlive(callbackContext);
                ret = true;
                break;

            default:
                LOG.e(LOG_TAG, "Invalid action: " + action);
                callbackContext.error("Invalid action");
                break;
        }

        return ret;
    }

    /**
     * Start Baidu Push service
     */
    private void startPushWork(final String apiKey) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                LOG.d(LOG_TAG, "PushManager#startWork");
                PushManager.startWork(
                    cordova.getActivity().getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    apiKey
                );
                keepCallbackAlive(pushCallbackContext);
            }
        });
    }

    /**
     * Stop Baidu Push service
     */
    private void stopPushWork() {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                LOG.d(LOG_TAG, "PushManager#stopWork");
                PushManager.stopWork(cordova.getActivity().getApplicationContext());
                keepCallbackAlive(pushCallbackContext);
            }
        });
    }

    /**
     * Resume Baidu Push service
     */
    private void resumePushWork() {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                LOG.d(LOG_TAG, "PushManager#resumeWork");
                PushManager.resumeWork(cordova.getActivity().getApplicationContext());
                keepCallbackAlive(pushCallbackContext);
            }
        });
    }

    /**
     * Set tags for Baidu Push
     */
    private void setTags(final JSONArray args) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                LOG.d(LOG_TAG, "PushManager#setTags");
                List<String> tags = extractTagsFromArgs(args);
                if (tags != null) {
                    PushManager.setTags(cordova.getActivity().getApplicationContext(), tags);
                }
                keepCallbackAlive(pushCallbackContext);
            }
        });
    }

    /**
     * Delete tags for Baidu Push
     */
    private void delTags(final JSONArray args) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                LOG.d(LOG_TAG, "PushManager#delTags");
                List<String> tags = extractTagsFromArgs(args);
                if (tags != null) {
                    PushManager.delTags(cordova.getActivity().getApplicationContext(), tags);
                }
                keepCallbackAlive(pushCallbackContext);
            }
        });
    }

    /**
     * Extract tags from JSONArray arguments
     */
    private List<String> extractTagsFromArgs(JSONArray args) {
        List<String> tags = null;
        if (args != null && args.length() > 0) {
            tags = new ArrayList<>();
            for (int i = 0; i < args.length(); i++) {
                try {
                    tags.add(args.getString(i));
                } catch (JSONException e) {
                    LOG.e(LOG_TAG, "Failed to extract tag: " + e.getMessage(), e);
                }
            }
        }
        return tags;
    }

    /**
     * Keep the callback alive for asynchronous operations
     */
    private void keepCallbackAlive(CallbackContext callbackContext) {
        if (callbackContext != null) {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    }
}