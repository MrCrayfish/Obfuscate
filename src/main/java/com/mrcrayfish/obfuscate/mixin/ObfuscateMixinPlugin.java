package com.mrcrayfish.obfuscate.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ObfuscateMixinPlugin implements IMixinConfigPlugin 
{
    private boolean optifineLoaded;

    @Override
    public void onLoad(String mixinPackage) 
    {
        try 
        {
            Class.forName("optifine.Installer");
            this.optifineLoaded = true;
        }
        catch (ClassNotFoundException e)
        {
            this.optifineLoaded = false;
        }
    }

    @Override
    public String getRefMapperConfig() 
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) 
    {
        return this.optifineLoaded ? !mixinClassName.equals("com.mrcrayfish.obfuscate.mixin.client.LivingRendererMixin") : !mixinClassName.equals("com.mrcrayfish.obfuscate.mixin.client.OptifineLivingRendererMixin");
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) 
    {

    }

    @Override
    public List<String> getMixins() 
    {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) 
    {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) 
    {

    }
}
