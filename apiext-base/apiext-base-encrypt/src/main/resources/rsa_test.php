<?php
    // php的公钥要求pkcs8格式
	define('RSA_PUBLIC', '-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq4Tzx8Dj7pt1WAvTcM0p
92PqQ6RJt/WiargaG6mDrn7yRKNNbtWllHRnR+RPGnwNVp42HLy9kx/zpoMi+vxf
PWzhjshp2PtBKePhFbUoQPEGVwsFbbMj4NUm2ZmeNLE5Hj5uYAiEPvUtilzQsXdF
CrN8Y9RXYMr6oWkLDr2iYSALB9twPFAdpYs/RWXxc8iQx9QiWU1sn+zd/dfjyqBH
39kcO22Bqi77eadlcA75zo7PeImeBzxuPAgr3zMJT94n67f0mpMhzI769lpTg/Kn
skeKuDe/Mz/ayjUyga6mYif8lHy1E3JX+d164acekN0wuo4RA0seKO3VR//B2i26
1wIDAQAB
-----END PUBLIC KEY-----');

// php对私钥的要求 pkcs1和pkcs8都可以
define('RSA_PRIVATE','-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCrhPPHwOPum3VY
C9NwzSn3Y+pDpEm39aJquBobqYOufvJEo01u1aWUdGdH5E8afA1WnjYcvL2TH/Om
gyL6/F89bOGOyGnY+0Ep4+EVtShA8QZXCwVtsyPg1SbZmZ40sTkePm5gCIQ+9S2K
XNCxd0UKs3xj1FdgyvqhaQsOvaJhIAsH23A8UB2liz9FZfFzyJDH1CJZTWyf7N39
1+PKoEff2Rw7bYGqLvt5p2VwDvnOjs94iZ4HPG48CCvfMwlP3ifrt/SakyHMjvr2
WlOD8qeyR4q4N78zP9rKNTKBrqZiJ/yUfLUTclf53Xrhpx6Q3TC6jhEDSx4o7dVH
/8HaLbrXAgMBAAECggEAQtd5OWtOaHpk7H8PU3Jg3Gwvq1Sm9e3f8b71Tbpt+t62
A1Gwp3TTJluGtFmhCi019X+KjwBu0JsvFMEeLLmVVBbOgBpSRaEE8BJCcXc7zLMh
SWKeJvYdEU1/6mUXZml6XPLviLVI9FAQA0/QdJiKa3UvEKh7tNjle2m4SXJViYuT
ftSjBD4lMrl1Rvj2grx7O4QG4pis+d836nexekGxK82zfGTZk9QkOsXvCs8BvIl9
U90uInM2EbKV5ZCN8KJSWuKh1ENJdKTkYUo54g3IiyApoqKD3aPROk50b1YeSjS7
y54jO9oUzd78LCc3yWTlg4eXtxvBE5EfZKXLV4Hh4QKBgQDhaGCD5pEAuNzULy/X
1NdWSHoEFasKSbkg+KWKS6eTbwfI0UZiHjx4XRI34VD5T0jgGAkD5BJU62Pm6Ing
ch5ofpLqclLBPcbNEl5pi3EbU12QHwwvsgGMQ4tFZqSJlkGP4uQNxaHgO7cEB/Cc
rH1qyCSgVV+DHL8GuOHfcoYfowKBgQDCzEOI7uTTtEhWy35qLDQV5EamCUMLgfuW
wPXttC89Bp67/7MxGdzcXyg5dUMRgGdQjRoP0R5i4NpmewbItX32tC5qHPlQPXzp
oPX9AzboTBAPf9RKgGeTJUdZBUhxOyuv2P1CxLhI1VOhMsSKd0/JZAbv1JOEcP9E
DoYCL7YbPQKBgC+DBsUGto3GaZW4FIeT74faYw21W4YhoAoZHxuVcs8a2jM0GXfZ
E7nzNEHfFmhWk+/YYR4W1t5bd4VWgqlypII2G9WbH2JoGR1kv83TfJXb7p7QTItJ
JBZirlqEli4CI/OmSZe1jrdrHtqUqdz8Zrs5UK8TD7zdNE7Abavhwe+tAoGBAIhb
GY8Zwn3avaRb75J8fL0yPK3u38qHY0gkrEGHFmX+Y1Cv7YwaUXrE9VA7IAZ5Plip
XKvjswR95wNDbP1D5feyKqdGvtIHKJAVhvtjq6Sx0bA3x6+a8GWU+7t5sjros2z1
VFrPfzwkb9Tvx5oxaF1Nsnlg8R4NRvIpxG51XWD1AoGAPbI0bWKvD75t4F3wrwid
83wzBObyBGxlL3JkZm8/yn99+sHX1/QLKcUfUNZFLkyipZqaUye3cVxVNckfjszK
CA+2aSbevc6e+UjACl2HzqS+ttIa6HcxP/8U2gJjAql1Jzl71Csmrd+NMLdcpjcf
VL4cuS+8PHnb9tcFprWvQn8=
-----END PRIVATE KEY-----');
	
	//公钥加密 
	$public_key = openssl_pkey_get_public(RSA_PUBLIC);
	echo $public_key;
	if(!$public_key){
		die('公钥不可用');
	}
	//第一个参数是待加密的数据只能是string，第二个参数是加密后的数据,第三个参数是openssl_pkey_get_public返回的资源类型,第四个参数是填充方式
	$return_en = openssl_public_encrypt("大雁塔", $crypted, $public_key);
	if(!$return_en){
		return('加密失败,请检查RSA秘钥');
	}
	$eb64_cry = base64_encode($crypted);
	echo "公钥加密数据：".$eb64_cry;
	echo "<hr>";
	
	//私钥解密
	$private_key = openssl_pkey_get_private(RSA_PRIVATE);
	if(!$private_key){
		die('私钥不可用');
	}
	$return_de = openssl_private_decrypt(base64_decode($eb64_cry), $decrypted, $private_key);
	if(!$return_de){
		return('解密失败,请检查RSA秘钥');
	}
	echo "私钥解密数据:".$decrypted;
	echo "<hr>";
?>