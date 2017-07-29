function IronSourceAds(appKey, userId) {

	cordova.exec(null, null, 'IronSourceAdsPlugin', 'init', [appKey, userId]);

	this.showRewardedAd = function(placementName, claimSpace, successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'showRewardedAd', [placementName]);
	};

	this.showInterstitial = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'showInterstitial', []);
	};

	this.showOfferwall = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'showOfferwall', []);
	};

	this.validateIntegration = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'validateIntegration', []);
	};

	this.isRewardedVideoAvailable = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'isRewardedVideoAvailable', []);
	};

	this.isInterstitialAdAvailable = function(successCallback, failureCallback) {
		cordova.exec(successCallback, failureCallback, 'IronSourceAdsPlugin', 'isInterstitialAdAvailable', []);
	};
}

if(typeof module !== undefined && module.exports) {

	module.exports = IronSourceAds;
}
