package com.shth.das.business;

import com.shth.das.codeparam.HandlerParam;
import com.shth.das.codeparam.JnSxDecoderParam;
import com.shth.das.codeparam.SxVerificationCode;
import com.shth.das.common.BaseAbstractDecoder;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.jnsx.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: 江南松下协议解析
 * @author: Shan Long
 * @create: 2021-08-09
 */
@Slf4j
public class JnSxDecoderAnalysis extends BaseAbstractDecoder {

    /**
     * 保存字符串长度和方法映射关系
     * Function<T,R>,T:传入参数，R：返回参数
     */
    private final Map<Integer, Function<JnSxDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();

    public JnSxDecoderAnalysis() {
        setDecoderMapping();
    }

    private void setDecoderMapping() {
        //松下【GL5、FR2、AT3】第一次握手验证
        this.decoderMapping.put(42, this::jnSxFirstVerify);
        //松下【GL5、FR2、AT3】第二次握手验证
        this.decoderMapping.put(128, this::jnSxSecondVerify);
        //松下焊机GL5软硬件参数
        this.decoderMapping.put(180, this::jnSxGl5SoftHardParam);
        //松下焊机GL5系列CO2实时数据
        this.decoderMapping.put(206, this::jnSxGl5RtDataAnalysis);
        //松下焊机GL5系列CO2状态信息
        this.decoderMapping.put(246, this::jnSxGl5StatusAnalysis);
        //松下焊机GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回、通道设定返回、通道读取返回】
        this.decoderMapping.put(106, this::jnSxGl5ProcessWeldSet);
        //松下焊机GL5系列CO2工艺索取返回（有数据）
        this.decoderMapping.put(406, this::jnSxGl5Co2ProcessClaimReturn);
        //松下焊机GL5系列TIG工艺索取返回（有数据）
        this.decoderMapping.put(446, this::jnSxGl5TigProcessClaimReturn);
        //松下焊机【FR2、AT3】系列软硬件参数
        this.decoderMapping.put(154, this::jnSxFr2At3SoftHardParam);
        //松下焊机FR2系列CO2实时数据
        this.decoderMapping.put(112, this::jnSxFr2Co2RtDataDbAnalysis);
        //松下焊机FR2系列TIG实时数据
        this.decoderMapping.put(118, this::jnSxFr2TigRtDataDbAnalysis);
        //松下焊机FR2系列CO2和TIG的状态信息
        this.decoderMapping.put(156, this::jnSxFr2StatusUiAnalysis);
        //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
        this.decoderMapping.put(52, this::jnSxChannelParamReplyAnalysis);
        //松下焊机FR2系列通道参数【查询回复（有参数）】
        this.decoderMapping.put(220, this::jnSxFr2ChannelParamReplyHave);
        //松下焊机AT3系列【查询回复（有参数）】
        this.decoderMapping.put(92, this::jnSxAt3ParamQueryReturn);
    }

