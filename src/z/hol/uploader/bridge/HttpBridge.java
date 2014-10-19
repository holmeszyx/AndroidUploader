package z.hol.uploader.bridge;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

/**
 * Http上传连接桥.
 * 上传之前要设置好httpClient,和httpPost.
 * 开始上传前的时候填充 #fillEntry.
 * 开始上传则执行 #execute
 * Created by holmes on 10/19/14.
 */
public class HttpBridge extends StreamBridge{

    private HttpPost mPost;
    private String mUrl;
    private HttpClient mClient;

    public HttpBridge(HttpClient client , HttpPost post){
        mClient = client;
        mPost = post;
    }

    public HttpBridge(HttpClient client, String url){
        mClient = client;
        mUrl = url;
        if (mUrl == null){
            throw new IllegalArgumentException("url should not be null.");
        }
        initHttpArgs();
    }

    public void initHttpArgs(){
        if (mPost == null){
            mPost = new HttpPost(mUrl);
        }
    }

    /**
     * 填充上传内容
     * @param entity
     */
    public void fillEntry(HttpEntity entity){
        mPost.setEntity(entity);
    }

    /**
     * 上传
     * @throws IOException
     */
    public void execute() throws IOException {
        mClient.execute(mPost);
    }
}
