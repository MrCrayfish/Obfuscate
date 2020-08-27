function initializeCoreMod() {
	return {
		'gui_patch': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.ItemRenderer',
				'methodName': 'func_191962_a',
				'methodDesc': '(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/model/IBakedModel;)V'
			},
			'transformer': function(method) {
			    print("[obfuscate] Patching ItemRenderer#renderItemModelIntoGUI");
                patch_ItemRenderer_renderItemModelIntoGUI(method);
                return method;
			}
		},
		'living_renderer_patch': {
		    'target': {
		        'type': 'METHOD',
		        'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
		        'methodName': 'func_225623_a_',
		        'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
		    },
		    'transformer': function(method) {
		        print("[obfuscate] Patching LivingRenderer#func_225623_a_");
		        patch_LivingRenderer_func_225623_a_(method);
		        return method;
		    }
		},
		'held_item_layer_patch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.HeldItemLayer',
                'methodName': 'func_229135_a_',
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;Lnet/minecraft/util/HandSide;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function(method) {
                print("[obfuscate] Patching HeldItemLayer#func_229135_a_");
                patch_HeldItemLayer_func_229135_a_(method);
                return method;
            }
        },
        'entity_item_renderer_patch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.ItemRenderer',
                'methodName': 'func_225623_a_',
                'methodDesc': '(Lnet/minecraft/entity/item/ItemEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function(method) {
                print("[obfuscate] Patching ItemRenderer#func_225623_a_");
                patch_ItemRenderer_func_225623_a_(method);
                return method;
            }
        },
        'entity_item_frame_patch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.ItemFrameRenderer',
                'methodName': 'func_225623_a_',
                'methodDesc': '(Lnet/minecraft/entity/item/ItemFrameEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function(method) {
                print("[obfuscate] Patching ItemFrameRenderer#func_225623_a_");
                patch_ItemFrameRenderer_func_225623_a_(method);
                return method;
            }
        },
        'head_layer_patch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.HeadLayer',
                'methodName': 'func_225628_a_',
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/LivingEntity;FFFFFF)V'
            },
            'transformer': function(method) {
                print("[obfuscate] Patching HeadLayer#func_225628_a_");
                patch_HeadLayer_func_225628_a_(method);
                return method;
            }
        }
	};
}

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

function patch_ItemRenderer_renderItemModelIntoGUI(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_229111_a_", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderGuiItem", "(Lnet/minecraft/client/renderer/ItemRenderer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched ItemRenderer#renderItemModelIntoGUI");
        return;
    }
    print("[obfuscate] Failed to patch ItemRenderer#renderItemModelIntoGUI");
}

function patch_LivingRenderer_func_225623_a_(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_225598_a_", "(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.ALOAD, 1)); //entity
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 14)); //limbSwing
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 13)); //limbSwingAmount
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 12)); //ageInTicks
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 10)); //headYaw
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 11)); //headPitch
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderPlayer", "(Lnet/minecraft/client/renderer/entity/model/EntityModel;Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFFLnet/minecraft/entity/LivingEntity;FFFFF)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched LivingRenderer#func_225623_a_");
        return;
    }
    print("[obfuscate] Failed to patch LivingRenderer#func_225623_a_");
}

function patch_HeldItemLayer_func_229135_a_(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_228397_a_", "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderHeldItem", "(Lnet/minecraft/client/renderer/FirstPersonRenderer;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched HeldItemLayer#func_229135_a_");
        return;
    }
    print("[obfuscate] Failed to patch HeldItemLayer#func_229135_a_");
}

function patch_ItemRenderer_func_225623_a_(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_229111_a_", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.ALOAD, 1)); //entityItem
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 3)); //partialTicks
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderEntityItem", "(Lnet/minecraft/client/renderer/ItemRenderer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;Lnet/minecraft/entity/item/ItemEntity;F)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched ItemRenderer#func_229110_a_");
        return;
    }
    print("[obfuscate] Failed to patch ItemRenderer#func_229110_a_");
}

function patch_ItemFrameRenderer_func_225623_a_(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_229110_a_", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;IILcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 3));
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderItemFrameItem", "(Lnet/minecraft/client/renderer/ItemRenderer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;IILcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;F)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched ItemFrameRenderer#func_225623_a_");
        return;
    }
    print("[obfuscate] Failed to patch ItemFrameRenderer#func_225623_a_");
}

function patch_HeadLayer_func_225628_a_(method) {
    var entryNode = findFirstMethodInsnNode(method, Opcodes.INVOKEVIRTUAL, "func_228397_a_", "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V");
    if(entryNode !== null) {
        method.instructions.insertBefore(entryNode, new VarInsnNode(Opcodes.FLOAD, 7)); //partialTicks
        method.instructions.insertBefore(entryNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderHeadItem", "(Lnet/minecraft/client/renderer/FirstPersonRenderer;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IF)V", false));
        method.instructions.remove(entryNode);
        print("[obfuscate] Successfully patched HeadLayer#func_225628_a_");
        return;
    }
    print("[obfuscate] Failed to patch HeadLayer#func_225628_a_");
}

function findFirstMethodInsnNode(method, opcode, name, desc) {
    var instructions = method.instructions.toArray();
    for(var i = 0; i < instructions.length; i++) {
        var node = instructions[i];
        if(node.getOpcode() != opcode)
            continue;
        if(!node.name.equals(ASMAPI.mapMethod(name)))
            continue;
        if(!node.desc.equals(desc))
            continue;
        return node;
    }
    return null;
}

function getNthRelativeNode(node, n) {
    while(n > 0 && node !== null) {
        node = node.getNext();
        n--;
    }
    while(n < 0 && node !== null) {
        node = node.getPrevious();
        n++;
    }
    return node;
}

