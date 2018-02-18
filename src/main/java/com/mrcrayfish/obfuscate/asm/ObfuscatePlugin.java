package com.mrcrayfish.obfuscate.asm;

import com.mrcrayfish.obfuscate.Obfuscate;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
@IFMLLoadingPlugin.TransformerExclusions({"com.mrcrayfish.obfuscate.asm"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
public class ObfuscatePlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"com.mrcrayfish.obfuscate.asm.ObfuscateTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return Obfuscate.class.getName();
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
