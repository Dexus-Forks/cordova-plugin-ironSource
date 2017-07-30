package com.deineagentur.cordova.plugin.ironsource;

import android.util.Log;
import android.webkit.WebView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.ironsource.sdk.SSAFactory;
import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.EBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.InterstitialPlacement;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;


public class IronSourceAdsPlugin extends CordovaPlugin implements RewardedVideoListener, OfferwallListener, InterstitialListener {
    private CallbackContext eventContext;
    private static final String TAG = "[IronSourceAdsPlugin]";

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "Initializing IronSourceAdsPlugin");
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        IronSource.onPause(this.cordova.getActivity());
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        IronSource.onResume(this.cordova.getActivity());
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if(action.equals("subscribeOnNotifications")) {
            return this.subscribeOnNotifications(callbackContext);
        } else if(action.equals("init")) {
            String appKey = args.getString(0);
            String userId = args.getString(1);
            return this.init(appKey, userId);
        } else if(action.equals("showRewardedVideo")) {
            String placementName = null;
            if (args.length() == 1) {
                placementName = args.getString(0);
            }
            return this.showRewardedVideo(placementName);
        } else if(action.equals("getRewardedVideoPlacementInfo")) {
            String placementName = args.getString(0);
            return this.getRewardedVideoPlacementInfo(placementName, callbackContext);
        } else if(action.equals("isRewardedVideoPlacementCapped")) {
            String placementName = args.getString(0);
            return this.isRewardedVideoPlacementCapped(placementName, callbackContext);
        } else if(action.equals("setDynamicUserId")) {
            String userId = args.getString(0);
            return this.setDynamicUserId(userId);
        } else if(action.equals("loadInterstitial")) {
            return this.loadInterstitial();
        } else if(action.equals("isInterstitialReady")) {
            return this.isInterstitialReady(callbackContext);
        } else if(action.equals("getInterstitialPlacementInfo")) {
            String placementName = args.getString(0);
            return this.getInterstitialPlacementInfo(placementName, callbackContext);
        }  else if (action.equals("validateIntegration")) {
            IntegrationHelper.validateIntegration(this.cordova.getActivity());
            callbackContext.success();
            return true;
        } else if(action.equals("showInterstitial")) {
            String placementName = null;
            if (args.length() == 1) {
                placementName = args.getString(0);
            }
            return this.showInterstitial(placementName);
        } else if(action.equals("showOfferwall")) {
            String placementName = null;
            if (args.length() == 1) {
                placementName = args.getString(0);
            }
            return this.showOfferwall(placementName);
        }
        return false;
    }

    private boolean subscribeOnNotifications(CallbackContext callbackContext) {
        if (eventContext == null) {
            eventContext = callbackContext;
        }
        return true;
    }

    private void fireEvent(final String event) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:cordova.fireWindowEvent('" + event + "');");
            }
        });
    }

    private void fireEvent(final String event, final JSONObject data) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s', %s);", event, data.toString()));
            }
        });
    }

    private boolean init(String appKey, String userId) {
        IronSource.setRewardedVideoListener(this);
        IronSource.setOfferwallListener(this);
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        IronSource.setInterstitialListener(this);
        IronSource.setUserId(userId);
        IronSource.init(this.cordova.getActivity(), appKey);
        return true;
    }

    private boolean showRewardedVideo(String placementName) {
        IronSource.showRewardedVideo(placementName);
        return true;
    }

    private boolean getRewardedVideoPlacementInfo(String placementName, CallbackContext callbackContext) {
        Placement placement = IronSource.getRewardedVideoPlacementInfo(placementName);
        if (placement != null) {
            JSONObject event = new JSONObject();
            try {
                event.put("rewardName", placement.getRewardName());
                event.put("rewardAmount", placement.getRewardAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackContext.success(event);
        }
        else {
            callbackContext.error("placementName_invalid");
        }
        return true;
    }

    private boolean isRewardedVideoPlacementCapped(String placementName, CallbackContext callbackContext) {
        boolean isCapped = IronSource.isRewardedVideoPlacementCapped(placementName);
        if (isCapped) {
            callbackContext.error("capped");
        }
        else {
            callbackContext.success("ok");
        }
        return true;
    }

    private boolean setDynamicUserId(String userId) {
        IronSource.setDynamicUserId(userId);
        return true;
    }

    private boolean loadInterstitial() {
        IronSource.loadInterstitial();
        return true;
    }

    private boolean isInterstitialReady(CallbackContext callbackContext) {
        boolean isReady = IronSource.isInterstitialReady();
        if (isReady) {
            callbackContext.success("ready");
        }
        else {
            callbackContext.error("not_ready");
        }
        return true;
    }

    private boolean getInterstitialPlacementInfo(String placementName, CallbackContext callbackContext) {
        InterstitialPlacement placement = IronSource.getInterstitialPlacementInfo(placementName);
        if (placement != null) {
            JSONObject event = new JSONObject();
            try {
                event.put("placementName", placement.getPlacementName());
                event.put("placementId", placement.getPlacementId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackContext.success(event);
        }
        else {
            callbackContext.error("placementName_invalid");
        }
        return true;
    }


    private boolean showInterstitial(String placementName) {
        IronSource.showInterstitial(placementName);
        return true;
    }

    private boolean showOfferwall(String placementName) {
        IronSource.showOfferwall(placementName);
        return true;
    }

    // --------- IronSource Rewarded Video Listener ---------
    @Override
    public void onRewardedVideoAdOpened() {
        this.fireEvent("onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        this.fireEvent("onRewardedVideoAdClosed");
    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean b) {
        final JSONObject data = new JSONObject();
        try {
            data.put("videoAvailable", b);
        } catch (JSONException e) {
        }
        this.fireEvent("onRewardedVideoAvailabilityChanged", data);
    }

    @Override
    public void onRewardedVideoAdStarted() {
        this.fireEvent("onRewardedVideoAdStarted");
    }

    @Override
    public void onRewardedVideoAdEnded() {
        this.fireEvent("onRewardedVideoAdEnded");
    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {
        final JSONObject data = new JSONObject();
        try {
            data.put("placementName", placement.getPlacementName());
            data.put("rewardName", placement.getRewardName());
            data.put("rewardAmount", placement.getRewardAmount());
        } catch (JSONException e) {
        }
        this.fireEvent("onRewardedVideoAdRewarded", data);
    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
        final JSONObject data = new JSONObject();
        try {
            data.put("errorCode", ironSourceError.getErrorCode());
            data.put("errorMessage", ironSourceError.getErrorMessage());
        } catch (JSONException e) {
        }
        this.fireEvent("onRewardedVideoAdShowFailed", data);
    }

    // --------- IronSource Offerwall Listener ---------


    @Override
    public void onOfferwallAvailable(boolean b) {
        final JSONObject data = new JSONObject();
        try {
            data.put("offerAvailable", b);
        } catch (JSONException e) {
        }
        this.fireEvent("onOfferwallAvailable", data);
    }

    @Override
    public void onOfferwallOpened() {
        this.fireEvent("onOfferwallOpened");
    }

    @Override
    public void onOfferwallShowFailed(IronSourceError ironSourceError) {
        final JSONObject data = new JSONObject();
        try {
            data.put("errorCode", ironSourceError.getErrorCode());
            data.put("errorMessage", ironSourceError.getErrorMessage());
        } catch (JSONException e) {
        }
        this.fireEvent("onOfferwallShowFailed", data);
    }

    @Override
    public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
        final JSONObject data = new JSONObject();
        try {
            data.put("credits", credits);
            data.put("totalCredits", totalCredits);
            data.put("totalCreditsFlag", totalCreditsFlag);
        } catch (JSONException e) {
        }
        this.fireEvent("onOfferwallAdCredited", data);
        return false;
    }

    @Override
    public void onGetOfferwallCreditsFailed(IronSourceError ironSourceError) {
        final JSONObject data = new JSONObject();
        try {
            data.put("errorCode", ironSourceError.getErrorCode());
            data.put("errorMessage", ironSourceError.getErrorMessage());
        } catch (JSONException e) {
        }
        this.fireEvent("onGetOfferwallCreditsFailed", data);
    }

    @Override
    public void onOfferwallClosed() {
        this.fireEvent("onOfferwallClosed");
    }

    // --------- IronSource Interstitial Listener ---------


    @Override
    public void onInterstitialAdReady() {
        this.fireEvent("onInterstitialAdReady");
    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        final JSONObject data = new JSONObject();
        try {
            data.put("errorCode", ironSourceError.getErrorCode());
            data.put("errorMessage", ironSourceError.getErrorMessage());
        } catch (JSONException e) {
        }
        this.fireEvent("onInterstitialAdLoadFailed", data);
    }

    @Override
    public void onInterstitialAdOpened() {
        this.fireEvent("onInterstitialAdOpened");
    }

    @Override
    public void onInterstitialAdClosed() {
        this.fireEvent("onInterstitialAdClosed");
    }

    @Override
    public void onInterstitialAdShowSucceeded() {
        this.fireEvent("onInterstitialAdShowSucceeded");
    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
        final JSONObject data = new JSONObject();
        try {
            data.put("errorCode", ironSourceError.getErrorCode());
            data.put("errorMessage", ironSourceError.getErrorMessage());
        } catch (JSONException e) {
        }
        this.fireEvent("onInterstitialAdLoadFailed", data);
    }

    @Override
    public void onInterstitialAdClicked() {
        this.fireEvent("onInterstitialAdClicked");
    }

}