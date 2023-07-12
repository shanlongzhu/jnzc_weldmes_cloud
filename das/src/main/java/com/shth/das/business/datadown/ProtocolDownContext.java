package com.shth.das.business.datadown;

import com.shth.das.codeparam.MqttParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class ProtocolDownContext {

    /**
     * 存储策略
     */
    private static final Map<String, Consumer<MqttParam>> downProtocolMap = new ConcurrentHashMap<>();

    static {
        registerProtocol();
    }

    /**
     * 自动注册策略
     */
    private static void registerProtocol() {
        // 通过反射找到所有的策略子类进行注册
        Reflections reflections = new Reflections(BaseProtocol.class.getPackage().getName());
        Set<Class<? extends BaseProtocol>> classes = reflections.getSubTypesOf(BaseProtocol.class);
        if (classes != null) {
            for (Class<?> clazz : classes) {
                try {
                    //自动完成策略注册
                    BaseProtocol object = (BaseProtocol) clazz.getConstructor().newInstance();
                    object.register(downProtocolMap);
                } catch (Exception e) {
                    log.error("下行数据协议创建实例异常：{}", e.getMessage());
                }
            }
        }
        log.info("下行数据协议注册完成，注册数量：{}", downProtocolMap.size());
    }

    /**
     * 根据主题找方法解析消息体
     *
     * @param mqttParam 消息内容
     */
    public void protocolHandlerByTopic(MqttParam mqttParam) {
        if (ObjectUtils.isEmpty(mqttParam)) {
            return;
        }
        if (!downProtocolMap.containsKey(mqttParam.getTopic())) {
            return;
        }
        downProtocolMap.get(mqttParam.getTopic()).accept(mqttParam);
    }

}