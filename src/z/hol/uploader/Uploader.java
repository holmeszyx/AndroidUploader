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

    /**
     * 设置一个id
     * @return
     */
    int getId();

    /**
     * 获取一个id.
     * 如果没有设置过id, 这个id将会是临时生成的一个随机数
     * @param id
     */
    void setId(int id);
}
