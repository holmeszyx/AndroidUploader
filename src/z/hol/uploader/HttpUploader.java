package z.hol.uploader;

import z.hol.uploader.bridge.Bridge;
import z.hol.uploader.bridge.HttpBridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用http上的传器
 * Created by holmes on 10/19/14.
 */
public class HttpUploader extends StreamUploader {

    private HttpBridge mBridge;
    private AtomicBoolean mIsStarted = new AtomicBoolean(false);

    public HttpUploader(InputStream inputStream, long length, HttpBridge bridge) {
        super(inputStream, length, bridge);
        mBridge = bridge;
    }

    @Override
    public void setBridge(Bridge bridge) {
        if (bridge instanceof HttpBridge){
            mBridge = (HttpBridge) bridge;
            super.setBridge(bridge);
        }else {
            throw new IllegalArgumentException("bridge should be HttpBridge");
        }
    }

    @Override
    public void uploader() {
        super.uploader();
        if (!mIsStarted.get()){
            mIsStarted.set(true);
            asyncUploader();
        }
    }

    /**
     * 异步上传
     */
    protected void asyncUploader(){
        Thread thread = new Thread(this, "Uploader");
        thread.start();
    }

    @Override
    public void run() {
        mBridge.fillEntry(this);
        try {
            mBridge.execute();
        } catch (IOException e) {
            e.printStackTrace();
            onError(4);
        }
        mIsStarted.set(false);
    }
}
