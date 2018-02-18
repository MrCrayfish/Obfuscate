package com.mrcrayfish.obfuscate.asm;

import com.mrcrayfish.obfuscate.Obfuscate;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Author: MrCrayfish
 */
public class ObfuscateTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if(basicClass == null)
            return null;

        if(name.equals("net.minecraft.client.renderer.RenderItem"))
        {
            return patchRenderItem(basicClass);
        }

        return basicClass;
    }

    private byte[] patchRenderItem(byte[] bytes)
    {
        Obfuscate.LOGGER.info("Applying ASM to RenderItem");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for(MethodNode method : classNode.methods)
        {
            if((method.name.equals("renderItemModelIntoGUI")) && (method.desc.equals("(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V")))
            {
                AbstractInsnNode target = null;
                for(AbstractInsnNode instruction : method.instructions.toArray())
                {
                    if(instruction.getOpcode() == Opcodes.INVOKESPECIAL)
                    {
                        if("setupGuiTransform".equals(((MethodInsnNode) instruction).name) && instruction.getPrevious().getOpcode() == Opcodes.INVOKEINTERFACE)
                        {
                            target = instruction;
                            break;
                        }
                    }
                }
                if(target != null)
                {
                    AbstractInsnNode popNode = target;
                    for(int i = 0; i < 13; i++)
                    {
                        popNode = popNode.getNext();
                    }

                    if(popNode != null)
                    {
                        InsnList preEvent = new InsnList();
                        preEvent.add(new LabelNode());
                        preEvent.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
                        preEvent.add(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Pre"));
                        preEvent.add(new InsnNode(Opcodes.DUP));
                        preEvent.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        preEvent.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Pre", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false));
                        preEvent.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));

                        LabelNode jumpNode = new LabelNode();
                        preEvent.add(new JumpInsnNode(Opcodes.IFNE, jumpNode));

                        //Inserts the RenderItemEvent.Gui.Pre event
                        method.instructions.insert(target, preEvent);

                        //Inserts the label node to jump to if the pre event is cancelled
                        method.instructions.insert(popNode, jumpNode);

                        InsnList postEvent = new InsnList();
                        postEvent.add(new LabelNode());
                        postEvent.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
                        postEvent.add(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post"));
                        postEvent.add(new InsnNode(Opcodes.DUP));
                        postEvent.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        postEvent.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false));
                        postEvent.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
                        postEvent.add(new InsnNode(Opcodes.POP));

                        //Inserts the RenderItemEvent.Gui.Post event
                        method.instructions.insert(popNode, postEvent);
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
