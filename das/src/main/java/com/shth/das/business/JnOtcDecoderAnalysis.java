package com.shth.das.business;

import com.shth.das.common.BaseAbstractDecoder;
import com.shth.das.common.HandlerParam;
import com.shth.das.common.JnOtcDecoderParam;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.util.CommonUtils;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: OTC1.0协议解析
 * @author: Shan Long
 * @create: 2021-08-08
 */
public class JnOtcDecoderAnalysis extends BaseAbstractDecoder {

    /**
     * 保存字符串长度和方法映射关系
     * Function<K,V>,k:传入参数，V：返回参数
     */
    private final Map<Integer, Function<JnOtcDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();
    private final JnOtcRtDataProtocol jnOtcRtDataProtocol = new JnOtcRtDataProtocol();

    public JnOtcDecoderAnalysis() {
        //OTC1.0实时数据解析
        this.decoderMapping.put(282, this::jnOtcRtDataAnalysis);
        //工艺下发返回解析
        this.decoderMapping.put(24, this::otcIssueReturnAnalysis);
        //索取返回协议解析
        this.decoderMapping.put(112, this::otcClaimReturnAnalysis);
        //密码返回和控制命令返回
        this.decoderMapping.put(22, this::otcPwdCmdReturnAnalysis);
    }

    @Override
    public HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str) {
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        JnOtcDecoderParam jnOtcDecoderParam = new JnOtcDecoderParam();
        jnOtcDecoderParam.setStr(str);
        jnOtcDecoderParam.setClientIp(clientIp);
        if (this.decoderMapping.containsKey(str.length())) {
            return this.decoderMapping.get(str.length()).apply(jnOtcDecoderParam);
        }
        return new HandlerParam();
    }

    /**
     * OTC1.0实时数据解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回MAP
     */
    private HandlerParam jnOtcRtDataAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        HandlerParam handlerParam = new HandlerParam();
        if (null != jnOtcDecoderParam) {
            Map<String, Object> map = new HashMap<>();
            //存数据库
            List<JNRtDataDB> jnRtDataDbs = this.jnOtcRtDataProtocol.jnRtDataDbAnalysis(jnOtcDecoderParam.getStr());
            //发送前端
            List<JNRtDataUI> jnRtDataUis = this.jnOtcRtDataProtocol.jnRtDataUiAnalysis(jnOtcDecoderParam.getClientIp(), jnOtcDecoderParam.getStr());
            if (CommonUtils.isNotEmpty(jnRtDataDbs)) {
                map.put("JNRtDataDB", jnRtDataDbs);
            }
            if (CommonUtils.isNotEmpty(jnRtDataUis)) {
                map.put("JNRtDataUI", jnRtDataUis);
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
        }
        return handlerParam;
    }

    /**
     * 工艺下发返回解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回MAP
     */
    private HandlerParam otcIssueReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        HandlerParam handlerParam = new HandlerParam();
        if (null != jnOtcDecoderParam) {
            Map<String, Object> map = new HashMap<>();
            JNProcessIssueReturn issueReturn = this.jnOtcRtDataProtocol.jnIssueReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != issueReturn) {
                map.put("JNProcessIssueReturn", issueReturn);
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
        }
        return handlerParam;
    }

    /**
     * 索取返回协议解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回MAP
     */
    private HandlerParam otcClaimReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        HandlerParam handlerParam = new HandlerParam();
        if (null != jnOtcDecoderParam) {
            Map<String, Object> map = new HashMap<>();
            JNProcessClaimReturn claimReturn = this.jnOtcRtDataProtocol.jnClaimReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != claimReturn) {
                map.put("JNProcessClaimReturn", claimReturn);
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
        }
        return handlerParam;
    }

    /**
     * 密码返回和控制命令返回
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回MAP
     */
    private HandlerParam otcPwdCmdReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        HandlerParam handlerParam = new HandlerParam();
        if (null != jnOtcDecoderParam) {
            Map<String, Object> map = new HashMap<>();
            String str = jnOtcDecoderParam.getStr();
            //密码返回
            if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNPasswordReturn passwordReturn = this.jnOtcRtDataProtocol.jnPasswordReturnAnalysis(str);
                if (null != passwordReturn) {
                    map.put("JNPasswordReturn", passwordReturn);
                }
            }
            //控制命令返回
            if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNCommandReturn commandReturn = this.jnOtcRtDataProtocol.jnCommandReturnAnalysis(str);
                if (null != commandReturn) {
                    map.put("JNCommandReturn", commandReturn);
                }
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
        }
        return handlerParam;
    }

}
