package com.shth.das.business.dataup.otc;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.dataup.BaseAnalysis;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.codeparam.JnOtcDecoderParam;
import com.shth.das.pojo.jnotc.*;
import com.shth.das.util.CommonUtils;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: OTC协议解析
 * @author: Shan Long
 * @create: 2021-08-08
 */
public class JnOtcProtocolAnalysis extends BaseAnalysis {

    /**
     * 保存字符串长度和方法映射关系
     * Function<T,R>,T:传入参数，R：返回参数
     */
    private final Map<Integer, Function<JnOtcDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();

    public JnOtcProtocolAnalysis() {
        init();
    }

    private void init() {
        //OTC（1.0）实时数据解析
        this.decoderMapping.put(282, this::jnOtcRtDataAnalysis);
        //OTC（1.0）工艺下发返回解析
        this.decoderMapping.put(24, this::otcIssueReturnAnalysis);
        //OTC（1.0）索取返回协议解析
        this.decoderMapping.put(112, this::otcClaimReturnAnalysis);
        //OTC（1.0）密码返回和控制命令返回[新增包路径下发返回]
        this.decoderMapping.put(22, this::otcPwdCmdReturnAnalysis);
    }

    @Override
    public HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str) {
        if (this.decoderMapping.containsKey(str.length())) {
            String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            JnOtcDecoderParam jnOtcDecoderParam = new JnOtcDecoderParam();
            jnOtcDecoderParam.setStr(str);
            jnOtcDecoderParam.setClientIp(clientIp);
            return this.decoderMapping.get(str.length()).apply(jnOtcDecoderParam);
        }
        return null;
    }

    /**
     * 密码返回和控制命令返回
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam otcPwdCmdReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            HandlerParam handlerParam = new HandlerParam();
            Map<String, String> map = new HashMap<>();
            String str = jnOtcDecoderParam.getStr();
            //密码返回
            if ("7E".equals(str.substring(0, 2)) && "53".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNPasswordReturn passwordReturn = JnOtcProtocolHandle.jnPasswordReturnAnalysis(str);
                if (null != passwordReturn) {
                    map.put(JNPasswordReturn.class.getSimpleName(), JSON.toJSONString(passwordReturn));
                }
            }
            //控制命令返回
            else if ("7E".equals(str.substring(0, 2)) && "54".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JNCommandReturn commandReturn = JnOtcProtocolHandle.jnCommandReturnAnalysis(str);
                if (null != commandReturn) {
                    map.put(JNCommandReturn.class.getSimpleName(), JSON.toJSONString(commandReturn));
                }
            }
            //锁焊机指令返回
            else if ("7E".equals(str.substring(0, 2)) && "18".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JnLockMachineReturn jnLockMachineReturn = JnOtcProtocolHandle.jnLockMachineReturnAnalysis(str);
                if (null != jnLockMachineReturn) {
                    map.put(JnLockMachineReturn.class.getSimpleName(), JSON.toJSONString(jnLockMachineReturn));
                }
            }
            //解锁焊机指令返回
            /*else if ("7E".equals(str.substring(0, 2)) && "19".equals(str.substring(10, 12)) && "7D".equals(str.substring(20, 22))) {
                JnLockMachineReturn jnLockMachineReturn = JnOtcProtocolHandle.jnLockMachineReturnAnalysis(str);
                if (null != jnLockMachineReturn) {
                    map.put(JnLockMachineReturn.class.getSimpleName(), JSON.toJSONString(jnLockMachineReturn));
                }
            }*/
            //程序包路径下发返回
            else if ("7E".equals(str.substring(0, 2)) && "11".equals(str.substring(12, 14)) && "7D".equals(str.substring(20, 22))) {
                OtcV1ProgramPathIssueReturn otcV1ProgramPathIssueReturn = JnOtcProtocolHandle.otcV1ProgramPathIssueReturn(str);
                if (null != otcV1ProgramPathIssueReturn) {
                    map.put(OtcV1ProgramPathIssueReturn.class.getSimpleName(), JSON.toJSONString(otcV1ProgramPathIssueReturn));
                }
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
            return handlerParam;
        }
        return null;
    }

    /**
     * OTC1.0实时数据解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam jnOtcRtDataAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            HandlerParam handlerParam = new HandlerParam();
            Map<String, String> map = new HashMap<>();
            //存数据库
            List<JNRtDataDB> jnRtDataDbs = JnOtcProtocolHandle.jnRtDataDbAnalysis(jnOtcDecoderParam.getStr());
            //发送前端
            List<JNRtDataUI> jnRtDataUis = JnOtcProtocolHandle.jnRtDataUiAnalysis(jnOtcDecoderParam.getClientIp(), jnOtcDecoderParam.getStr());
            if (CommonUtils.isNotEmpty(jnRtDataDbs)) {
                map.put(JNRtDataDB.class.getSimpleName(), JSON.toJSONString(jnRtDataDbs));
            }
            if (CommonUtils.isNotEmpty(jnRtDataUis)) {
                map.put(JNRtDataUI.class.getSimpleName(), JSON.toJSONString(jnRtDataUis));
            }
            handlerParam.setKey(jnOtcDecoderParam.getStr().length());
            handlerParam.setValue(map);
            return handlerParam;
        }
        return null;
    }

    /**
     * 工艺下发返回解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam otcIssueReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            JNProcessIssueReturn issueReturn = JnOtcProtocolHandle.jnIssueReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != issueReturn) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, String> map = new HashMap<>();
                map.put(JNProcessIssueReturn.class.getSimpleName(), JSON.toJSONString(issueReturn));
                handlerParam.setKey(jnOtcDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 索取返回协议解析
     *
     * @param jnOtcDecoderParam 入参
     * @return 返回HandlerParam
     */
    private HandlerParam otcClaimReturnAnalysis(JnOtcDecoderParam jnOtcDecoderParam) {
        if (null != jnOtcDecoderParam) {
            JNProcessClaimReturn claimReturn = JnOtcProtocolHandle.jnClaimReturnAnalysis(jnOtcDecoderParam.getStr());
            if (null != claimReturn) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, String> map = new HashMap<>();
                map.put(JNProcessClaimReturn.class.getSimpleName(), JSON.toJSONString(claimReturn));
                handlerParam.setKey(jnOtcDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }


}
