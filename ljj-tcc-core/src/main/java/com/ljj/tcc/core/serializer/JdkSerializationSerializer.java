package com.ljj.tcc.core.serializer;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Created by liangjinjing on 7/22/16.
 */
public class JdkSerializationSerializer<T extends Serializable> implements ObjectSerializer<T> {

    @Override
    public byte[] serialize(T object) {
        return SerializationUtils.serialize(object);
    }

    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            return SerializationUtils.deserialize(bytes);
        }
    }

    @Override
    public T clone(T object) {
        return SerializationUtils.clone(object);
    }
}
