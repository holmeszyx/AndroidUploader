package z.hol.uploader;

import z.hol.uploader.bridge.Bridge;

/**
 * 上传
 * Created by holmes on 10/15/14.
 */
public interface Uploader {

    /**
     * 设置上传的链接桥
     * @param bridge
     */
    void setBridge(Bridge bridge);

    /**
     * 获取上传连接桥
     * @return
     */
    Bridge getBridge();

    /**
     * 上传
     */
    void uploader();

    /**
     * 取消
     */
    void cancel();
}
