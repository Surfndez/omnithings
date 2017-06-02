// npm install crypto-js first

var CryptoJS = require("crypto-js");

function composeAuthorization(progKey){
	
	var timestamp = Math.floor(Date.now() / 1000);	
	var nonce = randomString(20);
	var signature = "nonce=" + nonce + "&signature_method=HMAC-SHA1&timestamp=" + timestamp;
	
	signature = encodeURI(signature);	
	signature = CryptoJS.HmacSHA1(signature, progKey);
	
	return 'nonce="' + nonce + '",signature_method="HMAC-SHA1",timestamp="' + timestamp + '",signature="' +signature +'"';
}