// npm install crypto-js first

var CryptoJS = require("crypto-js");

var ran = function(){return Math.random().toString(36).substring(7).substr(0,5);}
var composeAuthorization = function(progKey){
					
	var timestamp = Math.floor(Date.now() / 1000);	
	var nonce = ran()+ran()+ran()+ran();
	var signature = "nonce=" + nonce + "&signature_method=HMAC-SHA1&timestamp=" + timestamp;
					
	signature = encodeURIComponent(signature);	
	signature = CryptoJS.HmacSHA1(signature, progKey);
	signature = CryptoJS.enc.Base64.stringify(signature);
	signature = encodeURIComponent(signature);
	return 'nonce="' + nonce + '",signature_method="HMAC-SHA1",timestamp="' + timestamp + '",signature="' +signature +'"';
}
