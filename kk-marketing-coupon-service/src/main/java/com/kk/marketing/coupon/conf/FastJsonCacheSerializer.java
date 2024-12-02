package com.kk.marketing.coupon.conf;

import com.alibaba.fastjson2.JSON;
import com.kk.arch.common.util.JsonUtils;
import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Zal
 */
public class FastJsonCacheSerializer<T> implements CacheSerializer<T> {

    private final Class<T> clazz;

    public FastJsonCacheSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 将对象序列化为字节数组，并写入给定的 ByteBuffer。
     *
     * @param object     要序列化的对象
     * @param byteBuffer 用于存储序列化结果的 ByteBuffer
     */
    @Override
    public void serialize(Object object, ByteBuffer byteBuffer) {
        if (object == null) {
            // 如果对象为空，可以清空 ByteBuffer 或者不做任何操作
            byteBuffer.clear();
            return;
        }

        // 将对象序列化为 JSON 字符串
        String jsonString = JsonUtils.toJsonString(object);

        // 将 JSON 字符串转换为字节数组
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);

        // 确保 ByteBuffer 有足够的空间来存储字节数组
        if (byteBuffer.capacity() < bytes.length) {
            throw new IllegalArgumentException("ByteBuffer is too small to hold the serialized data");
        }

        // 清空 ByteBuffer 并设置位置为 0
        byteBuffer.clear();

        // 将字节数组写入 ByteBuffer
        byteBuffer.put(bytes);
        byteBuffer.flip(); // 准备 ByteBuffer 以供读取
    }


    /**
     * 将 ByteBuffer 中的数据反序列化为 Java 对象。
     *
     * @param byteBuffer 包含序列化数据的 ByteBuffer
     * @return 反序列化后的 Java 对象
     */
    @Override
    public T deserialize(ByteBuffer byteBuffer) {
        if (byteBuffer == null || !byteBuffer.hasRemaining()) {
            return null;
        }

        // 从 ByteBuffer 中读取字节数据
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);

        // 将字节数组转换为 JSON 字符串
        String jsonString = new String(bytes, StandardCharsets.UTF_8);

        // 使用 FastJSON 解析 JSON 字符串为 Java 对象
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * 计算对象序列化后的字节大小。
     *
     * @param object 要计算大小的对象
     * @return 序列化后数据的字节大小
     */
    public int serializedSize(T object) {
        if (object == null) {
            return 0;
        }

        // 将对象序列化为 JSON 字符串
        String jsonString = JSON.toJSONString(object);

        // 获取 JSON 字符串的字节数
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        return bytes.length;
    }

}
