
package com.inuc.wifiuse.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class ToolBase64 {

	/**
	 * 将bitmap转换成base64
	 * @param bitmap
	 * @return
     */
	public static String bitmapToBase64(Bitmap bitmap){
		String string=null;
		ByteArrayOutputStream bStream=new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG,50,bStream);
		byte[]bytes=bStream.toByteArray();
		string=Base64.encodeToString(bytes,Base64.DEFAULT);
		return string;
	}

}
