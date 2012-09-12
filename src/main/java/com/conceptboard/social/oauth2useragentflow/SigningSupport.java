package com.conceptboard.social.oauth2useragentflow;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.google.common.base.Charsets;

/**
 * modelled after the protected class {@linkplain} org.springframework.social.oauth1.SigningSupport}
 * 
 * @see org.springframework.social.oauth1.SigningSupport
 * 
 * @author christian
 * 
 */
public class SigningSupport {

	public static byte[] calculateSignature(final String signatureBaseString, final String key, final String signingAlgorithm) {
		try {
			final Mac mac = Mac.getInstance(signingAlgorithm);
			final SecretKeySpec spec = new SecretKeySpec(key.getBytes(Charsets.UTF_8), signingAlgorithm);
			mac.init(spec);
			final byte[] text = signatureBaseString.getBytes(Charsets.UTF_8.name());
			final byte[] signatureBytes = mac.doFinal(text);
			return signatureBytes;
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (final InvalidKeyException e) {
			throw new IllegalStateException(e);
		} catch (final UnsupportedEncodingException shouldntHappen) {
			throw new IllegalStateException(shouldntHappen);
		}
	}

	public static byte[] base64Decode(final String s) {
		return DatatypeConverter.parseBase64Binary(s);
	}

}
