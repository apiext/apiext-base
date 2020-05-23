# -*- coding: utf-8 -*-
import rsa
import base64


# 加密算法
def rsa_encrypt(message: str, publicKey):
    encrypt_message = rsa.encrypt(message.encode('UTF-8'), publicKey)
    return encrypt_message


# 解密算法
def rsa_decrypt(message, privateKey):
    message_str = rsa.decrypt(message, privateKey).decode()
    return message_str


# 公钥和私钥
with open('./public_key.pem', 'r') as f:
    publicKey = rsa.PublicKey.load_pkcs1(f.read().encode())
with open('./private_key.pem', 'r') as f:
    privateKey = rsa.PrivateKey.load_pkcs1(f.read().encode())

# 公钥加密私钥解密
data = "www.apiext.com;www.apiext.cn"
encript_data = rsa_encrypt(data, publicKey)
print("---------------------------公钥加密字符串-------------------------")
print(str(base64.b64encode(encript_data), "UTF-8"))
message = rsa_decrypt(encript_data, privateKey)
print(message)

# python 使用pkcs1的私钥解密java pkcs8的公钥加密的数据
javaPublicPkcs8EncodeStr = 'TmJx+emgbajEF/x7q2AyumMRgtYo/PhJo/oGL1OgfZjANhFaK2Jkc6W/NLbIT7nAXO3wIfOQPDH3q40b7y8pE7uMXJSlIbR0dARVWJ8DfMpcOmp+en7t0qQoqcLkIXO1vyqfx/eSGyu+DL7sbme1FbHvNu91Hj96O2ZYJxZ48ABuLVrDOA0fSnb4T1QAP06q9lMeG2oDlVhsQbIh+cQCTRNUbpCFsnsqq5g+MrbtApfpsKe1Tv4fdAnvVjafTyP2Ehaf3AMuqU2rY/sJ0Uj2Kviy7KDMeqYLQG06LpC1sqAcRwcL9J+jieL2t+2MF6mIQboX5/iFUUT70ufRDquDvw=='
javaPublicPkcs8DecoderBytes = base64.b64decode(javaPublicPkcs8EncodeStr)
print(javaPublicPkcs8DecoderBytes)
message = rsa_decrypt(javaPublicPkcs8DecoderBytes, privateKey)
print(message)
