package com.shth.das.business.dataup;

import com.alibaba.fastjson2.JSON;
import com.shth.das.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DataDecoderHandler {

    //自定义数据拆包逻辑集合
    public static final Map<Object, Method> unpack_map = new ConcurrentHashMap<>();

    //协议判定方法集合
    public static final Map<Object, Method> protocolDecide_map = new ConcurrentHashMap<>();

    static {
        try {
            //Class<?> superClass = Class.forName(DataUpHandle.class.getName());
            Reflections reflections = new Reflections(DataUpHandle.class.getPackageName());
            //得到某接口下的所有实现类
            Set<Class<? extends DataUpHandle>> implClassSet = reflections.getSubTypesOf(DataUpHandle.class);
            for (Class<?> aClass : implClassSet) {

                //执行自定义数据拆包逻辑
                addMethodByUnpack(aClass);

                //执行协议判定方法
                addMethodByProtocolDecide(aClass);

                //协议解析 todo

                //业务逻辑处理 todo

            }
        } catch (Exception e) {
            log.error("异常：", e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("-----------------------------------");
        System.out.println("-protocolDecide_map:" + protocolDecide_map.size());
        System.out.println("-unpack_map:" + unpack_map.size());
        System.out.println("-----------------------------------");

        //1、获取完整的数据包

        //2、协议判定

        //3、解析数据包 -> pojo对象

        //4、业务处理

    }

    public String getDataPacket(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        InetSocketAddress inetSocket = (InetSocketAddress) ctx.channel().localAddress();
        //服务端端口
        int serverPort = inetSocket.getPort();
        Set<Map.Entry<Object, Method>> entries = unpack_map.entrySet();
        for (Map.Entry<Object, Method> entry : entries) {
            Object key = entry.getKey();
            Method method = unpack_map.get(key);
            try {
                //执行拆包，拿到完整的数据包
                Object result = method.invoke(key, ctx.channel(), byteBuf, out);
                if (ObjectUtils.isEmpty(result)) {
                    continue;
                }
                String str = JSON.toJSONString(result);
                if (CommonUtils.isNotEmpty(str)) {
                    return str;
                }
            } catch (Exception e) {
                log.error("异常：", e);
            }
        }
        return null;
    }

    /**
     * 自定义数据拆包逻辑
     *
     * @param clazz
     * @throws Exception
     */
    public static void addMethodByUnpack(Class<?> clazz) throws Exception {
        if (clazz.isInterface()) {
            return;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }
        //获取Class的方法
        Method unpack = clazz.getMethod("unpack", Channel.class, ByteBuf.class, List.class);
        //通过Class的无参构造调用newInstance方法，不建议直接调用Class的newInstance
        Object object = clazz.getDeclaredConstructor().newInstance();
        //添加到map
        unpack_map.put(object, unpack);
    }

    /**
     * 协议判定方法
     *
     * @param clazz
     * @throws Exception
     */
    public static void addMethodByProtocolDecide(Class<?> clazz) throws Exception {
        if (clazz.isInterface()) {
            return;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }
        //获取Class的方法
        Method protocolDecide = clazz.getMethod("protocolDecide", String.class);
        //通过Class的无参构造调用newInstance方法，不建议直接调用Class的newInstance
        Object object = clazz.getDeclaredConstructor().newInstance();
        //添加到map
        protocolDecide_map.put(object, protocolDecide);

        //Object invoke = protocolDecide.invoke(o, "");
    }

}
