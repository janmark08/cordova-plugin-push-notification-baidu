# cordova-plugin-baidu-push

-- 2017.02.17 Updated Android SDK 5.6.0.30
-- 2016.06.06 Updated iOS SDK 1.4.5, Android 5.0.0
-- 2015.06.17 IOS SDK
-- 2015.06.15 Android SDK【L2-4.4.1】

# Installation

	$ ionic plugin add cordova-plugin-push-baidu

# Usage

```js
var api_key = 'SJI7BJ4hg8k5BUJ6LWVXit35' // your api key

document.addEventListener("deviceready", function () {

  window.baiduPush.onMessage(function (result) {
    console.log('onMessage success', result);
  }, function () {
    console.error('onMessage fail', error);
  })

  window.baiduPush.onNotificationClicked(function (result) {
    console.log('onNotificationClicked success', result);
  }, function (error) {
    console.error('onNotificationClicked fail', error);
  })

  window.baiduPush.onNotificationArrived(function (result) {
    console.log('onNotificationArrived success', resultj);
  }, function (error) {
    console.error('onNotificationArrived fail', error);
  })

  window.baiduPush.startWork(api_key, function (result) {
    console.log('startWork success', resultj);
  }, function (error) {
    console.error('startWork fail', error);
  })
}, false);
```

# API

baiduPush.startWork

	baiduPush.startWork(api_key, cb_success);
	# api_key: Baidu cloud push api_key
	# cb_success: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
	    type: 'onbind', // method of the corresponding Android Service
	    data: {
	      appId: 'xxxxxxxx',
	      userId: 'yyyyy',
	      channelId: 'zzzzzz'
	    }
	  }

baiduPush.stopWork

	baiduPush.startWork(cb_success);
	# cb_success: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
	    type: 'onunbind', // method of the corresponding Android Service
	    errorCode: 'xxxxxx', // error code corresponding to Baidu's request
	    data: {
	      requestId: 'yyyyyy', // request ID corresponding to Baidu
	    }
	  }

baiduPush.resumeWork

	baiduPush.resumeWork(cb_success);
	# cb_success: callback method can call succeeds, the return value is structured as follows: Method with baiduPush.startWork

baiduPush.setTags

	baiduPush.setTags(tags, cb_success);
	# tags[]: tag name you want to set
	# cb_success: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
	    type: 'onsettags',
	    errorCode: 'xxxxxxxx',
	    data: {
	      requestId: 'yyyyy',
	      channelId: 'zzzzzz'
	      sucessTags: ['aaa', 'bbb', 'ccc'], // set success tag list
	      failTags: ['ddd', 'eee', 'fff'] // tag list settings failed
	    }
	  }

baiduPush.delTags

	baiduPush.delTags(tags, cb_success);
	# tags[]: tag name you want to remove
	# cb_success: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
	    type: 'ondeltags',
	    errorCode: 'xxxxxxxx',
	    data: {
	      requestId: 'yyyyy',
	      channelId: 'zzzzzz'
	      sucessTags: ['aaa', 'bbb', 'ccc'], // set success tag list
	      failTags: ['ddd', 'eee', 'fff'] // tag list settings failed
	    }
	  }

## onMessage

	baiduPush.onMessage(cb_success, failureCallback)

## onNotificationClicked

	baiduPush.onNotificationClicked(successCallback, failureCallback)
	# successCallback: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
		  "data": {
		    "title": "foo",
		    "description": "bar",
		    "customContentString": "{\"Additional Field Key\":\"Additional Field Value\"}"
		  },
		  "type": "onNotificationClicked"
		}

## onNotificationArrived

	baiduPush.onNotificationArrived(successCallback, failureCallback)
	# successCallback: callback method call succeeds, will not consider callback call fails, the return value is structured as follows:
	  #json: {
		  "data": {
		    "title": "foo",
		    "description": "bar",
		    "customContentString": "{\"Additional Field Key\":\"Additional Field Value\"}"
		  },
		  "type": "onNotificationArrived"
		}

# Other instructions:

1. Json Parameters on the type of callback method can return the following values ​​that correspond Android's Service of Baidu cloud push callback method onbind, onunbind, onsettags, ondeltags, onlisttags, onmessage, onnotificationclicked, onnotificationarrived

2. Since Baidu applications different android and ios, APP client can use the following method to determined it:

```js
ionic.Platform.isIOS()
ionic.Platform.isAndroid()
```
