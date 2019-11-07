package cn.wangzhen.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.util.Date;

public class QiNiuTest {
   /* //使用七牛云提提供的sdk将本地图片上传到七牛服务器
    @Test
    public void test1(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "GOuuLpYLOMK0V1Gzy9rH1bk7xBsfBJiCuXh4GWaH";
        String secretKey = "x-FgovNeJbvh6RPL1PUwTE06LHFPWN512H6Ern1L";
        //空间名
        String bucket = "itcasthealthwangzhenspase1";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\传智健康\\day04\\素材\\图片资源\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "图片.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
    //删除七牛云服务器图片
    @Test
    public void test2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String accessKey = "GOuuLpYLOMK0V1Gzy9rH1bk7xBsfBJiCuXh4GWaH";
        String secretKey = "x-FgovNeJbvh6RPL1PUwTE06LHFPWN512H6Ern1L";
        String bucket = "itcasthealthwangzhenspase1";
        String key = "图片.jpg";
        //被删除的文件名
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }*/
  /* @Test
    public void test5(){
       Date date = new Date();
       int date1 = date.getDate();
       System.out.println(date1);
   }*/
}
