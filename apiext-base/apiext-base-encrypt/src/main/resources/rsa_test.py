# -*- coding: utf-8 -*-
import rsa
import base64


# 加密算法
def rsa_encrypt(message: str, publicKey):
    encrypt_message = rsa.encrypt(message.encode(), publicKey)
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


