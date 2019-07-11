package com.cs.zxing.decode;

import com.google.zxing.Result;

/**
 *
 * 解析图片的回调
 */

public interface DecodeImgCallback {
    public void onImageDecodeSuccess(Result result);

    public void onImageDecodeFailed();
}
