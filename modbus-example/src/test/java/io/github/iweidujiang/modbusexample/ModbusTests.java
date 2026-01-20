package io.github.iweidujiang.modbusexample;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.*;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

/**
 * ┌───────────────────────────────────────────────
 * │ 📦 Modbus协议测试
 * │
 * │ 👤 作者：苏渡苇
 * │ 🔗 公众号：苏渡苇
 * │ 💻 GitHub：https://github.com/iweidujiang
 * │ 📅 @date 2026/1/19
 * └───────────────────────────────────────────────
 */
@Slf4j
public class ModbusTests {

    @Test
    void test_read() {
        try {
            // 1. 创建 TCP 连接
            TCPMasterConnection conn = new TCPMasterConnection(InetAddress.getByName("127.0.0.1"));
            conn.setPort(502);
            conn.connect();

            // 2. 构造读请求：地址2，读1个寄存器
            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(1, 1);
            req.setUnitID(1); // 从站ID=1

            // 3. 发送请求
            ModbusTCPTransaction trans = new ModbusTCPTransaction(conn);
            trans.setRequest(req);
            trans.execute();

            // 4. 解析响应
            ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();
            int rawValue = res.getRegisterValue(0); // 假设返回 256

            // 5. 转换为实际温度（设备手册说明：值×0.1）
            double temperature = rawValue / 10.0;
            System.out.println("当前温度: " + temperature + " ℃");

            Thread.sleep(5000);
            conn.close();
        } catch (Exception e) {
            System.err.println("读取失败: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    @Test
    void test_write() {
        try {
            // 1. 创建连接
            TCPMasterConnection conn = new TCPMasterConnection(InetAddress.getByName("127.0.0.1"));
            conn.setPort(502);
            conn.connect();

            // 写入：地址1（即40002），值=300
            WriteSingleRegisterRequest req = new WriteSingleRegisterRequest(
                    1,                    // 寄存器地址
                    new SimpleRegister(300) // 值
            );
            req.setUnitID(1);

            ModbusTCPTransaction trans = new ModbusTCPTransaction(conn);
            trans.setRequest(req);
            trans.execute();

            WriteSingleRegisterResponse res = (WriteSingleRegisterResponse) trans.getResponse();
            System.out.println("res: " + res);
            System.out.println("目标温度已设为 30.0 ℃");

            conn.close();
        } catch (Exception e) {
            log.error("写入失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 读 线圈 状态
     */
    @Test
    void test_readCoils() {
        try {
            TCPMasterConnection conn = new TCPMasterConnection(InetAddress.getByName("127.0.0.1"));
            conn.setPort(502);
            conn.connect();


            // 正确！使用 ReadCoilsRequest
            ReadCoilsRequest req = new ReadCoilsRequest(0, 1); // 00001 → 0
            req.setUnitID(1); // 从站ID=1

            ModbusTCPTransaction trans = new ModbusTCPTransaction(conn);
            trans.setRequest(req);
            trans.execute();

            ReadCoilsResponse res = (ReadCoilsResponse) trans.getResponse();
            boolean isRunning = res.getCoilStatus(0);

            System.out.println("isRunning: " + isRunning);

            Thread.sleep(5000);
            conn.close();
        } catch (Exception e) {
            System.err.println("读取失败: " + e.getMessage());
            log.error(e.getMessage());
        }
    }
}
