package io.github.iweidujiang.modbusexample.resolver;

import io.github.iweidujiang.modbusexample.enums.AddressFormat;
import io.github.iweidujiang.modbusexample.enums.ModbusDataType;

import java.util.HashMap;
import java.util.Map;

/**
 * ┌───────────────────────────────────────────────
 * │ 📦 Modbus 地址解析器，支持 5位、6位、自定义偏移格式
 * │
 * │ 👤 作者：苏渡苇
 * │ 🔗 公众号：苏渡苇
 * │ 💻 GitHub：https://github.com/iweidujiang
 * │ 📅 @date 2026/1/20
 * └───────────────────────────────────────────────
 */
public class ModbusAddressResolver {
    // 默认偏移配置（按标准）
    private static final Map<ModbusDataType, Integer> DEFAULT_OFFSETS_5D = new HashMap<>();
    private static final Map<ModbusDataType, Integer> DEFAULT_OFFSETS_6D = new HashMap<>();

    static {
        // 5位格式偏移
        DEFAULT_OFFSETS_5D.put(ModbusDataType.COIL, 1);
        DEFAULT_OFFSETS_5D.put(ModbusDataType.DISCRETE_INPUT, 10001);
        DEFAULT_OFFSETS_5D.put(ModbusDataType.INPUT_REGISTER, 30001);
        DEFAULT_OFFSETS_5D.put(ModbusDataType.HOLDING_REGISTER, 40001);

        // 6位格式偏移
        DEFAULT_OFFSETS_6D.put(ModbusDataType.COIL, 1);
        DEFAULT_OFFSETS_6D.put(ModbusDataType.DISCRETE_INPUT, 100001);
        DEFAULT_OFFSETS_6D.put(ModbusDataType.INPUT_REGISTER, 300001);
        DEFAULT_OFFSETS_6D.put(ModbusDataType.HOLDING_REGISTER, 400001);
    }

    private final AddressFormat format;
    private final Map<ModbusDataType, Integer> customOffsets;

    /**
     * 构造标准格式解析器
     */
    public ModbusAddressResolver(AddressFormat format) {
        this.format = format;
        this.customOffsets = null;
    }

    /**
     * 构造自定义偏移解析器
     */
    public ModbusAddressResolver(Map<ModbusDataType, Integer> customOffsets) {
        if (customOffsets == null || customOffsets.isEmpty()) {
            throw new IllegalArgumentException("自定义偏移表不能为空");
        }
        this.format = AddressFormat.CUSTOM;
        this.customOffsets = new HashMap<>(customOffsets); // 防御性拷贝
    }

    /**
     * 将用户地址转换为协议地址
     */
    public int toProtocolAddress(int userAddress, ModbusDataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException("数据类型不能为 null");
        }

        int offset;
        if (format == AddressFormat.CUSTOM) {
            // customOffsets 不应为 null（构造时保证）
            Integer customOffset = customOffsets.get(dataType);
            if (customOffset == null) {
                throw new IllegalArgumentException("自定义偏移未配置数据类型: " + dataType);
            }
            offset = customOffset;
        } else {
            // 使用标准偏移表
            Map<ModbusDataType, Integer> offsets =
                    (format == AddressFormat.FIVE_DIGIT) ? DEFAULT_OFFSETS_5D : DEFAULT_OFFSETS_6D;

            Integer standardOffset = offsets.get(dataType);
            if (standardOffset == null) {
                throw new IllegalArgumentException("不支持的数据类型: " + dataType);
            }
            offset = standardOffset;
        }

        // 可选：校验结果非负（防止用户地址小于偏移量）
        if (userAddress < offset) {
            throw new IllegalArgumentException(
                    String.format("用户地址 %d 小于偏移量 %d，计算结果为负", userAddress, offset)
            );
        }

        return userAddress - offset;
    }

    /**
     * 获取读操作对应的功能码
     */
    public int getReadFunctionCode(ModbusDataType dataType) {
        switch (dataType) {
            case COIL: return 1;
            case DISCRETE_INPUT: return 2;
            case INPUT_REGISTER: return 4;
            case HOLDING_REGISTER: return 3;
            default: throw new IllegalArgumentException("未知数据类型: " + dataType);
        }
    }
}
