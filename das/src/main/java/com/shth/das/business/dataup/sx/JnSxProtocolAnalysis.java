package com.shth.das.business.dataup.sx;

import com.alibaba.fastjson2.JSON;
import com.shth.das.business.dataup.BaseAnalysis;
import com.shth.das.codeparam.HandlerParam;
import com.shth.das.codeparam.JnSxDecoderParam;
import com.shth.das.codeparam.SxVerificationCode;
import com.shth.das.common.CommonFunction;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.jnsx.*;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: 江南松下协议解析
 * @author: Shan Long
 * @create: 2021-08-09
 */
@Slf4j
public class JnSxProtocolAnalysis extends BaseAnalysis {

    /**
     * 保存字符串长度和方法映射关系
     * Function<T,R>,T:传入参数，R：返回参数
     */
    private final Map<Integer, Function<JnSxDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();

    public JnSxProtocolAnalysis() {
        initDecoderMapping();
    }

    private void initDecoderMapping() {
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
    public HandlerParam protocolAnalysis(ChannelHandlerContext ctx, String str) {
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
                String str = jnSxDecoderParam.getStr();
                if (str.length() == 42 && "4E455430".equals(str.substring(0, 8))) {
                    ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_FIRST_VERIFICATION);
                }
            } catch (Exception e) {
                log.error("松下第一次握手验证异常：", e);
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
                String str = jnSxDecoderParam.getStr();
                //松下焊机GL5/FR2/AT3第二次验证（下行）
                if (str.length() == 128 && "4C4A5348".equals(str.substring(0, 8))) {
                    ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    //设备CID（8个字节：字符长度则为：16）
                    String weldCid = str.substring(12, 28);
                    Map<String, String> map = new HashMap<>();
                    map.put("weldCid", weldCid);
                    HandlerParam handlerParam = new HandlerParam();
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_SECOND_VERIFICATION);
                    return handlerParam;
                }
            } catch (Exception e) {
                log.error("松下第二次握手验证异常：", e);
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
                String str = jnSxDecoderParam.getStr();
                //松下焊机GL5软硬件参数
                if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10)) && "0010".equals(str.substring(40, 44))) {
                    ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                    String clientIp = jnSxDecoderParam.getClientIp();
                    Map<String, String> map = new HashMap<>();
                    HandlerParam handlerParam = new HandlerParam();
                    SxWeldModel sxWeldModel = sxWeldAnalysis(clientIp, str);
                    if (null != sxWeldModel) {
                        map.put(SxWeldModel.class.getSimpleName(), JSON.toJSONString(sxWeldModel));
                        ctx.channel().writeAndFlush(SxVerificationCode.SX_SOFT_HARDWARE_PARAM_DOWN);
                    }
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            } catch (Exception e) {
                log.error("松下焊机GL5软硬件参数异常：", e);
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
            String str = jnSxDecoderParam.getStr();
            //松下焊机GL5实时数据
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                String clientIp = jnSxDecoderParam.getClientIp();
                Map<String, String> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataUi sxRtDataUi = sxRtDataUiAnalysis(clientIp, str);
                SxRtDataDb sxRtDataDb = sxRtDataDbAnalysis(clientIp, str);
                if (null != sxRtDataUi) {
                    map.put(SxRtDataUi.class.getSimpleName(), JSON.toJSONString(sxRtDataUi));
                }
                if (null != sxRtDataDb) {
                    map.put(SxRtDataDb.class.getSimpleName(), JSON.toJSONString(sxRtDataDb));
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
            String str = jnSxDecoderParam.getStr();
            //松下焊机GL5系列CO2状态信息
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                String clientIp = jnSxDecoderParam.getClientIp();
                SxStatusDataUI sxStatusDataUi = sxStatusDataUiAnalysis(clientIp, str);
                if (null != sxStatusDataUi) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, String> map = new HashMap<>();
                    map.put(SxStatusDataUI.class.getSimpleName(), JSON.toJSONString(sxStatusDataUi));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            if (str.length() == 106) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, String> map = new HashMap<>();
                //松下GL5系列【工艺下发返回、工艺索取返回(无数据)、工艺删除返回】
                if ("1201".equals(str.substring(40, 44))) {
                    //读写标志：0：主动上传；1：读取；2：设置；3：删除
                    if ("1".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺索取返回（无数据）
                        SxProcessClaimReturn sxProcessClaimReturn = sxProcessClaimReturnAnalysis(clientIp, str);
                        if (null != sxProcessClaimReturn) {
                            map.put(SxProcessClaimReturn.class.getSimpleName(), JSON.toJSONString(sxProcessClaimReturn));
                        }
                    }
                    if ("2".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺下发回复
                        SxProcessReturn sxProcessReturn = sxProcessReturnAnalysis(clientIp, str);
                        if (null != sxProcessReturn) {
                            map.put(SxProcessReturn.class.getSimpleName(), JSON.toJSONString(sxProcessReturn));
                        }
                    }
                    if ("3".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50028".equals(str.substring(0, 10))) {
                        //松下工艺删除回复
                        SxProcessDeleteReturn sxProcessDeleteReturn = sxProcessDeleteReturnAnalysis(clientIp, str);
                        if (null != sxProcessDeleteReturn) {
                            map.put(SxProcessDeleteReturn.class.getSimpleName(), JSON.toJSONString(sxProcessDeleteReturn));
                        }
                    }
                }
                //松下GL5系列【通道设定返回、通道读取返回】
                if ("1202".equals(str.substring(40, 44)) && "FE5AA50035".equals(str.substring(0, 10))) {
                    SxWeldChannelSetReturn weldChannelSetReturn = sxWeldChannelSetReturnAnalysis(clientIp, str);
                    if (null != weldChannelSetReturn) {
                        map.put(SxWeldChannelSetReturn.class.getSimpleName(), JSON.toJSONString(weldChannelSetReturn));
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
            String str = jnSxDecoderParam.getStr();
            //松下GL5系列CO2工艺索取返回
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                String clientIp = jnSxDecoderParam.getClientIp();
                SxCO2ProcessClaimReturn claimReturn = sxCO2ProcessClaimReturnAnalysis(clientIp, str);
                if (null != claimReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, String> map = new HashMap<>();
                    map.put(SxCO2ProcessClaimReturn.class.getSimpleName(), JSON.toJSONString(claimReturn));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下GL5系列TIG工艺索取返回
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxTIGProcessClaimReturn sxTigProcessClaimReturn = sxTIGProcessClaimReturnAnalysis(clientIp, str);
                if (null != sxTigProcessClaimReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, String> map = new HashMap<>();
                    map.put(SxTIGProcessClaimReturn.class.getSimpleName(), JSON.toJSONString(sxTigProcessClaimReturn));
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
                String str = jnSxDecoderParam.getStr();
                String clientIp = jnSxDecoderParam.getClientIp();
                ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                //判断是否是FR2或AT3的软硬件参数协议
                if (str.length() == 154 && "FE5AA5004D".equals(str.substring(0, 10)) && "0010".equals(str.substring(40, 44))) {
                    SxWeldModel sxWeldModel = jnSxFr2At3SoftHardParamAnalysis(clientIp, str);
                    Map<String, String> map = new HashMap<>();
                    HandlerParam handlerParam = new HandlerParam();
                    if (null != sxWeldModel) {
                        map.put(SxWeldModel.class.getSimpleName(), JSON.toJSONString(sxWeldModel));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列CO2实时数据
            if (str.length() == 112 && "FE5AA50038".equals(str.substring(0, 10))) {
                Map<String, String> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataDb sxRtDataDb = fr2Co2RtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = fr2Co2RtDataUiAnalysis(clientIp, str);
                if (null != sxRtDataDb) {
                    map.put(SxRtDataDb.class.getSimpleName(), JSON.toJSONString(sxRtDataDb));
                }
                if (null != sxRtDataUi) {
                    map.put(SxRtDataUi.class.getSimpleName(), JSON.toJSONString(sxRtDataUi));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列TIG实时数据
            if (str.length() == 118 && "FE5AA5003B".equals(str.substring(0, 10))) {
                Map<String, String> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataDb sxRtDataDb = fr2TigRtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = fr2TigRtDataUiAnalysis(clientIp, str);
                if (null != sxRtDataDb) {
                    map.put(SxRtDataDb.class.getSimpleName(), JSON.toJSONString(sxRtDataDb));
                }
                if (null != sxRtDataUi) {
                    map.put(SxRtDataUi.class.getSimpleName(), JSON.toJSONString(sxRtDataUi));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列CO2/TIG状态信息
            if (str.length() == 156 && "FE5AA5004E".equals(str.substring(0, 10))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, String> map = new HashMap<>();
                //CO2焊机
                if ("0102".equals(str.substring(40, 44)) && 1 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = fr2Co2StatusUiAnalysis(clientIp, str);
                    if (null != sxStatusDataUi) {
                        map.put(SxStatusDataUI.class.getSimpleName(), JSON.toJSONString(sxStatusDataUi));
                    }
                }
                //TIG焊机
                if ("0102".equals(str.substring(40, 44)) && 4 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = fr2TigStatusUiAnalysis(clientIp, str);
                    if (null != sxStatusDataUi) {
                        map.put(SxStatusDataUI.class.getSimpleName(), JSON.toJSONString(sxStatusDataUi));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下焊机【FR2、AT3】系列通道参数【查询回复（无参数）、下载回复、删除回复】
            if (str.length() == 52 && "FE5AA5001A".equals(str.substring(0, 10))) {
                SxChannelParamReply sxChannelParamReply = sxChannelParamReplyAnalysis(clientIp, str);
                if (null != sxChannelParamReply) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, String> map = new HashMap<>();
                    map.put(SxChannelParamReply.class.getSimpleName(), JSON.toJSONString(sxChannelParamReply));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列通道参数查询（有参数）
            if (str.length() == 220 && "FE5AA5006E".equals(str.substring(0, 10))) {
                SxChannelParamReplyHave sxChannelParamReplyHave = sxChannelParamReplyHaveAnalysis(clientIp, str);
                if (null != sxChannelParamReplyHave) {
                    Map<String, String> map = new HashMap<>();
                    HandlerParam handlerParam = new HandlerParam();
                    map.put(SxChannelParamReplyHave.class.getSimpleName(), JSON.toJSONString(sxChannelParamReplyHave));
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
            String str = jnSxDecoderParam.getStr();
            String clientIp = jnSxDecoderParam.getClientIp();
            //松下AT3系列查询回复（有参数）
            if (str.length() == 92 && "FE5AA5002E".equals(str.substring(0, 10))) {
                At3ParamQueryReturn at3ParamQueryReturn = at3ParamQueryReturnAnalysis(clientIp, str);
                if (null != at3ParamQueryReturn) {
                    HandlerParam handlerParam = new HandlerParam();
                    Map<String, String> map = new HashMap<>();
                    map.put(At3ParamQueryReturn.class.getSimpleName(), JSON.toJSONString(at3ParamQueryReturn));
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            }
        }
        return null;
    }

    /**
     * 松下GL5软硬件参数解析
     */
    private SxWeldModel sxWeldAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10))) {
                SxWeldModel sxWeldModel = new SxWeldModel();
                sxWeldModel.setWeldIp(clientIp);//客户端IP地址
                sxWeldModel.setWeldModel(CommonUtils.convertHexToString(str.substring(44, 62))); //设备机型
                sxWeldModel.setPowerSupply(Integer.valueOf(str.substring(62, 64), 16));//电源类型
                sxWeldModel.setWireFeederModel(Integer.valueOf(str.substring(64, 66), 16));//送丝机类型
                sxWeldModel.setWeldKind(Integer.valueOf(str.substring(70, 72), 16));//设备种类
                sxWeldModel.setWeldCode(str.substring(72, 82)); //设备编码
                sxWeldModel.setWeldCpuNum(Integer.valueOf(str.substring(82, 84), 16));//焊机CPU个数
                sxWeldModel.setCpu1No(Integer.valueOf(str.substring(84, 86), 16).toString());//cpu1编号
                sxWeldModel.setCpu1Model(Integer.valueOf(str.substring(86, 90), 16));
                sxWeldModel.setCpu1Version(CommonUtils.convertHexToString(str.substring(90, 114))); //cpu1软件版本
                sxWeldModel.setCpu2No(Integer.valueOf(str.substring(114, 116), 16).toString());
                sxWeldModel.setCpu2Model(Integer.valueOf(str.substring(116, 120), 16));
                sxWeldModel.setCpu2Version(CommonUtils.convertHexToString(str.substring(120, 144)));
                sxWeldModel.setCpu3No(Integer.valueOf(str.substring(144, 146), 16).toString());
                sxWeldModel.setCpu3Model(Integer.valueOf(str.substring(146, 150), 16));
                sxWeldModel.setCpu3Version(CommonUtils.convertHexToString(str.substring(150, 174)));
                sxWeldModel.setWeldNo("0001");
                sxWeldModel.setCreateTime(DateTimeUtils.getNowDateTime()); //创建时间
                return sxWeldModel;
            }
        }
        return null;
    }

    /**
     * 松下GL5状态信息解析发mq
     *
     * @param str 16进制字符串
     * @return 松下GL5状态数据实体类
     */
    private SxStatusDataUI sxStatusDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxStatusDataUi.setWeldIp(clientIp);
                sxStatusDataUi.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxStatusDataUi.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                sxStatusDataUi.setModeSelect(Integer.valueOf(str.substring(84, 86), 16));
                sxStatusDataUi.setWeldingControl(Integer.valueOf(str.substring(86, 88), 16));
                sxStatusDataUi.setWeldingManner(Integer.valueOf(str.substring(88, 90), 16));
                sxStatusDataUi.setTexture(Integer.valueOf(str.substring(90, 92), 16));
                sxStatusDataUi.setWireDiameter(Integer.valueOf(str.substring(92, 94), 16));
                sxStatusDataUi.setGases(Integer.valueOf(str.substring(94, 96), 16));
                sxStatusDataUi.setWireFeed(Integer.valueOf(str.substring(96, 98), 16));
                sxStatusDataUi.setCheckGasStatus(Integer.valueOf(str.substring(98, 100), 16));
                sxStatusDataUi.setCutStatus(Integer.valueOf(str.substring(100, 102), 16));
                sxStatusDataUi.setLockStatus(Integer.valueOf(str.substring(102, 104), 16));
                sxStatusDataUi.setEleShowSelect(Integer.valueOf(str.substring(104, 106), 16));
                sxStatusDataUi.setRev(BigDecimal.valueOf(Long.valueOf(str.substring(106, 108), 16)));
                sxStatusDataUi.setBoardThickness(BigDecimal.valueOf(Long.valueOf(str.substring(108, 110), 16)));
                sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                sxStatusDataUi.setVolShowSelect(Integer.valueOf(str.substring(114, 116), 16));
                sxStatusDataUi.setArcLength(BigDecimal.valueOf(Long.valueOf(str.substring(116, 118), 16)));
                sxStatusDataUi.setArcCharacter(Integer.valueOf(str.substring(118, 120), 16));
                sxStatusDataUi.setPenetrationControl(Integer.valueOf(str.substring(120, 122), 16));
                sxStatusDataUi.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                sxStatusDataUi.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                sxStatusDataUi.setUnitaryDifference(Integer.valueOf(str.substring(130, 132), 16));
                sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(132, 134), 16));
                sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(134, 136), 16));
                sxStatusDataUi.setWeldFlag(0);
                return sxStatusDataUi;
            }
        }
        return null;
    }

    /**
     * 松下GL5实时数据解析发mq
     *
     * @return SxRtDataUI
     */
    private SxRtDataUi sxRtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                try {
                    SxRtDataUi sxRtDataUi = new SxRtDataUi();
                    String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                    String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                    String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                    String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                    String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                    String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                    //毫秒
                    String millisecond = Integer.valueOf(str.substring(58, 62), 16).toString();
                    //焊机时间
                    String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                    sxRtDataUi.setWeldIp(clientIp);
                    sxRtDataUi.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                    sxRtDataUi.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                    sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(84, 88), 16));
                    sxRtDataUi.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
                    sxRtDataUi.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
                    sxRtDataUi.setInitialWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
                    sxRtDataUi.setWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
                    sxRtDataUi.setWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
                    sxRtDataUi.setWeldWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(108, 112), 16)));
                    sxRtDataUi.setArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(112, 116), 16)));
                    sxRtDataUi.setArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(116, 120), 16)));
                    sxRtDataUi.setArcWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(120, 124), 16)));
                    sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
                    sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
                    sxRtDataUi.setRealityWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
                    sxRtDataUi.setWeldFlag(0);
                    return sxRtDataUi;
                } catch (Exception e) {
                    log.error("松下GL5实时数据解析发MQTT异常：", e);
                }
            }
        }
        return null;
    }

    /**
     * 松下实时数据解析存数据库
     *
     * @param str 16进制字符串
     * @return SxRtDataDB
     */
    private SxRtDataDb sxRtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                try {
                    //判断松下待机数据是否存储,如果不存储，则取出待机状态判断
                    if (!CommonFunction.isSxStandbySave()) {
                        Integer sxStandby = Integer.valueOf(str.substring(84, 88), 16);
                        //焊接状态为0表示待机，则直接进入下一次循环
                        if (sxStandby == 0) {
                            return null;
                        }
                    }
                    SxRtDataDb sxRtDataDb = new SxRtDataDb();
                    String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                    String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                    String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                    String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                    String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                    String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                    String millisecond = Integer.valueOf(str.substring(58, 62), 16).toString();
                    String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    sxRtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                    sxRtDataDb.setWeldIp(clientIp);
                    sxRtDataDb.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                    sxRtDataDb.setFunctionFlag(Integer.valueOf(str.substring(76, 80), 16).toString());
                    sxRtDataDb.setWeldStatus(Integer.valueOf(str.substring(84, 88), 16));
                    sxRtDataDb.setInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
                    sxRtDataDb.setInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
                    sxRtDataDb.setInitialWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
                    sxRtDataDb.setWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
                    sxRtDataDb.setWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
                    sxRtDataDb.setWeldWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(108, 112), 16)));
                    sxRtDataDb.setArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(112, 116), 16)));
                    sxRtDataDb.setArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(116, 120), 16)));
                    sxRtDataDb.setArcWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(120, 124), 16)));
                    sxRtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
                    sxRtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
                    sxRtDataDb.setRealityWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
                    sxRtDataDb.setAlarmsCode(Integer.valueOf(str.substring(140, 144), 16).toString());
                    sxRtDataDb.setCreateTime(DateTimeUtils.getNowDateTime());
                    sxRtDataDb.setWeldFlag(0);
                    return sxRtDataDb;
                } catch (Exception e) {
                    log.error("松下GL5实时数据解析存DB异常：", e);
                }
            }
        }
        return null;
    }

    /**
     * 松下工艺下发回复解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 工艺返回实体类
     */
    private SxProcessReturn sxProcessReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50035".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxProcessReturn sxProcessReturn = new SxProcessReturn();
                sxProcessReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxProcessReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxProcessReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxProcessReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                return sxProcessReturn;
            }
        }
        return null;
    }

    /**
     * 松下工艺删除回复解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    private SxProcessDeleteReturn sxProcessDeleteReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50028".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxProcessDeleteReturn sxProcessDeleteReturn = new SxProcessDeleteReturn();
                sxProcessDeleteReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                sxProcessDeleteReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                sxProcessDeleteReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                sxProcessDeleteReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                return sxProcessDeleteReturn;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询（无参数）、下载、删除回复
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxChannelParamReply
     */
    private SxChannelParamReply sxChannelParamReplyAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if ("0211".equals(str.substring(40, 44))) {
                SxChannelParamReply sxChannelParamReply = new SxChannelParamReply();
                sxChannelParamReply.setCommand(Integer.valueOf(str.substring(44, 46), 16));
                sxChannelParamReply.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
                sxChannelParamReply.setWeldIp(clientIp);
                return sxChannelParamReply;
            }
        }
        return null;
    }

    /**
     * 松下工艺索取回复解析（无数据）
     *
     * @param clientIp
     * @param str
     * @return
     */
    private SxProcessClaimReturn sxProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxProcessClaimReturn sxProcessClaimReturn = new SxProcessClaimReturn();
            sxProcessClaimReturn.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxProcessClaimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxProcessClaimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
            sxProcessClaimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
            sxProcessClaimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
            return sxProcessClaimReturn;
        }
        return null;
    }

    /**
     * 松下焊机通道设定回复解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下焊机通道设定回复实体类
     */
    private SxWeldChannelSetReturn sxWeldChannelSetReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 106 && "FE5AA50035".equals(str.substring(0, 10)) && "1202".equals(str.substring(40, 44))) {
                SxWeldChannelSetReturn weldChannelSetReturn = new SxWeldChannelSetReturn();
                weldChannelSetReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                weldChannelSetReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                //读写标志：0：主动上传；1：读取；2：设置；3：删除
                weldChannelSetReturn.setReadWriteFlag(Integer.valueOf(str.substring(70, 72), 16));
                weldChannelSetReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                weldChannelSetReturn.setFunction(Integer.valueOf(str.substring(78, 80), 16));
                weldChannelSetReturn.setChannelSelect(Integer.valueOf(str.substring(80, 82), 16));
                return weldChannelSetReturn;
            }
        }
        return null;
    }

    /**
     * 松下FR2和AT3软硬件参数协议解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxWeldModel
     */
    private SxWeldModel jnSxFr2At3SoftHardParamAnalysis(String clientIp, String str) {
        if (StringUtils.isNotBlank(str)) {
            if (str.length() == 154 && "FE5AA5004D".equals(str.substring(0, 10))) {
                SxWeldModel sxWeldModel = new SxWeldModel();
                sxWeldModel.setWeldIp(clientIp);
                sxWeldModel.setWeldKind(Integer.valueOf(str.substring(52, 54), 16));
                sxWeldModel.setWeldModel(CommonUtils.convertHexToString(str.substring(54, 72)));
                sxWeldModel.setWeldCode(str.substring(82, 92));
                sxWeldModel.setWeldCpuNum(Integer.valueOf(str.substring(92, 94), 16));
                sxWeldModel.setCpu1No(Integer.valueOf(str.substring(94, 96), 16).toString());
                sxWeldModel.setCpu1Model(Integer.valueOf(str.substring(96, 100)));
                String cpu1Version = str.substring(100, 104);
                sxWeldModel.setCpu1Version("5453".equals(cpu1Version) ? CommonUtils.convertHexToString(str.substring(100, 124)) : str.substring(100, 124));
                sxWeldModel.setWeldNo("0001");
                sxWeldModel.setCreateTime(DateTimeUtils.getNowDateTime());
                return sxWeldModel;
            }
        }
        return null;
    }

    /**
     * 松下CO2焊机工艺索取返回协议解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下CO2焊机工艺索取返回实体类
     */
    private SxCO2ProcessClaimReturn sxCO2ProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxCO2ProcessClaimReturn claimReturn = new SxCO2ProcessClaimReturn();
                claimReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                claimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                claimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                claimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                claimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
                claimReturn.setChannelFlag(Integer.valueOf(str.substring(80, 82), 16));
                //预置参数
                claimReturn.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(82, 86), 16)));
                claimReturn.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(86, 90), 16)));
                claimReturn.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
                claimReturn.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(94, 98), 16)));
                claimReturn.setInitialWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(98, 102), 16)));
                claimReturn.setInitialWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(102, 106), 16)));
                claimReturn.setWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
                claimReturn.setWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                claimReturn.setWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(114, 118), 16)));
                claimReturn.setWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
                claimReturn.setWeldWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                claimReturn.setWeldWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                claimReturn.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
                claimReturn.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138), 16)));
                claimReturn.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(138, 142), 16)));
                claimReturn.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(142, 146), 16)));
                claimReturn.setArcWireSpeedMax(BigDecimal.valueOf(Long.valueOf(str.substring(146, 150), 16)));
                claimReturn.setArcWireSpeedMin(BigDecimal.valueOf(Long.valueOf(str.substring(150, 154), 16)));
                //焊接条件
                claimReturn.setModeSelect(Integer.valueOf(str.substring(174, 176), 16));
                claimReturn.setWeldingControl(Integer.valueOf(str.substring(176, 178), 16));
                claimReturn.setWeldingManner(Integer.valueOf(str.substring(178, 180), 16));
                claimReturn.setTexture(Integer.valueOf(str.substring(180, 182), 16));
                claimReturn.setWireDiameter(Integer.valueOf(str.substring(182, 184), 16));
                claimReturn.setGases(Integer.valueOf(str.substring(184, 186), 16));
                claimReturn.setWireFeed(Integer.valueOf(str.substring(186, 188), 16));
                claimReturn.setCheckGasStatus(Integer.valueOf(str.substring(188, 190), 16));
                claimReturn.setCutStatus(Integer.valueOf(str.substring(190, 192), 16));
                claimReturn.setLockStatus(Integer.valueOf(str.substring(192, 194), 16));
                claimReturn.setEleShowSelect(Integer.valueOf(str.substring(194, 196), 16));
                claimReturn.setRev(BigDecimal.valueOf(Long.valueOf(str.substring(196, 198), 16)));
                claimReturn.setBoardThickness(BigDecimal.valueOf(Long.valueOf(str.substring(198, 200), 16)));
                claimReturn.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(200, 204), 16)));
                claimReturn.setVolShowSelect(Integer.valueOf(str.substring(204, 206), 16));
                claimReturn.setArcLength(BigDecimal.valueOf(Long.valueOf(str.substring(206, 208), 16)));
                claimReturn.setArcCharacter(Integer.valueOf(str.substring(208, 210), 16));
                claimReturn.setPenetrationControl(Integer.valueOf(str.substring(210, 212), 16));
                claimReturn.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(212, 216), 16)));
                claimReturn.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(216, 220), 16)));
                claimReturn.setUnitaryDifference(Integer.valueOf(str.substring(220, 222), 16));
                claimReturn.setNowChannel(Integer.valueOf(str.substring(222, 224), 16));
                //动态限流参数
                claimReturn.setDclInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(244, 248), 16)));
                claimReturn.setDclInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(248, 252), 16)));
                claimReturn.setDclWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(252, 256), 16)));
                claimReturn.setDclWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(256, 260), 16)));
                claimReturn.setDclArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(260, 264), 16)));
                claimReturn.setDclArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(264, 268), 16)));
                claimReturn.setStartDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(268, 270), 16)));
                claimReturn.setClAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(270, 272), 16)));
                //超限报警参数
                claimReturn.setOaInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(292, 296), 16)));
                claimReturn.setOaInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(296, 300), 16)));
                claimReturn.setOaInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(300, 304), 16)));
                claimReturn.setOaInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(304, 308), 16)));
                claimReturn.setOaWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(308, 312), 16)));
                claimReturn.setOaWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(312, 316), 16)));
                claimReturn.setOaWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(316, 320), 16)));
                claimReturn.setOaWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(320, 324), 16)));
                claimReturn.setOaArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(324, 328), 16)));
                claimReturn.setOaArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(328, 332), 16)));
                claimReturn.setOaArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(332, 336), 16)));
                claimReturn.setOaArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(336, 340), 16)));
                claimReturn.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(340, 342), 16)));
                claimReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(342, 346), 16)));
                claimReturn.setHaltDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(346, 348), 16)));
                claimReturn.setHaltFreezeTime(BigDecimal.valueOf(Long.valueOf(str.substring(348, 350), 16)));
                return claimReturn;
            }
        }
        return null;
    }

    /**
     * 松下TIG焊机工艺索取返回协议解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return 松下TIG焊机工艺索取返回实体类
     */
    private SxTIGProcessClaimReturn sxTIGProcessClaimReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                SxTIGProcessClaimReturn claimReturn = new SxTIGProcessClaimReturn();
                claimReturn.setWeldIp(clientIp);
                String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
                String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
                String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
                String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
                String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
                String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
                String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                claimReturn.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
                claimReturn.setWeldType(Integer.valueOf(str.substring(72, 74), 16));
                claimReturn.setChannelNo(Integer.valueOf(str.substring(76, 78), 16).toString());
                claimReturn.setDataFlag(Integer.valueOf(str.substring(78, 80), 16));
                claimReturn.setChannelFlag(Integer.valueOf(str.substring(80, 82), 16));
                //预置参数
                claimReturn.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(82, 86), 16)));
                claimReturn.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(86, 90), 16)));
                claimReturn.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
                claimReturn.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(94, 98), 16)));
                claimReturn.setFirstWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(98, 102), 16)));
                claimReturn.setFirstWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(102, 106), 16)));
                claimReturn.setFirstWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
                claimReturn.setFirstWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
                claimReturn.setSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(114, 118), 16)));
                claimReturn.setSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
                claimReturn.setSecondWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
                claimReturn.setSecondWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
                claimReturn.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
                claimReturn.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138), 16)));
                claimReturn.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(138, 142), 16)));
                claimReturn.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(142, 146), 16)));
                claimReturn.setPeakWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(146, 150), 16)));
                claimReturn.setPeakWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(150, 154), 16)));
                claimReturn.setPeakWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(154, 158), 16)));
                claimReturn.setPeakWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(158, 162), 16)));
                //焊接条件
                claimReturn.setWeldMethod(Integer.valueOf(str.substring(182, 184), 16));
                claimReturn.setArcHaveNot(Integer.valueOf(str.substring(184, 186), 16));
                claimReturn.setPulseHaveNot(Integer.valueOf(str.substring(186, 188), 16));
                claimReturn.setAcWaveform(Integer.valueOf(str.substring(188, 190), 16));
                claimReturn.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(190, 194), 16)));
                claimReturn.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(194, 198), 16)));
                claimReturn.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(198, 202), 16)));
                claimReturn.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(202, 206), 16)));
                claimReturn.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(206, 210), 16)));
                claimReturn.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(210, 214), 16)));
                claimReturn.setPulseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(214, 218), 16)));
                claimReturn.setArcStiffness(BigDecimal.valueOf(Long.valueOf(str.substring(218, 222), 16)));
                claimReturn.setHandWeldThrust(BigDecimal.valueOf(Long.valueOf(str.substring(222, 226), 16)));
                claimReturn.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(226, 230), 16)));
                claimReturn.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(230, 234), 16)));
                claimReturn.setMainWeldRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(234, 238), 16)));
                claimReturn.setMainWeldDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(238, 242), 16)));
                claimReturn.setMainWeldRiseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(242, 246), 16)));
                claimReturn.setMainWeldDeclineRadian(BigDecimal.valueOf(Long.valueOf(str.substring(246, 250), 16)));
                claimReturn.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(250, 254), 16)));
                claimReturn.setSpotWeldIntervalTime(BigDecimal.valueOf(Long.valueOf(str.substring(254, 258), 16)));
                claimReturn.setSpotWeldRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(258, 262), 16)));
                claimReturn.setSpotWeldDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(262, 266), 16)));
                claimReturn.setSpotWeldRiseRadian(BigDecimal.valueOf(Long.valueOf(str.substring(266, 270), 16)));
                claimReturn.setSpotWeldDeclineRadian(BigDecimal.valueOf(Long.valueOf(str.substring(270, 274), 16)));
                claimReturn.setMaxChannel(Integer.valueOf(str.substring(274, 276), 16));
                claimReturn.setNowChannel(Integer.valueOf(str.substring(276, 278), 16));
                claimReturn.setHandWeldWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(278, 282), 16)));
                claimReturn.setHandWeldArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(282, 286), 16)));
                //动态限流参数
                claimReturn.setDclInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(298, 302), 16)));
                claimReturn.setDclInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(302, 306), 16)));
                claimReturn.setDclWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(306, 310), 16)));
                claimReturn.setDclWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(310, 314), 16)));
                claimReturn.setDclSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(314, 318), 16)));
                claimReturn.setDclSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(318, 322), 16)));
                claimReturn.setDclArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(322, 326), 16)));
                claimReturn.setDclArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(326, 330), 16)));
                claimReturn.setStartDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(330, 332), 16)));
                claimReturn.setClAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(332, 334), 16)));
                //超限报警参数
                claimReturn.setOaInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(354, 358), 16)));
                claimReturn.setOaInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(358, 362), 16)));
                claimReturn.setOaInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(362, 366), 16)));
                claimReturn.setOaInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(366, 370), 16)));
                claimReturn.setOaWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(370, 374), 16)));
                claimReturn.setOaWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(374, 378), 16)));
                claimReturn.setOaWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(378, 382), 16)));
                claimReturn.setOaWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(382, 386), 16)));
                claimReturn.setOaSecondWeldEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(386, 390), 16)));
                claimReturn.setOaSecondWeldEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(390, 394), 16)));
                claimReturn.setOaSecondWeldVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(394, 398), 16)));
                claimReturn.setOaSecondWeldVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(398, 402), 16)));
                claimReturn.setOaArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(402, 406), 16)));
                claimReturn.setOaArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(406, 410), 16)));
                claimReturn.setOaArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(410, 414), 16)));
                claimReturn.setOaArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(414, 418), 16)));
                claimReturn.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(418, 420), 16)));
                claimReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(420, 422), 16)));
                claimReturn.setHaltDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(422, 424), 16)));
                claimReturn.setHaltFreezeTime(BigDecimal.valueOf(Long.valueOf(str.substring(424, 426), 16)));
                return claimReturn;
            }
        }
        return null;
    }

    /**
     * 松下FR2系列CO2实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制数据包
     * @return 实时表实体类
     */
    private SxRtDataDb fr2Co2RtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            //判断松下待机数据是否存储,如果不存储，则取出待机状态判断
            if (!CommonFunction.isSxStandbySave()) {
                Integer sxStandby = Integer.valueOf(str.substring(70, 72), 16);
                //焊接状态为0表示待机，则直接进入下一次循环
                if (sxStandby == 0) {
                    return null;
                }
            }
            SxRtDataDb rtDataDb = new SxRtDataDb();
            rtDataDb.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            rtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            rtDataDb.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            rtDataDb.setPresetEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            rtDataDb.setPresetVol(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            rtDataDb.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            rtDataDb.setPresetInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            rtDataDb.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            rtDataDb.setPresetArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            rtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            rtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            rtDataDb.setWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
            rtDataDb.setAlarmsCode(Integer.valueOf(str.substring(108, 112), 16).toString());
            //rtDataDb.setRunFlag(Integer.valueOf(str.substring(118, 120), 16));
            rtDataDb.setWeldFlag(1);
            return rtDataDb;
        }
        return null;
    }

    /**
     * 松下FR2系列CO2实时数据解析
     *
     * @param clientIp 设备IP
     * @param str      16进制字符串
     * @return SxRtDataUI
     */
    private SxRtDataUi fr2Co2RtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataUi sxRtDataUi = new SxRtDataUi();
            sxRtDataUi.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            sxRtDataUi.setPresetEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            sxRtDataUi.setPresetVol(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            sxRtDataUi.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            sxRtDataUi.setPresetInitialVol(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            sxRtDataUi.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            sxRtDataUi.setPresetArcVol(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            sxRtDataUi.setWireSpeed(BigDecimal.valueOf(Long.valueOf(str.substring(104, 108), 16)));
            sxRtDataUi.setAlarmsCode(Integer.valueOf(str.substring(108, 112), 16).toString());
            //sxRtDataUi.setRunFlag(Integer.valueOf(str.substring(118, 120), 16));
            sxRtDataUi.setWeldFlag(1);
            return sxRtDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制数据包
     * @return FR2系列TIG实体类
     */
    private SxRtDataDb fr2TigRtDataDbAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 118) {
            //判断松下待机数据是否存储,如果不存储，则取出待机状态判断
            if (!CommonFunction.isSxStandbySave()) {
                Integer sxStandby = Integer.valueOf(str.substring(70, 72), 16);
                //焊接状态为0表示待机，则直接进入下一次循环
                if (sxStandby == 0) {
                    return null;
                }
            }
            SxRtDataDb rtDataDb = new SxRtDataDb();
            rtDataDb.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            rtDataDb.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            rtDataDb.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            rtDataDb.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            rtDataDb.setPresetWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            rtDataDb.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            rtDataDb.setPresetMaxEle(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            rtDataDb.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));
            rtDataDb.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
            rtDataDb.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(94, 96), 16)));
            rtDataDb.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            rtDataDb.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            rtDataDb.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(104, 106), 16)));
            rtDataDb.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
            rtDataDb.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            rtDataDb.setAlarmsCode(Integer.valueOf(str.substring(114, 118), 16).toString());
            rtDataDb.setWeldFlag(2);
            return rtDataDb;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG实时数据解析
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxRtDataUi
     */
    private SxRtDataUi fr2TigRtDataUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxRtDataUi sxRtDataUi = new SxRtDataUi();
            sxRtDataUi.setWeldIp(clientIp);
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxRtDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxRtDataUi.setWeldStatus(Integer.valueOf(str.substring(70, 72), 16));
            sxRtDataUi.setPresetInitialEle(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            sxRtDataUi.setPresetWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            sxRtDataUi.setPresetArcEle(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            sxRtDataUi.setPresetMaxEle(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            sxRtDataUi.setPulseRate(BigDecimal.valueOf(Long.valueOf(str.substring(88, 90), 16)));
            sxRtDataUi.setPulseFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(90, 94), 16)));
            sxRtDataUi.setCleanWidth(BigDecimal.valueOf(Long.valueOf(str.substring(94, 96), 16)));
            sxRtDataUi.setAcFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            sxRtDataUi.setMixFrequency(BigDecimal.valueOf(Long.valueOf(str.substring(100, 104), 16)));
            sxRtDataUi.setMixAcRate(BigDecimal.valueOf(Long.valueOf(str.substring(104, 106), 16)));
            sxRtDataUi.setRealityWeldEle(BigDecimal.valueOf(Long.valueOf(str.substring(106, 110), 16)));
            sxRtDataUi.setRealityWeldVol(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            sxRtDataUi.setAlarmsCode(Integer.valueOf(str.substring(114, 118), 16).toString());
            sxRtDataUi.setWeldFlag(2);
            return sxRtDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列TIG焊机状态数据解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    private SxStatusDataUI fr2TigStatusUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxStatusDataUi.setWeldIp(clientIp);
            sxStatusDataUi.setWeldModel(CommonUtils.convertHexToString(str.substring(70, 88)));
            sxStatusDataUi.setWelderNo(str.substring(88, 98));
            sxStatusDataUi.setWorkpieceNoMin(str.substring(98, 108));
            sxStatusDataUi.setWeldMethod(Integer.valueOf(str.substring(108, 110), 16));
            sxStatusDataUi.setArcHaveNot(Integer.valueOf(str.substring(112, 114), 16));
            sxStatusDataUi.setPulseHaveNot(Integer.valueOf(str.substring(114, 116), 16));
            sxStatusDataUi.setAcWaveform(Integer.valueOf(str.substring(116, 118), 16));
            sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
            sxStatusDataUi.setBeforeAspiratedTime(BigDecimal.valueOf(Long.valueOf(str.substring(122, 126), 16)));
            sxStatusDataUi.setAfterStopGasTime(BigDecimal.valueOf(Long.valueOf(str.substring(126, 130), 16)));
            sxStatusDataUi.setRiseTime(BigDecimal.valueOf(Long.valueOf(str.substring(130, 134), 16)));
            sxStatusDataUi.setDeclineTime(BigDecimal.valueOf(Long.valueOf(str.substring(134, 138))));
            sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(138, 140)));
            sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(140, 142)));
            sxStatusDataUi.setWeldFlag(2);
            return sxStatusDataUi;
        }
        return null;
    }

    /**
     * 松下FR2系列通道参数查询（有参数）
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return SxChannelParamReplyHave
     */
    private SxChannelParamReplyHave sxChannelParamReplyHaveAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str) && "0211".equals(str.substring(40, 44))) {
            SxChannelParamReplyHave replyHave = new SxChannelParamReplyHave();
            replyHave.setWeldIp(clientIp);
            replyHave.setCommand(Integer.valueOf(str.substring(44, 46), 16));
            replyHave.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
            replyHave.setChannelFlag(Integer.valueOf(str.substring(50, 52), 16));
            replyHave.setPresetEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));
            replyHave.setPresetVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16)));
            replyHave.setPresetEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));
            replyHave.setPresetVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16)));
            replyHave.setInitialEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));
            replyHave.setInitialVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            replyHave.setInitialEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            replyHave.setInitialVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            replyHave.setArcEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(84, 88), 16)));
            replyHave.setArcVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(88, 92), 16)));
            replyHave.setArcEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(92, 96), 16)));
            replyHave.setArcVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(96, 100), 16)));
            replyHave.setTexture(Integer.valueOf(str.substring(100, 102), 16));
            replyHave.setWireDiameter(Integer.valueOf(str.substring(102, 104), 16));
            replyHave.setGases(Integer.valueOf(str.substring(104, 106), 16));
            replyHave.setWeldingControl(Integer.valueOf(str.substring(106, 108), 16));
            replyHave.setPulseHaveNot(Integer.valueOf(str.substring(108, 110), 16));
            replyHave.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(110, 114), 16)));
            replyHave.setUnitaryDifference(Integer.valueOf(str.substring(114, 116), 16));
            replyHave.setDryExtendLength(Integer.valueOf(str.substring(116, 118), 16));
            replyHave.setWeldMax(BigDecimal.valueOf(Long.valueOf(str.substring(124, 128), 16)));
            replyHave.setWeldMin(BigDecimal.valueOf(Long.valueOf(str.substring(128, 132), 16)));
            replyHave.setInitialMax(BigDecimal.valueOf(Long.valueOf(str.substring(132, 136), 16)));
            replyHave.setInitialMin(BigDecimal.valueOf(Long.valueOf(str.substring(136, 140), 16)));
            replyHave.setArcMax(BigDecimal.valueOf(Long.valueOf(str.substring(140, 144), 16)));
            replyHave.setArcMin(BigDecimal.valueOf(Long.valueOf(str.substring(144, 148), 16)));
            replyHave.setDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(148, 150), 16)));
            replyHave.setAmendPeriod(BigDecimal.valueOf(Long.valueOf(str.substring(150, 152), 16)));
            replyHave.setPresetEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(152, 156), 16)));
            replyHave.setPresetVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(156, 160), 16)));
            replyHave.setPresetEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(160, 164), 16)));
            replyHave.setPresetVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(164, 168), 16)));
            replyHave.setInitialEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(168, 172), 16)));
            replyHave.setInitialVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(172, 176), 16)));
            replyHave.setInitialEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(176, 180), 16)));
            replyHave.setInitialVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(180, 184), 16)));
            replyHave.setArcEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(184, 188), 16)));
            replyHave.setArcVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(188, 192), 16)));
            replyHave.setArcEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(192, 196), 16)));
            replyHave.setArcVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(196, 200), 16)));
            replyHave.setArcDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(200, 202), 16)));
            replyHave.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(202, 204), 16)));
            replyHave.setAlarmHaltTime(BigDecimal.valueOf(Long.valueOf(str.substring(204, 206), 16)));
            replyHave.setFlowMax(BigDecimal.valueOf(Long.valueOf(str.substring(208, 210), 16)));
            return replyHave;
        }
        return null;
    }

    /**
     * 松下FR2系列CO2焊机状态数据解析
     *
     * @param clientIp
     * @param str
     * @return
     */
    private SxStatusDataUI fr2Co2StatusUiAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str)) {
            SxStatusDataUI sxStatusDataUi = new SxStatusDataUI();
            String year = CommonUtils.hexToDecLengthJoint(str.substring(46, 48), 2);
            String month = CommonUtils.hexToDecLengthJoint(str.substring(48, 50), 2);
            String day = CommonUtils.hexToDecLengthJoint(str.substring(50, 52), 2);
            String hour = CommonUtils.hexToDecLengthJoint(str.substring(52, 54), 2);
            String minute = CommonUtils.hexToDecLengthJoint(str.substring(54, 56), 2);
            String second = CommonUtils.hexToDecLengthJoint(str.substring(56, 58), 2);
            String strDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            sxStatusDataUi.setWeldTime(LocalDateTime.parse(strDate, DateTimeUtils.YEAR_DATETIME).format(DateTimeUtils.DEFAULT_DATETIME));
            sxStatusDataUi.setWeldIp(clientIp);
            sxStatusDataUi.setFunctionFlag(Integer.valueOf(str.substring(66, 68), 16).toString());
            sxStatusDataUi.setWeldModel(CommonUtils.convertHexToString(str.substring(70, 88)));
            sxStatusDataUi.setWelderNo(str.substring(88, 98));
            sxStatusDataUi.setWorkpieceNoMin(str.substring(98, 108));
            sxStatusDataUi.setTexture(Integer.valueOf(str.substring(108, 110), 16));
            sxStatusDataUi.setWireDiameter(Integer.valueOf(str.substring(110, 112), 16));
            sxStatusDataUi.setGases(Integer.valueOf(str.substring(112, 114), 16));
            sxStatusDataUi.setWeldingControl(Integer.valueOf(str.substring(114, 116), 16));
            sxStatusDataUi.setPulseHaveNot(Integer.valueOf(str.substring(116, 118), 16));
            sxStatusDataUi.setSpotWeldingTime(BigDecimal.valueOf(Long.valueOf(str.substring(118, 122), 16)));
            sxStatusDataUi.setNowChannel(Integer.valueOf(str.substring(122, 124), 16));
            sxStatusDataUi.setMaxChannel(Integer.valueOf(str.substring(124, 126), 16));
            sxStatusDataUi.setUnitaryDifference(Integer.valueOf(str.substring(126, 128), 16));
            sxStatusDataUi.setDryExtendLength(Integer.valueOf(str.substring(128, 130), 16));
            sxStatusDataUi.setWorkpieceNoMax(str.substring(130, 136));
            sxStatusDataUi.setWeldFlag(1);
            return sxStatusDataUi;
        }
        return null;
    }

    /**
     * 松下AT3系列查询回复（有参数）
     *
     * @param clientIp 焊机IP
     * @param str      16进制字符串
     * @return At3ParamQueryReturn
     */
    private At3ParamQueryReturn at3ParamQueryReturnAnalysis(String clientIp, String str) {
        if (CommonUtils.isNotEmpty(str) && str.length() == 92) {
            At3ParamQueryReturn at3ParamQueryReturn = new At3ParamQueryReturn();
            at3ParamQueryReturn.setWeldIp(clientIp);
            at3ParamQueryReturn.setCommand(Integer.valueOf(str.substring(44, 46), 16));
            at3ParamQueryReturn.setChannel(Integer.valueOf(str.substring(46, 48), 16).toString());
            at3ParamQueryReturn.setChannelFlag(Integer.valueOf(str.substring(50, 52), 16));
            at3ParamQueryReturn.setPresetEleMax(BigDecimal.valueOf(Long.valueOf(str.substring(52, 56), 16)));
            at3ParamQueryReturn.setPresetVolMax(BigDecimal.valueOf(Long.valueOf(str.substring(56, 60), 16)));
            at3ParamQueryReturn.setPresetEleMin(BigDecimal.valueOf(Long.valueOf(str.substring(60, 64), 16)));
            at3ParamQueryReturn.setPresetVolMin(BigDecimal.valueOf(Long.valueOf(str.substring(64, 68), 16)));
            at3ParamQueryReturn.setEleAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(68, 72), 16)));
            at3ParamQueryReturn.setVolAlarmMax(BigDecimal.valueOf(Long.valueOf(str.substring(72, 76), 16)));
            at3ParamQueryReturn.setEleAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(76, 80), 16)));
            at3ParamQueryReturn.setVolAlarmMin(BigDecimal.valueOf(Long.valueOf(str.substring(80, 84), 16)));
            at3ParamQueryReturn.setAlarmDelayTime(BigDecimal.valueOf(Long.valueOf(str.substring(84, 86), 16)));
            at3ParamQueryReturn.setAlarmHaltTime(BigDecimal.valueOf(Long.valueOf(str.substring(86, 88), 16)));
            return at3ParamQueryReturn;
        }
        return null;
    }

}
