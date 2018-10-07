package com.mrcrayfish.obfuscate.asm;

import com.mrcrayfish.obfuscate.Obfuscate;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.MinecraftForge;
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
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if(bytes == null)
            return null;

        boolean isObfuscated = !name.equals(transformedName);
        if(transformedName.equals("net.minecraft.client.renderer.RenderItem"))
        {
            return patchRenderItem(bytes, isObfuscated);
        }
        return bytes;
    }

    private byte[] patchRenderItem(byte[] bytes, boolean isObfuscated)
    {
        Obfuscate.LOGGER.info("Applying ASM to RenderItem");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        ObfName methodName = new ObfName("func_191962_a", "renderItemModelIntoGUI");
        String params = "(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V";
        ObfName targetInstruction = new ObfName("func_180452_a", "setupGuiTransform");
        ObfName popInstruction = new ObfName("func_180454_a", "renderItem");

        for(MethodNode method : classNode.methods)
        {
            if(methodName.equals(method.name) && method.desc.equals(params))
            {
                AbstractInsnNode target = null;
                AbstractInsnNode popNode = null;
                for(AbstractInsnNode node : method.instructions.toArray())
                {
                    if(node instanceof MethodInsnNode && targetInstruction.equals(((MethodInsnNode) node).name))
                    {
                        target = node;
                        break;
                    }
                }
                for(AbstractInsnNode node : method.instructions.toArray())
                {
                    if(node.getOpcode() == Opcodes.INVOKEVIRTUAL)
                    {
                        if(popInstruction.equals(((MethodInsnNode) node).name) && node.getPrevious().getOpcode() == Opcodes.ALOAD)
                        {
                            popNode = node;
                            break;
                        }
                    }
                }

                if(target != null && popNode != null)
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

                    InsnList postEvent = new InsnList();
                    postEvent.add(new LabelNode());
                    postEvent.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
                    postEvent.add(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post"));
                    postEvent.add(new InsnNode(Opcodes.DUP));
                    postEvent.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    postEvent.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false));
                    postEvent.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
                    postEvent.add(new InsnNode(Opcodes.POP));

                    //Inserts the label node to jump to if the pre event is cancelled
                    postEvent.add(jumpNode);
                    postEvent.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    //Inserts the RenderItemEvent.Gui.Post event
                    method.instructions.insert(popNode, postEvent);

                    Obfuscate.LOGGER.info("Successfully patched RenderItem");
                    break;

                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    //Leaving the code here as an example in case I need to refresh my mind about ASM
    /*private byte[] patchEntityLivingBase(byte[] bytes, boolean isObfuscated)
    {
        Obfuscate.LOGGER.info("Applying ASM to EntityLivingBase");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        String methodName = isObfuscated ? "func_70088_a" : "entityInit";

        for(MethodNode method : classNode.methods)
        {
            if((method.name.equals(methodName)) && (method.desc.equals("()V")))
            {
                AbstractInsnNode target = method.instructions.getFirst();

                InsnList eventList = new InsnList();
                eventList.add(new LabelNode());
                eventList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
                eventList.add(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/common/event/EntityLivingInitEvent"));
                eventList.add(new InsnNode(Opcodes.DUP));
                eventList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                eventList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/common/event/EntityLivingInitEvent", "<init>", "(Lnet/minecraft/entity/EntityLivingBase;)V", false));
                eventList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
                eventList.add(new InsnNode(Opcodes.POP));

                method.instructions.insertBefore(target, eventList);

                Obfuscate.LOGGER.info("Successfully patched EntityLivingBase");
                break;
            }
        }

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }*/
}
