package z.hol.uploader.bridge;

import org.apache.http.entity.FileEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.File;
import java.io.OutputStream;

/**
 * Http上传文件
 * Created by holmes on 10/15/14.
 */
public class FileHttpBridge extends StreamBridge{

    public FileHttpBridge(File file){

        FileEntity mFileEntry = new FileEntity(file, "stream");
    }


    @Override
    public void setOutStream(OutputStream out) {

    }

}
