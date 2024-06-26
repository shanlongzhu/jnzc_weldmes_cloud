package com.shth.das.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * CRC7校验
 */
@Slf4j
public class CRC7Check {

    private CRC7Check() {
    }

    private static final int[] CRC7_TABLE = {0x00, 0x12, 0x24, 0x36, 0x48, 0x5a, 0x6c, 0x7e,
            0x19, 0x0b, 0x3d, 0x2f, 0x51, 0x43, 0x75, 0x67,
            0x32, 0x20, 0x16, 0x04, 0x7a, 0x68, 0x5e, 0x4c,
            0x2b, 0x39, 0x0f, 0x1d, 0x63, 0x71, 0x47, 0x55,
            0x64, 0x76, 0x40, 0x52, 0x2c, 0x3e, 0x08, 0x1a,
            0x7d, 0x6f, 0x59, 0x4b, 0x35, 0x27, 0x11, 0x03,
            0x56, 0x44, 0x72, 0x60, 0x1e, 0x0c, 0x3a, 0x28,
            0x4f, 0x5d, 0x6b, 0x79, 0x07, 0x15, 0x23, 0x31,
            0x41, 0x53, 0x65, 0x77, 0x09, 0x1b, 0x2d, 0x3f,
            0x58, 0x4a, 0x7c, 0x6e, 0x10, 0x02, 0x34, 0x26,
            0x73, 0x61, 0x57, 0x45, 0x3b, 0x29, 0x1f, 0x0d,
            0x6a, 0x78, 0x4e, 0x5c, 0x22, 0x30, 0x06, 0x14,
            0x25, 0x37, 0x01, 0x13, 0x6d, 0x7f, 0x49, 0x5b,
            0x3c, 0x2e, 0x18, 0x0a, 0x74, 0x66, 0x50, 0x42,
            0x17, 0x05, 0x33, 0x21, 0x5f, 0x4d, 0x7b, 0x69,
            0x0e, 0x1c, 0x2a, 0x38, 0x46, 0x54, 0x62, 0x70};

    /**
     * CRC7校验
     *
     * @param str 16进制字符串
     * @return 校验码
     */
    public static String crc7Check(String str) {
        int result = 0;
        if (StringUtils.isNotBlank(str)) {
            for (int i = 0; i < str.length(); i = i + 2) {
                result = CRC7_TABLE[(Integer.parseInt(str.substring(i, i + 2), 16) >> 1) ^ result];
                result ^= ((Integer.parseInt(str.substring(i, i + 2), 16) & 0x01) == 0 ? 0x00 : 0x09);
            }
        }
        return Integer.toHexString((result << 1) + 0x01).toUpperCase();
    }

    /**
     * 对19位的校验码进行CRC7校验后替换（松下CRC7校验）
     *
     * @param str 16进制字符串
     * @return 校验后的16进制字符串
     */
    public static String crc7CheckAndReplace(String str) {
        try {
            if (StringUtils.isNotBlank(str) && str.length() >= 40) {
                StringBuilder builder = new StringBuilder(str);
                //替换第19字节得CRC7校验码位数
                builder.replace(38, 40, "00");
                //对所有字段进行CRC7校验，得到校验码
                String crc7Code = crc7Check(builder.toString());
                //长度拼接
                String crc7CodeStr = CommonUtils.stringLengthJoint(crc7Code, 2);
                //替换第19字节的校验码为计算后的校验码（校验码替换）
                builder.replace(38, 40, crc7CodeStr);
                return builder.toString();
            }
        } catch (Exception e) {
            log.error("[CRC7校验异常！]:{}", e.getMessage());
        }
        return null;
    }

}
