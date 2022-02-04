package me.gv7.woodpecker.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipTest implements IHelper{
    public String getHelperTabCaption() {
        return "unzip test";
    }

    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = ZipTools.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<IArg>();
        IArg args1 = ZipTools.pluginHelper.createArg();
        args1.setName("zip_file");
        args1.setDefaultValue("/tmp/test.zip");
        args1.setDescription("要解压的文件");
        args1.setRequired(true);
        args.add(args1);


        IArg uncompressPath = ZipTools.pluginHelper.createArg();
        uncompressPath.setName("uncompress_path");
        uncompressPath.setDefaultValue("/tmp/");
        uncompressPath.setDescription("解压结果保存路径");
        uncompressPath.setRequired(true);
        args.add(uncompressPath);

        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    public void doHelp(Map<String, Object> customArgs, IResultOutput resultOutput) throws Throwable {
        String zipFile = (String)customArgs.get("zip_file");
        String uncompressPath = (String)customArgs.get("uncompress_path");
        resultOutput.infoPrintln(String.format("Uncompress %s ......",zipFile));
        unzip(zipFile,uncompressPath);
        resultOutput.successPrintln(String.format("Uncompress to %s finsh!",uncompressPath));
    }

    public static void unzip(String uncompressFile,String savePath) throws Exception{
        ZipFile zipFile = new ZipFile(uncompressFile);
        Enumeration enumeration = zipFile.entries();
        byte[] buffer = new byte[10240];

        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();

            // 针对空文件夹的识别
            // Ref: https://blog.csdn.net/qq_42606750/article/details/82830584
            boolean isDirectory = false;
            if ((zipEntry.getName().endsWith("/") || zipEntry.getName().endsWith("\\")) && zipEntry.getSize() == 0) {
                isDirectory = true;
            }
            if (zipEntry.isDirectory() || isDirectory) {
                File dirFile = new File(savePath + File.separator + zipEntry.getName());
                dirFile.mkdirs();
            } else {
                File file = new File(savePath + File.separator + zipEntry.getName());
                File dirFile = file.getParentFile();
                dirFile.mkdirs();
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                int readLen;
                while ((readLen = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readLen);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        }
        zipFile.close();
    }
}