    @Override
    public HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str) {
        if (this.decoderMapping.containsKey(str.length())) {
            String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            JnSxDecoderParam jnSxDecoderParam = new JnSxDecoderParam();
            jnSxDecoderParam.setStr(str);
            jnSxDecoderParam.setClientIp(clientIp);
            jnSxDecoderParam.setCtx(ctx);
            return this.decoderMapping.get(str.length()).apply(jnSxDecoderParam);
        }
        return null;
    }

    /**
     * 松下第一次握手验证
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxFirstVerify(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            try {
                final String str = jnSxDecoderParam.getStr();
                if (str.length() == 42 && "4E455430".equals(str.substring(0, 8))) {
                    final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_FIRST_VERIFICATION);
                }
            } catch (Exception e) {
                log.error("松下第一次握手验证异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下第二次握手验证
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxSecondVerify(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            try {
                final String str = jnSxDecoderParam.getStr();
                //松下焊机GL5/FR2/AT3第二次验证（下行）
                if (str.length() == 128 && "4C4A5348".equals(str.substring(0, 8))) {
                    final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    //设备CID（8个字节：字符长度则为：16）
                    String weldCid = str.substring(12, 28);
                    Map<String, Object> map = new HashMap<>();
                    map.put("weldCid", weldCid);
                    HandlerParam handlerParam = new HandlerParam();
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_SECOND_VERIFICATION);
                    return handlerParam;
                }
            } catch (Exception e) {
                log.error("松下第二次握手验证异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下焊机GL5软硬件参数
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5SoftHardParam(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            try {
                final String str = jnSxDecoderParam.getStr();
                //松下焊机GL5软硬件参数
                if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10)) && "0010".equals(str.substring(40, 44))) {
                    final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    final String clientIp = jnSxDecoderParam.getClientIp();
                    final Map<String, Object> map = new HashMap<>();
                    final HandlerParam handlerParam = new HandlerParam();
                    final SxWeldModel sxWeldModel = JnSxRtDataProtocol.sxWeldAnalysis(clientIp, str);
                    if (null != sxWeldModel) {
                        map.put("SxWeldModel", sxWeldModel);
                        ctx.channel().writeAndFlush(SxVerificationCode.SX_SOFT_HARDWARE_PARAM_DOWN);
                    }
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            } catch (Exception e) {
                log.error("松下焊机GL5软硬件参数异常：", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 松下焊机GL5实时数据
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5RtDataAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            //松下焊机GL5实时数据
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                final String clientIp = jnSxDecoderParam.getClientIp();
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataUi sxRtDataUi = JnSxRtDataProtocol.sxRtDataUiAnalysis(clientIp, str);
                SxRtDataDb sxRtDataDb = JnSxRtDataProtocol.sxRtDataDbAnalysis(clientIp, str);
                if (null != sxRtDataUi) {
                    map.put("SxRtDataUi", sxRtDataUi);
                }
                if (null != sxRtDataDb) {
                    map.put("SxRtDataDb", sxRtDataDb);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下焊机GL5系列CO2状态信息
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5StatusAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            //松下焊机GL5系列CO2状态信息
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                final String clientIp = jnSxDecoderParam.getClientIp();
                SxStatusDataUI sxStatusDataUi = JnSxRtDataProtocol.sxStatusDataUiAnalysis(clientIp, str);
                if (null != sxStatusDataUi) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("SxStatusDataUI", sxStatusDataUi);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下焊机GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回、通道设定返回、通道读取返回】
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5ProcessWeldSet(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            if (str.length() == 106) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                //松下GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回】
                if ("1201".equals(str.substring(40, 44))) {
                    //读写标志：0：主动上传；1：读取；2：设置；3：删除
                    if ("1".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺索取返回（无数据）
                        SxProcessClaimReturn sxProcessClaimReturn = JnSxRtDataProtocol.sxProcessClaimReturnAnalysis(clientIp, str);
                        if (null != sxProcessClaimReturn) {
                            map.put("SxProcessClaimReturn", sxProcessClaimReturn);
                        }
                    }
                    if ("2".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺下发回复
                        SxProcessReturn sxProcessReturn = JnSxRtDataProtocol.sxProcessReturnAnalysis(clientIp, str);
                        if (null != sxProcessReturn) {
                            map.put("SxProcessReturn", sxProcessReturn);
                        }
                    }
                    if ("3".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50028".equals(str.substring(0, 10))) {
                        //松下工艺删除回复
                        SxProcessDeleteReturn sxProcessDeleteReturn = JnSxRtDataProtocol.sxProcessDeleteReturnAnalysis(clientIp, str);
                        if (null != sxProcessDeleteReturn) {
                            map.put("SxProcessDeleteReturn", sxProcessDeleteReturn);
                        }
                    }
                }
                //松下GL5系列【通道设定返回、通道读取返回】
                if ("1202".equals(str.substring(40, 44)) && "FE5AA50035".equals(str.substring(0, 10))) {
                    SxWeldChannelSetReturn weldChannelSetReturn = JnSxRtDataProtocol.sxWeldChannelSetReturnAnalysis(clientIp, str);
                    if (null != weldChannelSetReturn) {
                        map.put("SxWeldChannelSetReturn", weldChannelSetReturn);
                    }
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下GL5系列CO2工艺索取返回
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5Co2ProcessClaimReturn(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            //松下GL5系列CO2工艺索取返回
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                final String clientIp = jnSxDecoderParam.getClientIp();
                SxCO2ProcessClaimReturn claimReturn = JnSxRtDataProtocol.sxCO2ProcessClaimReturnAnalysis(clientIp, str);
                if (null != claimReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("SxCO2ProcessClaimReturn", claimReturn);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下GL5系列TIG工艺索取返回
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxGl5TigProcessClaimReturn(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下GL5系列TIG工艺索取返回
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxTIGProcessClaimReturn sxTigProcessClaimReturn = JnSxRtDataProtocol.sxTIGProcessClaimReturnAnalysis(clientIp, str);
                if (null != sxTigProcessClaimReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("SxTIGProcessClaimReturn", sxTigProcessClaimReturn);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下焊机【FR2、AT3】系列软硬件参数
     *
     * @param jnSxDecoderParam 入参
     * @return HandlerParam
     */
    private HandlerParam jnSxFr2At3SoftHardParam(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            try {
                final String str = jnSxDecoderParam.getStr();
                final String clientIp = jnSxDecoderParam.getClientIp();
                final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                //判断是否是FR2或AT3的软硬件参数协议
                if (str.length() == 154 && "FE5AA5004D".equals(str.substring(0, 10)) && "0010".equals(str.substring(40, 44))) {
                    final SxWeldModel sxWeldModel = JnSxRtDataProtocol.jnSxFr2At3SoftHardParamAnalysis(clientIp, str);
                    final Map<String, Object> map = new HashMap<>();
                    final HandlerParam handlerParam = new HandlerParam();
                    if (null != sxWeldModel) {
                        map.put("SxWeldModel", sxWeldModel);
                        ctx.channel().writeAndFlush(SxVerificationCode.SX_SOFT_HARDWARE_PARAM_DOWN);
                    }
                    handlerParam.setKey(str.length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            } catch (Exception e) {
                log.error("松下焊机【FR2、AT3】系列软硬件参数协议解析异常：", e);
            }
        }
        return null;
    }

    /**
     * 松下FR2系列CO2焊机实时数据解析
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxFr2Co2RtDataDbAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列CO2实时数据
            if (str.length() == 112 && "FE5AA50038".equals(str.substring(0, 10))) {
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataDb sxRtDataDb = JnSxRtDataProtocol.fr2Co2RtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = JnSxRtDataProtocol.fr2Co2RtDataUiAnalysis(clientIp, str);
                if (null != sxRtDataDb) {
                    map.put("SxRtDataDb", sxRtDataDb);
                }
                if (null != sxRtDataUi) {
                    map.put("SxRtDataUi", sxRtDataUi);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列TIG实时数据
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxFr2TigRtDataDbAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列TIG实时数据
            if (str.length() == 118 && "FE5AA5003B".equals(str.substring(0, 10))) {
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataDb sxRtDataDb = JnSxRtDataProtocol.fr2TigRtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = JnSxRtDataProtocol.fr2TigRtDataUiAnalysis(clientIp, str);
                if (null != sxRtDataDb) {
                    map.put("SxRtDataDb", sxRtDataDb);
                }
                if (null != sxRtDataUi) {
                    map.put("SxRtDataUi", sxRtDataUi);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列CO2和TIG的状态信息
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxFr2StatusUiAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列CO2/TIG状态信息
            if (str.length() == 156 && "FE5AA5004E".equals(str.substring(0, 10))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                //CO2焊机
                if ("0102".equals(str.substring(40, 44)) && 1 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = JnSxRtDataProtocol.fr2Co2StatusUiAnalysis(clientIp, str);
                    if (null != sxStatusDataUi) {
                        map.put("SxStatusDataUI", sxStatusDataUi);
                    }
                }
                //TIG焊机
                if ("0102".equals(str.substring(40, 44)) && 4 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = JnSxRtDataProtocol.fr2TigStatusUiAnalysis(clientIp, str);
                    if (null != sxStatusDataUi) {
                        map.put("SxStatusDataUI", sxStatusDataUi);
                    }
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxChannelParamReplyAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
            if (str.length() == 52 && "FE5AA5001A".equals(str.substring(0, 10))) {
                SxChannelParamReply sxChannelParamReply = JnSxRtDataProtocol.sxChannelParamReplyAnalysis(clientIp, str);
                if (null != sxChannelParamReply) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("SxChannelParamReply", sxChannelParamReply);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询（有参数）
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxFr2ChannelParamReplyHave(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列通道参数查询（有参数）
            if (str.length() == 220 && "FE5AA5006E".equals(str.substring(0, 10))) {
                SxChannelParamReplyHave sxChannelParamReplyHave = JnSxRtDataProtocol.sxChannelParamReplyHaveAnalysis(clientIp, str);
                if (null != sxChannelParamReplyHave) {
                    Map<String, Object> map = new HashMap<>();
                    HandlerParam handlerParam = new HandlerParam();
                    map.put("SxChannelParamReplyHave", sxChannelParamReplyHave);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下AT3系列查询回复（有参数）
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxAt3ParamQueryReturn(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下AT3系列查询回复（有参数）
            if (str.length() == 92 && "FE5AA5002E".equals(str.substring(0, 10))) {
                At3ParamQueryReturn at3ParamQueryReturn = JnSxRtDataProtocol.at3ParamQueryReturnAnalysis(clientIp, str);
                if (null != at3ParamQueryReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("At3ParamQueryReturn", at3ParamQueryReturn);
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

}
