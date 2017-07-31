package com.gxf.controller;


import com.gxf.agent.commandExec.CommandExec;
import com.gxf.agent.constants.BaseConstant;
import com.gxf.common.util.StringUtil;
import com.gxf.entity.MachineStats;
import com.gxf.udp.proto.MachineStat_Pb;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gxf.util.EmptyObjectConstant.EMPTY_STRING;

/**
 * Created by 58 on 2017/7/29.
 * 收集机器运行时信息，CPU负载，内存等
 * 执行top得到的机器运行时信息
 */
public class MachineDataCollectController {
    private final static String COMMAND_TOP = "top -b -n 1 | head -5";
    private final static String COMMAND_DF_LH = "df -lh";
    private final static String LOAD_AVERAGE_STRING = "load average: ";
    private final static String MEM_USAGE_STRING = "KiB Mem :";
    private final static String BUFFER_CACHE = "buff/cache";
    private final static String SWAP_USAGE_STRING = "KiB Swap:  ";
    private static Logger logger = LoggerFactory.getLogger(MachineDataCollectController.class);

    public static void main(String[] args) {
        String content = "1001332 total";
        String result = matchMemLineNumber(content);
        System.out.println("result = {}" + result);
    }

    /**
     * 通过执行top命令获取机器信息
     * TODO:待完成
     * */
    public static byte[] getMachineInfo(){
        MachineStats systemPerfomanceEntity = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try{
            systemPerfomanceEntity = new MachineStats();
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", COMMAND_TOP});
            //stderr
            CommandExec.printExecStdErr(COMMAND_TOP, process);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            int lineNum = 0;
            String totalMem = EMPTY_STRING;
            String freeMem = EMPTY_STRING;
            String buffersMem = EMPTY_STRING;
            String cachedMem = EMPTY_STRING;
            while((line = bufferedReader.readLine()) != null){
                logger.info("execute top line:{}", line);
                if(StringUtil.isEmpty(line)){
                    continue;
                }
                lineNum ++;
                if(5 < lineNum){
                    return machineStatsToPb(systemPerfomanceEntity);
                }
                if(1 == lineNum){
                    //top命令第一行
                    //top - 00:20:30 up 12:17,  3 users,  load average: 0.39, 0.16, 0.09
                    int loadAverageIndex = line.indexOf(LOAD_AVERAGE_STRING);
                    String loadAverages = line.substring(loadAverageIndex).replace(LOAD_AVERAGE_STRING, EMPTY_STRING);
                    String[] loadAverageArray = loadAverages.split(",");
                    if(3 != loadAverageArray.length){
                        continue;
                    }
                    systemPerfomanceEntity.setLoad(com.gxf.util.StringUtil.trimToEmpty(loadAverageArray[0]));
                 }else if(3 == lineNum){
                    //第三行通常是
                    //%Cpu(s):  0.6 us,  0.6 sy,  0.0 ni, 98.6 id,  0.1 wa,  0.0 hi,  0.0 si,  0.0 st
                    double cpuUsUsage = getUsageCpu(line);
                    systemPerfomanceEntity.setCpuUsage(String.valueOf(cpuUsUsage));
                }else if(4 == lineNum){
                     //第四行通常是
                    //KiB Mem :  1001332 total,    71096 free,   705880 used,   224356 buff/cache
                    //注意:centos7中是上面这样的
                    //centos 6是这样的:
                    //Mem:    262144k total,    34264k used,   227880k free,        0k buffers
                    //这里虚拟机用的7
                    String[] memArray = line.replace(MEM_USAGE_STRING, EMPTY_STRING).split(BaseConstant.COMMA);
                    totalMem = matchMemLineNumber(memArray[0].trim());
                    //这里也是centos7 | 6或其他版本不同
                    if(line.contains(BUFFER_CACHE)){
                        freeMem = matchMemLineNumber(memArray[1]);
                    }else{
                        freeMem = matchMemLineNumber(memArray[2]);
                    }
                    buffersMem = matchMemLineNumber(memArray[3]);
                }else if(5 == lineNum){
                    //第五行通常是这样：
                    //KiB Swap:  2097148 total,  1904092 free,   193056 used.   103956 avail Mem
                    String[] memArray = line.replace(SWAP_USAGE_STRING, EMPTY_STRING).split(BaseConstant.COMMA);
                    if (memArray.length > 3){
                        cachedMem =  matchMemLineNumber(memArray[3].trim());
                    }else{
                        cachedMem = "0";
                    }
                    if(com.gxf.util.StringUtil.isBlank(totalMem, freeMem, buffersMem)){
                        logger.error("can not get totalMem, freeMem, buffersMem or cachedMem when" +
                                "execute top command");
                        throw new Exception("can not get totalMem, freeMem, buffersMem or cachedMem when" +
                                "execute top command");
                    }
                    Long totalMemLong = NumberUtils.toLong(totalMem);
                    Long freeMemLong = NumberUtils.toLong(freeMem);
                    Long buffersMemLong = NumberUtils.toLong(buffersMem);
                    Long cachedMemLong = NumberUtils.toLong(cachedMem);

                    Long usedMemFree = freeMemLong + buffersMemLong + cachedMemLong;
                    Double memoryUsage = 1 - (NumberUtils.toDouble(usedMemFree.toString()) /
                                            NumberUtils.toDouble(totalMemLong.toString()) / 1.0);
                    systemPerfomanceEntity.setMemoryTotal(String.valueOf(totalMemLong));
                    systemPerfomanceEntity.setMemoryFree(String.valueOf(usedMemFree));
                    DecimalFormat df = new DecimalFormat("0.0");
                    systemPerfomanceEntity.setMemoryUsageRatio(df.format(memoryUsage * 100));
                }else{
                    continue;
                }
            } //parse the top output
            process.destroy();

            //统计磁盘使用情况
            Map<String/**使用百分率*/, String/**挂载点*/> diskUsageMap = new HashMap<String, String>();
            process = Runtime.getRuntime().exec(COMMAND_DF_LH);
            //stderr
            CommandExec.printExecStdErr(COMMAND_DF_LH, process);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            /**
             * 执行df -lh命令 内容通常是这样的
                 Filesystem               Size  Used Avail Use% Mounted on
                 /dev/mapper/centos-root   18G   12G  6.3G  64% /
                 devtmpfs                 474M     0  474M   0% /dev
                 tmpfs                    489M  156K  489M   1% /dev/shm
                 tmpfs                    489M   14M  476M   3% /run
                 tmpfs                    489M     0  489M   0% /sys/fs/cgroup
                 /dev/sda1                497M  157M  340M  32% /boot
                 tmpfs                     98M   20K   98M   1% /run/user/1000
                 /dev/sr0                 4.1G  4.1G     0 100% /run/media/guanxianseng/CentOS 7 x86_64
             * */
            boolean isFirstLine = true;
            while((line = bufferedReader.readLine()) != null){
                logger.info("execute df -lh line:{}", line);
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                if(StringUtil.isEmpty(line)){
                    continue;
                }
                line = line.replaceAll("{1,}", BaseConstant.WORD_SEPARATOR);
                String[] lineArray = line.split(BaseConstant.WORD_SEPARATOR);
                if (6 != lineArray.length){
                    continue;
                }
                String diskUsage = lineArray[4];
                String mountedOn = lineArray[5];
                diskUsageMap.put(mountedOn, diskUsage);
            } //while
            systemPerfomanceEntity.setDiskUsageMap(diskUsageMap);
            //使用star统计当前网络流量 TODO
            Double traffic = 0.0;
            systemPerfomanceEntity.setTraffic(traffic.toString());
        } catch (Exception e){
            logger.error("getMachineInfo failed");
            logger.error(e.getMessage(), e);
        } finally {
            try{
                if(null != bufferedReader){
                    bufferedReader.close();
                }
            } catch (Exception e){
                logger.error(e.getMessage(), e);
            }
            if (null != process){
                process.destroy();
            }
        }

        return machineStatsToPb(systemPerfomanceEntity);
    } //getMachineInfo

