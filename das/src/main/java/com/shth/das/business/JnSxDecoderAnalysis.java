package com.shth.das.business;

import com.shth.das.common.*;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.jnsx.*;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: 江南松下协议解析
 * @author: Shan Long
 * @create: 2021-08-09
 */
public class JnSxDecoderAnalysis extends BaseAbstractDecoder {

    /**
     * 保存字符串长度和方法映射关系
     * Function<K,V>,k:传入参数，V：返回参数
     */
    private final Map<Integer, Function<JnSxDecoderParam, HandlerParam>> decoderMapping = new HashMap<>();
    private final JnSxRtDataProtocol jnSxRtDataProtocol = new JnSxRtDataProtocol();

    public JnSxDecoderAnalysis() {
        setDecoderMapping();
    }

    private void setDecoderMapping() {
        //松下第一次握手验证
        this.decoderMapping.put(42, this::jnSxFirstVerify);
        //松下第二次握手验证
        this.decoderMapping.put(128, this::jnSxSecondVerify);
        //松下焊机GL5软硬件参数
        this.decoderMapping.put(180, this::jnSxSoftHardParam);
        //松下焊机GL5实时数据
        this.decoderMapping.put(206, this::jnSxGl5RtDataAnalysis);
        //松下焊机GL5系列CO2状态信息
        this.decoderMapping.put(246, this::jnSxGl5StatusAnalysis);
        //松下GL5系列工艺信息和焊接通道设定
        this.decoderMapping.put(106, this::jnSxGl5ProcessWeldSet);
        //松下GL5系列CO2工艺索取返回
        this.decoderMapping.put(406, this::jnSxCo2ProcessClaimReturn);
        //松下GL5系列TIG工艺索取返回
        this.decoderMapping.put(446, this::jnSxTigProcessClaimReturn);
        //松下FR2系列CO2焊机实时数据解析
        this.decoderMapping.put(126, this::jnSxFr2Co2RtDataDbAnalysis);
        //松下FR2系列TIG实时数据
        this.decoderMapping.put(118, this::jnSxFr2TigRtDataDbAnalysis);
        //松下FR2系列CO2和TIG的状态信息
        this.decoderMapping.put(156, this::jnSxFr2StatusUiAnalysis);
        //松下FR2系列通道参数查询（无参数）、下载、删除回复
        this.decoderMapping.put(52, this::jnSxChannelParamReplyAnalysis);
        //松下FR2系列通道参数查询（有参数）
        this.decoderMapping.put(218, this::jnSxChannelParamReplyHave);
        //松下AT3系列查询回复（有参数）
        this.decoderMapping.put(92, this::jnSxAt3ParamQueryReturn);
    }

