# IronSource Ads Cordova Plugin

[![Greenkeeper badge](https://badges.greenkeeper.io/Dexus/cordova-plugin-ironSource.svg)](https://greenkeeper.io/)

Add support for [IronSource Ads](https://www.IronSource.com/) to your Cordova and Phonegap based mobile apps.

## How do I install it? ##

If you're like me and using [CLI](http://cordova.apache.org/):
```
cordova plugin add cordova-plugin-ironsource
```

or

```
cordova plugin add https://github.com/blakgeek/cordova-plugin-ironSource
```

or

```
phonegap local plugin add https://github.com/blakgeek/cordova-plugin-ironSource
```

## How do I use it? ##

```javascript
document.addEventListener('deviceready', function() {

	var isAds = new IronSourceAds("yo_app_key", "some_unique_userid");
	
	// show a rewarded ad
	isAds.showRewardedAd();
    	
	// show a rewarded ad for placement RightHere
	isAds.showRewardedAd("RightHere");
    
	// show an offerwall
    isAds.showOfferWall();
    
    // show an interstitial
    isAds.showInterstitial();

    // launch in Android Studio/Xcode mediation integration verification
    isAds.validateIntegration();

    // Check is a rewarded video is available to show
    isAds.isRewardedVideoAvailable(function () {
    	alert('Yes');
	}, function () {
		alert('No');
	});

	// Check is an interstitial is available to show
    isAds.isInterstitialAdAvailable(function () {
    	alert('Yes');
	}, function () {
		alert('No');	
	});
    
    // give em some credit
	window.addEventListener("offerwallCreditReceived", function(e) {
	    
	    var credit = e.credit;
	    
	    // The number of credits the user has earned.
	    console.log(credit.amount);
	    
	    // The total number of credits ever earned by the user.
	    console.log(credit.total):
	    
	    // estimated flag is the same as totalCreditsFlag 
	    // In some cases, we won’t be able to provide the exact
        // amount of credits since the last event (specifically if the user clears
        // the app’s data). In this case the ‘credits’ will be equal to the ‘totalCredits’,
        // and this flag will be ‘true’.
	    console.log(credit.estimated);
	    
	}, false);
	
	// reward your users
	window.addEventListener("rewardedVideoRewardReceived", function(e) {
		
		var placement = e.placement;
		console.log(placement.id); // only available on android
		console.log(placement.name);
		console.log(placement.reward);
		console.log(placement.amount);
	}, false);
    
}, false);
```

## Can I just see a working example?
Yep.  Check out the [demo project](https://github.com/Dexus/cordova-plugin-ironSource-demo).  It runs on both Android and iOS.

## What events are supported?
### Interstitial
1. onInterstitialAdClicked
1. onInterstitialAdClosed
1. onInterstitialAdLoadFailed
1. onInterstitialAdOpened
1. onInterstitialAdReady
1. onInterstitialAdShowFailed
1. onInterstitialAdShowSucceeded

### Offerwall
1. onOfferwallAdCredited
1. onGetOfferwallCreditsFailed
1. onOfferwallAvailable
1. onOfferwallClosed
1. onOfferwallOpened
1. onOfferwallShowFailed

### Rewarded Video
1. onRewardedVideoAdClosed
1. onRewardedVideoAdEnded
1. onRewardedVideoAdOpened
1. onRewardedVideoAdRewarded
1. onRewardedVideoAdShowFailed
1. onRewardedVideoAdStarted
1. onRewardedVideoAvailabilityChanged
