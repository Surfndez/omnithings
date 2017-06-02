public static class AuthorizationUtil
{
    public static string makeAuthorizeString(String stringProgKey)
        {
            //Declare OAuth Header Parameters
            string signatureMethod = "HMAC-SHA1";
            string timestamp = Convert.ToString(GetTimestamp(DateTime.Now));
            string nonce = timestamp;

            StringBuilder oAuthParams = new StringBuilder();
            oAuthParams.Append("nonce=").Append(nonce)
                       .Append("&signature_method=").Append(signatureMethod)
                       .Append("&timestamp=").Append(timestamp);
            string oAuthParasURLEn = UrlEncodeUpperCase(oAuthParams.ToString());
            //string oAuthParasURLEn = System.Web.HttpUtility.UrlEncode(oAuthParams.ToString()).ToUpper();
            string signature = doHMacSH1(oAuthParasURLEn, stringProgKey);
            //string signature = HMACSHA1Encode(stringProgKey, oAuthParasURLEn);

            StringBuilder oAuth = new StringBuilder();
            oAuth.Append("signature_method=\"").Append(signatureMethod).Append("\",")
                 .Append("timestamp=\"").Append(timestamp).Append("\",")
                 .Append("nonce=\"").Append(nonce).Append("\",")
                 .Append("signature=\"").Append(signature).Append("\"");

            return oAuth.ToString();
        }
        public static double GetTimestamp(DateTime now)
        {
            //計算本地時差
            double tz = TimeZone.CurrentTimeZone.GetUtcOffset(DateTime.Now).TotalHours;

            DateTime gtm = new DateTime(1970, 1, 1);        //宣告一個GTM時間出來
            //DateTime utc = DateTime.UtcNow.AddHours(tz);    //宣告一個目前的時間
            double timestamp = ((TimeSpan)now.Subtract(gtm)).TotalMilliseconds;
            timestamp = Convert.ToInt64(timestamp);

            return timestamp;
        }
        public static string doHMacSH1(string input, string key)
        {
            string retVal = "";
            byte[] keyBytes = Encoding.UTF8.GetBytes(key);
            using (HMACSHA1 hmac = new HMACSHA1(keyBytes))
            {
                byte[] hashBytes = hmac.ComputeHash(Encoding.UTF8.GetBytes(input));
                string hashValue = Convert.ToBase64String(hashBytes);
                retVal = UrlEncodeUpperCase(hashValue);
            }
            return retVal;
        }
        public static string UrlEncodeUpperCase(string value)
        {
            value = System.Web.HttpUtility.UrlEncode(value);

            return System.Text.RegularExpressions.Regex.Replace(value, "(%[0-9a-f][0-9a-f])",
                                 delegate(System.Text.RegularExpressions.Match match)
                                 {
                                     string v = match.ToString();
                                     return v.ToUpper();
                                 }
                                );
        }
}