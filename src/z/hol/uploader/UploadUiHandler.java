package z.hol.uploader;

import android.os.Handler;
import android.os.Message;

/**
 * ui的回调
 * Created by holmes on 10/19/14.
 */
public abstract class UploadUiHandler extends Handler implements UploadStateCallback{
    private static final int CODE_FINISHDE = 1;
    private static final int CODE_ERROR = 2;
    private static final int CODE_COMPLETED = 3;
    private static final int CODE_PROGRESS = 4;
    private static final int CODE_CANCEL = 5;
    private static final int CODE_START = 6;

    @Override
    public void handleMessage(Message msg) {
        int id = msg.arg1;
        switch (msg.what){
            case CODE_COMPLETED:
                onCompleted(id);
                break;
            case CODE_FINISHDE:
                onUploadFinished(id);
                break;
            case CODE_CANCEL:
                onCancel(id);
                break;
            case CODE_ERROR:
                int[] errorCode = (int[]) msg.obj;
                onError(id, errorCode[0]);
                break;
            case CODE_START:
                onUploadStart(id);
                break;
            case CODE_PROGRESS:
                long[] pro = (long[]) msg.obj;
                onProgress(id, pro[0], pro[1]);
                break;
        }

    }

    protected void sendMessageToTarge(Message msg){
        this.sendMessage(msg);
    }

    /**
     * 上开始
     * @param id
     */
    void uploadStart(int id){
        Message msg = Message.obtain();
        msg.what = CODE_START;
        msg.arg1 = id;
        sendMessageToTarge(msg);
    }

    /**
     * 上传完成，不一定是成功, 只是过程结束了
     */
    void uploadFinished(int id){
        Message msg = Message.obtain();
        msg.what = CODE_FINISHDE;
        msg.arg1 = id;
        sendMessageToTarge(msg);

    }

    /**
     * 出错
     * @param code
     */
    void error(int id, int code){
        Message msg = Message.obtain();
        msg.what = CODE_ERROR;
        msg.arg1 = id;
        msg.obj = new int[]{code};
        sendMessageToTarge(msg);

    }

    /**
     * 完成
     */
    void completed(int id){
        Message msg = Message.obtain();
        msg.what = CODE_COMPLETED;
        msg.arg1 = id;
        sendMessageToTarge(msg);

    }

    /**
     * 进度
     * @param total
     * @param current
     */
    void progress(int id, long total, long current){
        Message msg = Message.obtain();
        msg.what = CODE_PROGRESS;
        msg.arg1 = id;
        msg.obj = new long[]{total, current};
        sendMessageToTarge(msg);

    }

    /**
     * 被取消
     */
    void cancel(int id){
        Message msg = Message.obtain();
        msg.what = CODE_CANCEL;
        msg.arg1 = id;
        sendMessageToTarge(msg);
    }
}
