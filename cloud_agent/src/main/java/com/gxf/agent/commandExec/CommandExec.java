package com.gxf.agent.commandExec;


import com.gxf.agent.protocol.MachineProtocol;
import com.gxf.agent.util.IdempotentConfirmer;
import com.gxf.common.util.ListUtil;
import com.gxf.common.util.StringUtil;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by 58 on 2017/7/11.
 */
public class CommandExec {
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);


    /**
     * 创建配置文件
     * */
    public static boolean createConfigFile(String fileName, List<String> content, String machinePath){
        if(StringUtil.isEmpty(fileName) || StringUtil.isEmpty(machinePath) || ListUtil.isEmpty(content)){
            return false;
        }

        String configPath = machinePath + fileName;
        File configDir = new File(machinePath);
        if(!configDir.exists()){
            configDir.mkdirs();
        }

        Path path = Paths.get(configPath);
        try{
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, Charset.forName(MachineProtocol.ENCODING_UTF8));
            for(String line : content){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    /**
     * 端口号是否被占用
     * */
    public static boolean isPortUsed(int port){
        String psCmd = "/usr/sbin/lsof -i:%s";
        psCmd = String.format(psCmd, port);
        boolean isUsed = false;
        try{
            String psResponse = execute(psCmd);
            if(!StringUtil.isEmpty(psCmd)){
                String lines[] = psResponse.split(System.lineSeparator());
                for(String line : lines){
                    if(line.contains(String.valueOf(port))){
                        isUsed = true;
                        break;
                    } //if
                } //for
            } //if
        } catch (Exception e){
            e.printStackTrace();
        }

        return isUsed;
    }

    /**
     * 执行命令
     * */
    public static String execute(final String command) throws InterruptedException, ExecutionException, TimeoutException {
        if(StringUtil.isEmpty(command)){
            return "";
        } //if

        Process process = null;
        try{
            process = new ProcessBuilder(new String[]{"/bin/bash", "-c", command}).start();
        } catch (Exception e){
            e.printStackTrace();
        }

        final Process finalProcess = process;

        //输出执行命令的错误信息
        executorService.execute(new Runnable() {
            public void run() {
                printExecStdErr(command, finalProcess);
            }
        });

        Future<String> resultFutrue = executorService.submit(new Callable<String>(){

            public String call() throws Exception {
                StringBuilder stdoutContent = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
                String line = "";
                int lineNumber = 1;
                do{
                    line = bufferedReader.readLine();
                    System.out.println("line = " + line);
                    if(lineNumber++ > 1){
                        stdoutContent.append(System.lineSeparator());
                    }
                    stdoutContent.append(line);
                } while(line != null);
                if(null != bufferedReader){
                    try{
                        bufferedReader.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                return stdoutContent.toString();
            }
        }); //resultFuture

        String result = resultFutrue.get(3000, TimeUnit.MILLISECONDS);

        try{
            boolean success = process.waitFor(3000, TimeUnit.MILLISECONDS);
            if(!success){
                System.out.println("process.waitFor Failed.");
                return "";
            }

            return result;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(null != process){
                process.destroy();
            }
        }

        return result;
    }

    /**
     * 打印exec错误信息
     * */
    public static void printExecStdErr(String command, Process process){
        if(process == null){
            return;
        }
        StringBuilder errorMessage  = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        char[] buff = new char[1024];
        int readSize = 0;
        do{
            try {
                readSize = bufferedReader.read(buff);
                System.out.println("readSize = " + readSize);
                if(-1 != readSize){
                    errorMessage.append(new String(buff, 0, readSize));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } //catch
        } while(readSize != -1); //while

        if(!StringUtil.isEmpty(errorMessage.toString())){
            System.out.println(errorMessage.toString());
        }
    }

    /**
     * 判断端口是否有redis实例在运行
     * */
    public static boolean isRedisRun(int port, final String password, int type){
        final Jedis jedis = new Jedis("127.0.0.1", port);

        try{
            jedis.auth(password);
            return new IdempotentConfirmer(){
                @Override
                public boolean execute(){
                    String pong = jedis.ping();
                    return null != pong && pong.equals("PONG");
                }
            }.run();
        } finally {
            jedis.close();
            return false;
        }
    }


}
