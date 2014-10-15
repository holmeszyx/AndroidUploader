package z.hol.uploader.bridge;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 流上传器
 * Created by holmes on 10/15/14.
 */
public abstract class StreamBridge implements Bridge{

    protected OutputStream outStream;

    private boolean mCloseStreamOnFinish = true;

    public StreamBridge(){

    }

    /**
     * 设置上传到的流
     * 在里面给 this.outStream 赋值
     * @param out
     */
    public abstract void setOutStream(OutputStream out);

    public void setCloseStreamOnFinish(boolean close){
        mCloseStreamOnFinish = close;
    }

    public boolean isCloseStreamOnFinish(){
        return mCloseStreamOnFinish;
    }

    @Override
    public int uploadData(byte[] data, int len) throws IOException {
        if (outStream == null){
            throw new NullPointerException("outStream can not be null");
        }

        int uploadLen = len > data.length ? data.length : len;
        outStream.write(data, 0, uploadLen);

        return uploadLen;
    }

    @Override
    public boolean finish() {
        if (mCloseStreamOnFinish){
            if (outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }
}