    @Override
    public HandlerParam baseProtocolAnalysis(ChannelHandlerContext ctx, String str) {
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        JnSxDecoderParam jnSxDecoderParam = new JnSxDecoderParam();
        jnSxDecoderParam.setStr(str);
        jnSxDecoderParam.setClientIp(clientIp);
        jnSxDecoderParam.setCtx(ctx);
        if (this.decoderMapping.containsKey(str.length())) {
            return this.decoderMapping.get(str.length()).apply(jnSxDecoderParam);
        }
        return new HandlerParam();
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
                final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                if (str.length() == 42 && "4E455430".equals(str.substring(0, 8))) {
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_FIRST_VERIFICATION).sync();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                //松下焊机GL5/FR2/AT第二次验证（下行）
                if (str.length() == 128 && "4C4A5348".equals(str.substring(0, 8))) {
                    //设备CID（8个字节：字符长度则为：16）
                    String weldCid = str.substring(12, 28);
                    //保存设备CID和通道对应关系
                    CommonMap.SX_WELD_CID_CTX_MAP.put(weldCid, ctx);
                    CommonMap.SX_CTX_WELD_CID_MAP.put(ctx, weldCid);
                    ctx.channel().writeAndFlush(SxVerificationCode.SX_SECOND_VERIFICATION).sync();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
    private HandlerParam jnSxSoftHardParam(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            try {
                final String str = jnSxDecoderParam.getStr();
                final ChannelHandlerContext ctx = jnSxDecoderParam.getCtx();
                final String clientIp = jnSxDecoderParam.getClientIp();
                //松下焊机GL5软硬件参数
                if (str.length() == 180 && "FE5AA5005A".equals(str.substring(0, 10))) {
                    Map<String, Object> map = new HashMap<>();
                    HandlerParam handlerParam = new HandlerParam();
                    SxWeldModel sxWeldModel = this.jnSxRtDataProtocol.sxWeldAnalysis(clientIp, str);
                    if (null != sxWeldModel) {
                        map.put("SxWeldModel", sxWeldModel);
                        ctx.channel().writeAndFlush(SxVerificationCode.SX_SOFT_HARDWARE_PARAM_DOWN).sync();
                    }
                    handlerParam.setKey(jnSxDecoderParam.getStr().length());
                    handlerParam.setValue(map);
                    return handlerParam;
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下焊机GL5实时数据
            if (str.length() == 206 && "FE5AA50067".equals(str.substring(0, 10))) {
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataUi sxRtDataUi = this.jnSxRtDataProtocol.sxRtDataUiAnalysis(clientIp, str);
                SxRtDataDb sxRtDataDb = this.jnSxRtDataProtocol.sxRtDataDbAnalysis(clientIp, str);
                if (null != sxRtDataUi) {
                    map.put("SxRtDataUI", sxRtDataUi);
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
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下焊机GL5系列CO2状态信息
            if (str.length() == 246 && "FE5AA5007B".equals(str.substring(0, 10))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                SxStatusDataUI sxStatusDataUi = this.jnSxRtDataProtocol.sxStatusDataUiAnalysis(clientIp, str);
                if (null != sxStatusDataUi) {
                    map.put("SxStatusDataUI", sxStatusDataUi);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

    /**
     * 松下GL5系列工艺信息和焊接通道设定
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
                //松下GL5工艺下发回复/索取返回
                if ("1201".equals(str.substring(40, 44))) {
                    //读写标志：0：主动上传；1：读取；2：设置；3：删除
                    if ("1".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺索取返回（无数据）
                        SxProcessClaimReturn sxProcessClaimReturn = this.jnSxRtDataProtocol.sxProcessClaimReturnAnalysis(clientIp, str);
                        if (null != sxProcessClaimReturn) {
                            map.put("SxProcessClaimReturn", sxProcessClaimReturn);
                        }
                    }
                    if ("2".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50035".equals(str.substring(0, 10))) {
                        //松下工艺下发回复
                        SxProcessReturn sxProcessReturn = this.jnSxRtDataProtocol.sxProcessReturnAnalysis(clientIp, str);
                        if (null != sxProcessReturn) {
                            map.put("SxProcessReturn", sxProcessReturn);
                        }
                    }
                    if ("3".equals(Integer.valueOf(str.substring(70, 72), 16).toString()) && "FE5AA50028".equals(str.substring(0, 10))) {
                        //松下工艺删除回复
                        SxProcessDeleteReturn sxProcessDeleteReturn = this.jnSxRtDataProtocol.sxProcessDeleteReturnAnalysis(clientIp, str);
                        if (null != sxProcessDeleteReturn) {
                            map.put("SxProcessDeleteReturn", sxProcessDeleteReturn);
                        }
                    }
                }
                //松下GL5焊接规范通道设定回复/读取回复
                if ("1202".equals(str.substring(40, 44)) && "FE5AA50035".equals(str.substring(0, 10))) {
                    SxWeldChannelSetReturn weldChannelSetReturn = this.jnSxRtDataProtocol.sxWeldChannelSetReturnAnalysis(clientIp, str);
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
    private HandlerParam jnSxCo2ProcessClaimReturn(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下GL5系列CO2工艺索取返回
            if (str.length() == 406 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                SxCO2ProcessClaimReturn claimReturn = this.jnSxRtDataProtocol.sxCO2ProcessClaimReturnAnalysis(clientIp, str);
                if (null != claimReturn) {
                    map.put("SxCO2ProcessClaimReturn", claimReturn);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
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
    private HandlerParam jnSxTigProcessClaimReturn(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下GL5系列TIG工艺索取返回
            if (str.length() == 446 && "FE5AA500CB".equals(str.substring(0, 10)) && "1201".equals(str.substring(40, 44))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                SxTIGProcessClaimReturn sxTigProcessClaimReturn = this.jnSxRtDataProtocol.sxTIGProcessClaimReturnAnalysis(clientIp, str);
                if (null != sxTigProcessClaimReturn) {
                    map.put("SxTIGProcessClaimReturn", sxTigProcessClaimReturn);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
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
            if (str.length() == 126 && "FE5AA5003F".equals(str.substring(0, 10))) {
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxRtDataDb sxRtDataDb = this.jnSxRtDataProtocol.fr2Co2RtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = this.jnSxRtDataProtocol.fr2Co2RtDataUiAnalysis(clientIp, str);
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
                SxRtDataDb sxRtDataDb = this.jnSxRtDataProtocol.fr2TigRtDataDbAnalysis(clientIp, str);
                SxRtDataUi sxRtDataUi = this.jnSxRtDataProtocol.fr2TigRtDataUiAnalysis(clientIp, str);
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
                    SxStatusDataUI sxStatusDataUi = this.jnSxRtDataProtocol.fr2Co2StatusUiAnalysis(clientIp, str);
                    if (null != sxStatusDataUi) {
                        map.put("SxStatusDataUI", sxStatusDataUi);
                    }
                }
                //TIG焊机
                if ("0102".equals(str.substring(40, 44)) && 4 == Integer.valueOf(str.substring(68, 70), 16)) {
                    SxStatusDataUI sxStatusDataUi = this.jnSxRtDataProtocol.fr2TigStatusUiAnalysis(clientIp, str);
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
     * 松下FR2系列通道参数查询（无参数）、下载、删除回复
     *
     * @param jnSxDecoderParam 入参
     * @return 返回
     */
    private HandlerParam jnSxChannelParamReplyAnalysis(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列通道参数查询（无参数）、下载、删除回复
            if (str.length() == 52 && "FE5AA5001A".equals(str.substring(0, 10))) {
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                SxChannelParamReply sxChannelParamReply = this.jnSxRtDataProtocol.sxChannelParamReplyAnalysis(clientIp, str);
                if (null != sxChannelParamReply) {
                    map.put("SxChannelParamReply", sxChannelParamReply);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
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
    private HandlerParam jnSxChannelParamReplyHave(JnSxDecoderParam jnSxDecoderParam) {
        if (null != jnSxDecoderParam) {
            final String str = jnSxDecoderParam.getStr();
            final String clientIp = jnSxDecoderParam.getClientIp();
            //松下FR2系列通道参数查询（有参数）
            if (str.length() == 218 && "FE5AA5006E".equals(str.substring(0, 10))) {
                Map<String, Object> map = new HashMap<>();
                HandlerParam handlerParam = new HandlerParam();
                SxChannelParamReplyHave sxChannelParamReplyHave = this.jnSxRtDataProtocol.sxChannelParamReplyHaveAnalysis(clientIp, str);
                if (null != sxChannelParamReplyHave) {
                    map.put("SxChannelParamReplyHave", sxChannelParamReplyHave);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
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
                HandlerParam handlerParam = new HandlerParam();
                Map<String, Object> map = new HashMap<>();
                At3ParamQueryReturn at3ParamQueryReturn = this.jnSxRtDataProtocol.at3ParamQueryReturnAnalysis(clientIp, str);
                if (null != at3ParamQueryReturn) {
                    map.put("At3ParamQueryReturn", at3ParamQueryReturn);
                }
                handlerParam.setKey(jnSxDecoderParam.getStr().length());
                handlerParam.setValue(map);
                return handlerParam;
            }
        }
        return null;
    }

}
