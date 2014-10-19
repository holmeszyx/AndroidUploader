package z.hol.uploader;

/**
 * 上传状态回调
 * Created by holmes on 10/19/14.
 */
public interface UploadStateCallback {

    /**
     * 上传完成，不一定是成功, 只是过程结束了
     */
    void onUploadFinished(int id);

    /**
     * 上传开始
     * @param id
     */
    void onUploadStart(int id);

    /**
     * 出错
     * @param code
     */
    void onError(int id, int code);

    /**
     * 完成
     */
    void onCompleted(int id);

    /**
     * 进度
     * @param total
     * @param current
     */
    void onProgress(int id, long total, long current);

    /**
     * 被取消
     */
    void onCancel(int id);
}
