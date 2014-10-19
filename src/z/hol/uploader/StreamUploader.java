package z.hol.uploader;

import org.apache.http.entity.InputStreamEntity;
import z.hol.general.ConcurrentCanceler;
import z.hol.uploader.bridge.Bridge;
import z.hol.uploader.bridge.StreamBridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流上传
 * Created by holmes on 10/15/14.
 */
public class StreamUploader extends InputStreamEntity implements Uploader, Runnable{

    /**
     * 上传的流
     */
    protected InputStream mSrcStream;
    private StreamBridge mBridge;

    /** 要上传的总大小 */
    private long mTotal = 0l;
    /** 已上传的大小 */
    private long mCurrent = 0l;
    /** 取消器 */
    private ConcurrentCanceler mCanceler;

    /** 是否完成 */
    private boolean mConsumed = false;

    /** id */
    private Integer mId;

    private UploadUiHandler mUiHandler;

    public StreamUploader(InputStream inputStream, long length, StreamBridge bridge){
        super(inputStream, length);
        mTotal = length;
        mSrcStream = inputStream;
        mBridge = bridge;
        mCanceler = new ConcurrentCanceler();
    }

    public void setUiHandler(UploadUiHandler handler){
        mUiHandler = handler;
    }

    @Override
    public int getId() {
        if (mId == null){
            mId = super.hashCode();
        }
        return mId.intValue();
    }

    @Override
    public void setId(int id) {
        if (mId == null ||
                mId.intValue() != id){
            mId = id;
        }
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
        if (bridge instanceof StreamBridge){
            mBridge = (StreamBridge) bridge;
        }else {
            throw new IllegalArgumentException("Only StreamBridge allowed!");
        }
    }

    @Override
    public Bridge getBridge() {
        return mBridge;
    }

    @Override
    public void uploader() {
        mCanceler.restore();
        onUploadStart();
        //internalUploader();
    }

    @Override
    public void run() {

    }

    @Override
    public void cancel() {
        mCanceler.cancel();
    }

    public boolean isCanceled(){
        return mCanceler.isCanceled();
    }

    /**
     * 上传, 请在子类中执行
     */
    protected void internalUploader(OutputStream out){
        if (mBridge == null){
            onError(1);
            return;
        }
        mBridge.setOutStream(out);

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

        onUploadFinished();
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        //super.writeTo(outstream);
        internalUploader(outstream);
        this.mConsumed = true;
    }

    @Override
    public boolean isStreaming() {
        return !this.mConsumed;
    }

    @Override
    public void consumeContent() throws IOException {
        this.mConsumed = true;
        mSrcStream.close();
    }

    /**
     * 上传开始
     */
    protected void onUploadStart(){
        if (mUiHandler != null){
            mUiHandler.uploadStart(getId());
        }
    }

    /**
     * 上传完成，不一定是成功, 只是过程结束了
     */
    protected void onUploadFinished(){
        if (mUiHandler != null){
            mUiHandler.uploadFinished(getId());
        }

    }

    /**
     * 出错
     * @param code
     */
    protected void onError(int code){
        if (mUiHandler != null){
            mUiHandler.error(getId(), code);
        }

    }

    /**
     * 完成
     */
    protected void onCompleted(){
        if (mUiHandler != null){
            mUiHandler.completed(getId());
        }

    }

    /**
     * 进度
     * @param total
     * @param current
     */
    protected void onProgress(long total, long current){
        if (mUiHandler != null){
            mUiHandler.progress(getId(), total, current);
        }

    }

    /**
     * 被取消
     */
    protected void onCancel(){
        if (mUiHandler != null){
            mUiHandler.cancel(getId());
        }

    }
}
