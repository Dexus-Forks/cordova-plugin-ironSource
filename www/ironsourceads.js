function IronSourceAds(appKey, userId, successCallback) {
	cordova.exec(successCallback, null, 'IronSourceAdsPlugin', 'init', [appKey, userId]);
	this.showRewardedVideo = function(placementName, successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'showRewardedVideo', [placementName]);
	};
	this.getRewardedVideoPlacementInfo = function(placementName, successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'getRewardedVideoPlacementInfo', [placementName]);
	};
    this.isRewardedVideoPlacementCapped = function(placementName, successCallback, failureCallback) {
        cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'isRewardedVideoPlacementCapped', [placementName]);
    };
	this.validateIntegration = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'validateIntegration', []);
	};
	this.setDynamicUserId = function(userId) {
        cordova.exec(null, null, 'IronSourceAdsPlugin', 'setDynamicUserId', [userId]);
    };
    this.loadInterstitial = function() {
        cordova.exec(null, null, 'IronSourceAdsPlugin', 'loadInterstitial', []);
    };
    this.isInterstitialReady = function(successCallback, failureCallback) {
        cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'isInterstitialReady', []);
    };
    this.getInterstitialPlacementInfo = function(placementName, successCallback, failureCallback) {
        cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'getInterstitialPlacementInfo', [placementName]);
    };
    this.showInterstitial = function(placementName) {
        cordova.exec(null, null, 'IronSourceAdsPlugin', 'showInterstitial', [placementName]);
    };
    this.showOfferwall = function(placementName) {
        cordova.exec(null, null, 'IronSourceAdsPlugin', 'showOfferwall', [placementName]);
    };
    this.subscribeOnNotifications = function(successCallback) {
        cordova.exec(successCallback, null, 'IronSourceAdsPlugin', 'subscribeOnNotifications', []);
    };
}

if(typeof module !== undefined && module.exports) {

	module.exports = IronSourceAds;
}
