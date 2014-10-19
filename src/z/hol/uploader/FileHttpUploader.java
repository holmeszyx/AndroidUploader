package z.hol.uploader;

import z.hol.uploader.bridge.HttpBridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 文件上传器
 * Created by holmes on 10/19/14.
 */
public class FileHttpUploader extends HttpUploader{

    private File mFile;

    public FileHttpUploader(File file, HttpBridge bridge) throws FileNotFoundException {
        super(new FileInputStream(file), file.length(), bridge);
        mFile = file;
    }

    /**
     * 获取文件
     * @return
     */
    public File getFile(){
        return mFile;
    }

    /**
     * 获取文件路径
     * @return
     */
    public String getFilepath(){
        return mFile.getAbsolutePath();
    }

    /**
     * 获取文件长度
     * @return
     */
    public long getFileLength(){
        return mFile.length();
    }
}
