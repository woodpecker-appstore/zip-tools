package me.gv7.woodpecker.plugin;

import java.util.ArrayList;
import java.util.List;

public class ZipTools implements IHelperPlugin{
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void HelperPluginMain(IHelperPluginCallbacks iHelperPluginCallbacks) {
        this.callbacks = iHelperPluginCallbacks;
        this.pluginHelper = callbacks.getPluginHelper();
        callbacks.setHelperPluginName("zip-tools");
        callbacks.setHelperPluginAutor("woodpecker-org");
        callbacks.setHelperPluginVersion("0.1.0");
        List<IHelper> helperPluginList = new ArrayList<IHelper>();
        helperPluginList.add(new ZipSlip());
        helperPluginList.add(new UnzipTest());
        callbacks.registerHelper(helperPluginList);
    }
}
