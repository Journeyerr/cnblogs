package com.cnblog.payment.utils;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * @description  cmb国密算法工具类
 * @author AnYuan
 *
 */

public class CmbUtil {
    
    /**
     * SM2私钥签名（不使用行内国密算法库）
     */
    public static String sm3withsm2Signature2(String privateKeyStr, String dataStr) throws Exception {
        byte[] key = Hex.decode(privateKeyStr);
        byte[] data = dataStr.getBytes();
        
        // 获得一条签名曲线
        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        // 构造domain函数
        ECDomainParameters domainParameters = new ECDomainParameters(spec.getCurve(), spec.getG(), spec.getN(), spec.getH(), spec.getSeed());
        
        // 国密要求，ID默认值为1234567812345678
        ECPrivateKeyParameters privateKey = new ECPrivateKeyParameters(new BigInteger(1, key), domainParameters);
        ParametersWithID parameters = new ParametersWithID(privateKey, "1234567812345678".getBytes());
        
        // 初始化签名实例
        SM2Signer signer = new SM2Signer();
        signer.init(true, parameters);
        signer.update(data, 0, data.length);
        
        // 计算签名值
        byte[] signature = decodeDERSignature(signer.generateSignature());
        
        return Hex.toHexString(signature);
    }
    

    /**
     * SM3摘要计算（不使用行内国密算法库）
     */
    public static String sm3Signature2 (String src) {
        try {
            SM3Digest sm3Digest = new SM3Digest();
            sm3Digest.update(src.getBytes(StandardCharsets.UTF_8), 0, src.getBytes(StandardCharsets.UTF_8).length);
            byte[] ret = new byte[sm3Digest.getDigestSize()];
            sm3Digest.doFinal(ret, 0);
            return Hex.toHexString(ret);
        } catch (Exception e) {
            throw new RuntimeException("签名计算出现异常");
        }
    }
    
 
    private static byte[] decodeDERSignature(byte[] signature) throws Exception {
        ASN1InputStream stream = new ASN1InputStream(new ByteArrayInputStream(signature));
        
        try {
            ASN1Sequence primitive = (ASN1Sequence)stream.readObject();
            Enumeration enumeration = primitive.getObjects();
            BigInteger R = ((ASN1Integer)enumeration.nextElement()).getValue();
            BigInteger S = ((ASN1Integer)enumeration.nextElement()).getValue();
            byte[] bytes = new byte[64];
            byte[] r = format(R.toByteArray());
            byte[] s = format(S.toByteArray());
            System.arraycopy(r, 0, bytes, 0, 32);
            System.arraycopy(s, 0, bytes, 32, 32);
            return bytes;
        } catch (Exception var10) {
            throw new Exception();
        }
    }
    
    private static byte[] format(byte[] value) {
        if (value.length == 32) {
            return value;
        } else {
            byte[] bytes = new byte[32];
            if (value.length > 32) {
                System.arraycopy(value, value.length - 32, bytes, 0, 32);
            } else {
                System.arraycopy(value, 0, bytes, 32 - value.length, value.length);
            }
            
            return bytes;
        }
    }
}
