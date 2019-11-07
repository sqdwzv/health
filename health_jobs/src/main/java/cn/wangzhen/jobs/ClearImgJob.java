package cn.wangzhen.jobs;

import cn.wangzhen.constant.RedisConstant;
import cn.wangzhen.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/** * 自定义Job，实现定时清理垃圾图片 */

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set!=null){
            //利用工具类删除七牛云的照片
            for (String prcName : set) {
                QiniuUtils.deleteFileFromQiniu(prcName);
                //再从大的集合删除无效照片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,prcName);
                System.out.println("已经删除"+prcName);
            }
        }

    }
}
