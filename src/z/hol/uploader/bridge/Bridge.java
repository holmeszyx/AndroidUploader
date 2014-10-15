package z.hol.uploader.bridge;

import java.io.IOException;

/**
 * 上传桥
 * Created by holmes on 10/15/14.
 */
public interface Bridge {

    /**
     * 上传数据
     * @param data
     * @param len 需要上传的长度
     * @return 上传一的数据长度, -1 为结束.
     *      一般都应该是 data.length()
     */
    int uploadData(byte[] data, int len) throws IOException;

    /**
     * 结束
     * @return
     */
    boolean finish();
}
