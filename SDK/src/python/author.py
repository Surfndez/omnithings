import time
import hmac
from hashlib import sha1
import random, string
import urllib.parse
import base64

def hash_hmac(key, code, sha1):
    hmac_code = hmac.new(key.encode("utf-8"), code.encode("utf-8"), sha1).digest()
    return base64.b64encode(hmac_code).decode()

def authorization(key):
    timestamp = str(time.time()).replace('.', '')
    nonce = ''.join(random.choice(string.ascii_letters + string.digits) for x in range(20))
    enc_code = urllib.parse.quote_plus("nonce=" + nonce + "&signature_method=HMAC-SHA1&timestamp=" + timestamp)
    sha_enc_code = hash_hmac(key, enc_code, sha1)
    signature = urllib.parse.quote_plus(sha_enc_code)
    
    return "signature_method=\"HMAC-SHA1\",timestamp=\""+timestamp+"\",nonce=\""+nonce+"\",signature=\""+signature+"\""


if __name__=='__main__':
    print(authorization("12345678"))
