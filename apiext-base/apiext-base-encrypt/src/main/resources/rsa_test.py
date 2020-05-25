# -*- coding: utf-8 -*-
# 作者本人不熟悉python
# 以下代码仅用来测试
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
javaPublicPkcs8EncodeStr = 'WMyOLGKHMSqbq1WsheI1G1+vNPc/pK4zTzGZ3TV1DRTJAY8OUpvCINFcVfPOyNmVQCcnwPn58bsotfY59co/tFhFcCGZFYY8ohGtgTpnxnL7/Jpjk8EDuOzBy9p/+WKA18IsQawH50201vPj8WtFIDnwkDzAHGph5tskhcvZc5n9d5CHjJdlHCv/G/j5fA2FsxdOHhRD3+fWrHUJFgVYgpr7Ro+XfdQrRazPmsYE45Y3Il+GOl2cP4sWNh/0oI7ziakIAircLjZ4cELnK9HD755JpgP6vt+WpCGCnyvQdjLbHIyIgtz/VgRiG2di1vnhZ2yrMwXcKanUehpsFbDvNQ=='
javaPublicPkcs8DecoderBytes = base64.b64decode(javaPublicPkcs8EncodeStr)
print(javaPublicPkcs8DecoderBytes)
message = rsa_decrypt(javaPublicPkcs8DecoderBytes, privateKey)
print(message)

# 签名
signStr = rsa.sign(data.encode('UTF-8'), privateKey, 'SHA-256')
print(str(base64.b64encode(signStr), "UTF-8"))
hashMethod = rsa.verify(data.encode('UTF-8'), signStr, publicKey)
print(hashMethod)
