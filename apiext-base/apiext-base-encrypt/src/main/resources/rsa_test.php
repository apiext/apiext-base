<?php
	// 作者本人不熟悉php
	// 以下代码仅用来测试
    // php的公钥要求pkcs8格式
	define('RSA_PUBLIC', '-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoVc1MaC4ATWzpegKXouh
XLw97hwn1qAlkYr6O+BE5kt9qi8dudzA7eK40hJj3hg9VVDDp1Ap8p27/Q1a92VG
ses6YEdI3T/6PnU7ylK6xWOlGspTKc2fhN6cgvJmcVoZZGxJy3NcBXJvhtJSbpFT
mUnIyPoHevtUo8bvD3XMNj/YMIv9mNbH0RFkbo2K0ckxd+mBIhzq0ssRRj5jDXSk
ZeD8jLKWMHSEkLilQ87ib7SMxCrmG326/uPmMeuSTLgigMqumrcr4KVSO3G7YZpK
YzLYlGtR2Bn4qlqYNGYHlNLfBX3EXXod8T3XPOWyjNN3uCwMkDLaLjdsjAKsS5w1
AwIDAQAB
-----END PUBLIC KEY-----');

// php对私钥的要求 pkcs1和pkcs8都可以
define('RSA_PRIVATE','-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQChVzUxoLgBNbOl
6Apei6FcvD3uHCfWoCWRivo74ETmS32qLx253MDt4rjSEmPeGD1VUMOnUCnynbv9
DVr3ZUax6zpgR0jdP/o+dTvKUrrFY6UaylMpzZ+E3pyC8mZxWhlkbEnLc1wFcm+G
0lJukVOZScjI+gd6+1Sjxu8Pdcw2P9gwi/2Y1sfREWRujYrRyTF36YEiHOrSyxFG
PmMNdKRl4PyMspYwdISQuKVDzuJvtIzEKuYbfbr+4+Yx65JMuCKAyq6atyvgpVI7
cbthmkpjMtiUa1HYGfiqWpg0ZgeU0t8FfcRdeh3xPdc85bKM03e4LAyQMtouN2yM
AqxLnDUDAgMBAAECggEAKUZkJ2QUbsrMQL9SF8nk6BKITWgKkQ+7TLafOb4wIjJh
3a3cDvZ4rOL5oVRvFBxjZHYa96y8boMH2r8V7ICOS2oALy1NaLy7Sks3lpT/jwvk
6yujhuhBzP4eUCmvJ4SDRnuljVTfz8zs1zLrP7oSBv8nTLMaQs4VYyi7c9sQ+TeG
KzAmzhJCz41p0ZAuaa0yPLOjkbJf1398mTpE8vbL/GhaN74TM679BXZv/Xfc37TT
UGy7GKsMKLL5WLTW8u9mE+MLo1VG2wwz2bhoed4g0sgt8onHkseoC/Eu+0R9pkPz
eLZ2+Tppvfwuiu3GuKA3GqqJKMkr293KrDBKD1DWYQKBgQD2hQqVtMJTxDty/gxv
cgJS8r078PhhcTpmW809+0TNltA4aerKQpnm1JnyU3LNoUikrpb0wien7FQRbmsx
EjV5tqFGypWTImY2cQlOVIWc1szcojAHRRzexiR2breKQWfgO7eTW3qdc7ixqtsy
3bLO0p+ORtYYpf5rc1YyrzNwawKBgQCni5ahPOYNedKIUBKukcYD9hq08MzO22Qz
7cmzyWswwWEaISB+u5XlGGIqb33Z3+TSSqb5qhtl/K3pC7iTMTDkxvf4xWX/tln/
smaurEvw7pAN7BEtflOC030G7CMR0hCq+1acQw2oH19kzDYFT+0lcpv6jle1FloR
llKotOMTyQKBgQDeLcX+ay2BRai22RToFH7Xt4Kp1WJmoCAcRLzUEfROvo/naD5+
EEyY6B/fcSpU/fl0ww22ho4rICock8H0Ng8QJXNxVAA+3Y/1nSbf+/l4A3gbmelj
g/yjIz2Vp++voYc3Z1rqalY2NifNqMlqLLmNAfNfzaQEDwcgXJDhmVnpBQKBgQCY
2GwLHe5VPtyEhF/2p3+4n2xPwxlkYJryEKygelByQwgdeTkWxHSDchIj2mA6QdmS
yo23V6r1CGVzI8E3bnnHS4huh0580FMcRBkOopoI6um5+bDWCteOkvcNOKDpofSY
tv+VQEqWJEC3SODDSd3y+rI3CmITDpgOUU+JHrPFmQKBgFedr8SOJoPPumcWnuv9
wulFAB9Bo3qyOjODtfg7deNdW03Y9h5cbJVJyoFNt1pX3SOWGaCweQKxvJBqU/7S
jE0Tik1L3cGAswQ2WKuFkWXwdYjy6KF5MpeAUtPedePvpuSKoHd6S8NZ0eHX4YDg
AQoYN35baZYSjtZSG8Ehk2oV
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


	$data = "春风十里不如你";//原始数据
	$encrypted = "";
	$decrypted = "";
	echo "source data:",$data,"\n";
	echo "private key encrypt:\n";
	openssl_private_encrypt($data,$encrypted,$private_key);//私钥加密
	$encrypted = base64_encode($encrypted);//加密后的内容通常含有特殊字符，需要编码转换下，在网络间通过url传输时要注意base64编码是否是url安全的
	echo $encrypted,"\n";

	echo "public key decrypt:\n";

	openssl_public_decrypt(base64_decode($encrypted), $decrypted, $public_key);//私钥加密的内容通过公钥可用解密出来
	echo $decrypted,"\n";

	echo "---------------------------------------\n";

	/********************私钥签名********************/
	function get_private_sign($sign_str,$priKey,$signature_alg=OPENSSL_ALGO_SHA256){
		openssl_sign($sign_str,$signature,$priKey,$signature_alg);//生成签名
		$signature = base64_encode($signature);
		return $signature;
	}
	/********************公钥验签********************/
	function public_verify($sign_str,$sign,$pubKey,$signature_alg=OPENSSL_ALGO_SHA256){

		$verify = openssl_verify($sign_str, base64_decode($sign), $pubKey, $signature_alg);
		return $verify==1;//false or true
	}
	echo "---------------------------------------\n";
	$signature = get_private_sign("1", $private_key);
	echo "签名字符串\n";
	echo $signature;
	echo "\n";
	$res = public_verify("abc", $signature, $public_key);
	echo "<hr>";
	echo($res);
	echo "<hr>";
	echo "验签java";
	$res = public_verify("www.apiext.com;www.apiext.cn", "KTCU/l+ouUheP4JeI8dsHTE3TJJayfzDFZbXL88PC/AqpmhzU1BwS5Or35mD6o9ig9v8MeueSROASt0vWAomwYa0uF3lqRjsMnETxtfEBabevu9enWasIb9weL4cdDfgUzsKf3hcy56Tu81ZRg72oE11H+NfYiz1LFWOEqKir1ZT8Eajj3O0eqZJG7TtA2aJFT9YZzRNaAPjJVt9wYLST4j7L1emdIrkYl0IBagm9QJ5g0tt3gxlMTytV/+kvuLiOopwCgXoBaTQ38oFygX0HICWLaB0XOytfCrzDTtNJu6g82llnhOyezR0vbMdRPNxF4zFZ4R/AW+yn62notPSig==", $public_key);
	echo "<hr>";
	echo($res);

?>