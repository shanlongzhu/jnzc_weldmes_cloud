package com.shth.das.business.datadown;

import com.shth.das.codeparam.MqttParam;

import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseProtocol {

    protected abstract void register(final Map<String, Consumer<MqttParam>> map);

}