    /**
     * 匹配字符串中的数字
     * */
    private static String matchMemLineNumber(String content){
        logger.info("matchMemLineNumber content:{}", content);
        String result = EMPTY_STRING;
        if(null == content || EMPTY_STRING.equals(content.trim())){
            return result;
        }
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            result = matcher.group(1);
        }
        return result;
    }

    /**
     * 从top的cpuLine解析出us
     * %Cpu(s):  0.6 us,  0.6 sy,  0.0 ni, 98.6 id,  0.1 wa,  0.0 hi,  0.0 si,  0.0 st
     * */
    public static double getUsageCpu(String cpuLine){
        if(cpuLine == null || EMPTY_STRING.equalsIgnoreCase(cpuLine.trim())){
            return 0;
        }
        String[] items = cpuLine.split(BaseConstant.COMMA);
        if(items.length < 1){
            return 0;
        }
        String usCpuStr = items[0];
        return NumberUtils.toDouble(matchCpuLine(usCpuStr));
    }

    /**
     * %Cpu(s):  0.6 us
     * 匹配出0.6出来
     * */
    private static String matchCpuLine(String content){
        String result = EMPTY_STRING;
        if(content == null || EMPTY_STRING.equals(content.trim())){
            return result;
        }
        Pattern pattern = Pattern.compile("(\\d+).(\\d+)");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            result = matcher.group();
        }
        return result;
    }


    /**
     * 执行top命令收集到的机器信息
     * 转换成pb，序列化成字节数组
     * */
    private static byte[] machineStatsToPb(MachineStats machineStats){
        MachineStat_Pb.MachineStat.Builder builder = MachineStat_Pb.MachineStat.newBuilder();
        builder.setId(machineStats.getId());
        builder.setCpuUsage(machineStats.getCpuUsage());
        builder.setLoad(machineStats.getLoad());
        builder.setTraffic(machineStats.getTraffic());
        builder.setMemoryUsageRatio(machineStats.getMemoryUsageRatio());
        builder.setMemoryFree(machineStats.getMemoryFree());
        builder.setMemoryTotal(machineStats.getMemoryTotal());
        builder.setMemoryAllocated(machineStats.getMemoryAllocated());

        MachineStat_Pb.DiskUsageMapFileEntry.Builder disUsageMapFileEntryBuilder;
        for(Map.Entry<String, String> entry : machineStats.getDiskUsageMap().entrySet()){
            disUsageMapFileEntryBuilder = MachineStat_Pb.DiskUsageMapFileEntry.newBuilder();
            disUsageMapFileEntryBuilder.setKey(entry.getKey());
            disUsageMapFileEntryBuilder.setValue(entry.getValue());
            builder.addDisUsageMap(disUsageMapFileEntryBuilder);
        }

        return builder.build().toByteArray();
    }
}
