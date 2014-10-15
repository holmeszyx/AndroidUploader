package z.hol.uploader;

import z.hol.general.ConcurrentCanceler;
import z.hol.uploader.bridge.Bridge;

import java.io.IOException;
import java.io.InputStream;

/**
 * 流上传
 * Created by holmes on 10/15/14.
 */
public class StreamUploader implements Uploader{

    /**
     * 上传的流
     */
    private InputStream mSrcStream;
    private Bridge mBridge;

    /** 要上传的总大小 */
    private long mTotal = 0l;
    /** 已上传的大小 */
    private long mCurrent = 0l;
    /** 取消器 */
    private ConcurrentCanceler mCanceler;

    public StreamUploader(InputStream inputStream, Bridge bridge){
        mSrcStream = inputStream;
        mBridge = bridge;
        mCanceler = new ConcurrentCanceler();
    }

    /**
     * 设置要上传的总大小.
     * 一般在初始化的时候就设置好
     * @param total
     */
    protected void setTotal(long total){
        mTotal = total;
    }

    /**
     * 要上传的总大小
     * @return
     */
    public long getTotal(){
        return mTotal;
    }

    /**
     * 设已上传的大小. 一般为0
     * @param current
     */
    protected void setCurrent(long current){
        mCurrent = current;
    }

    /**
     * 已上传的大小
     * @return
     */
    public long getCurrent(){
        return mCurrent;
    }

    @Override
    public void setBridge(Bridge bridge) {
        mBridge = bridge;
    }

    @Override
    public Bridge getBridge() {
        return mBridge;
    }

    @Override
    public void uploader() {
        mCanceler.restore();
        internalUploader();
    }

    @Override
    public void cancel() {
        mCanceler.cancel();
    }

    public boolean isCanceled(){
        return mCanceler.isCanceled();
    }

    /**
     * 上传
     */
    protected void internalUploader(){
        if (mBridge == null){
            onError(1);
            return;
        }

        byte[] buff = new byte[4096];
        int readed = -1;

        // 上传
        while (true) {
            if (mCanceler.isCanceled()){
                onCancel();
                break;
            }

            try {
                readed = mSrcStream.read(buff);
            } catch (IOException e) {
                e.printStackTrace();
                onError(2);
                break;
            }

            if (mCanceler.isCanceled()){
                onCancel();
                break;
            }
            if (readed != -1){
                int uploaded;
                try {
                    uploaded = mBridge.uploadData(buff, readed);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(3);
                    break;
                }
                mCurrent += uploaded;
                onProgress(mTotal, mCurrent);
            }else {
                if (mCurrent != mTotal){
                    mCurrent = mTotal;
                    onProgress(mTotal, mCurrent);
                }
                onCompleted();
                break;
            }
        }

        onUploaderFinished();
    }

    /**
     * 上传完成，不一定是成功, 只是过程结束了
     */
    protected void onUploaderFinished(){

    }

    /**
     * 出错
     * @param code
     */
    protected void onError(int code){

    }

    /**
     * 完成
     */
    protected void onCompleted(){

    }

    /**
     * 进度
     * @param total
     * @param current
     */
    protected void onProgress(long total, long current){

    }

    /**
     * 被取消
     */
    protected void onCancel(){

    }
}
