package com.gxf.util;

import com.gxf.constant.CHARSET;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/16.
 */
public class FileUtil {

    /**
     * 向指定文件写入内容
     * */
    public static void writeFile(String filePath, List<String> lines){
        for(int i = 0; i < lines.size(); i++){
            saveFile(lines.get(i), filePath);
        }
    }

    /**
     * 读取文件内容，放到列表
     * */
    public static List<String> getFileContent(String filePath){
        List<String> fileContent = new ArrayList<String>();
        FileInputStream billFileInpuStream = null;
        BufferedReader billFileBufferedReader = null;

        try{
            billFileInpuStream = new FileInputStream(filePath);
            billFileBufferedReader  = new BufferedReader(new InputStreamReader(billFileInpuStream, CHARSET.UTF8));
            String line = billFileBufferedReader.readLine();
            while(null != line){
                fileContent.add(line);
                line = billFileBufferedReader.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                billFileInpuStream.close();
                billFileBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } //finally
        return fileContent;
    }

    /**
     * 保存文件内容
     * */
    public static void saveFile(String fileContent, String filePath){
        //获取账单内容保存到文件中
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;

        File billFile = new File(filePath);
        try{
            fileOutputStream = new FileOutputStream(billFile);
            fileChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(fileContent.getBytes().length);
            byteBuffer.put(fileContent.getBytes());
            byteBuffer.flip();
            fileChannel.write(byteBuffer);

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(null != fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != fileChannel){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
