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
    var foundNode = null;
    var instructions = method.instructions.toArray();
    for(var i = 0; i < instructions.length; i++) {
        var node = instructions[i];
        if(node.getOpcode() != Opcodes.INVOKEVIRTUAL)
            continue;
        if(!node.name.equals(ASMAPI.mapMethod("func_229111_a_")))
            continue;
        foundNode = node;
        break;
    }
    if(foundNode !== null) {
        var startNode = getNthRelativeNode(foundNode, -9);
        var nextNode = getNthRelativeNode(foundNode, 1);
        if(startNode !== null && nextNode !== null) {
            method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 1));
            method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 5));
            method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 6));
            method.instructions.insertBefore(startNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderGuiItemPre", "(Lnet/minecraft/item/ItemStack;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)Z", false));
            var jumpNode = new LabelNode();
            method.instructions.insertBefore(startNode, new JumpInsnNode(Opcodes.IFNE, jumpNode));

            method.instructions.insertBefore(nextNode, new VarInsnNode(Opcodes.ALOAD, 1));
            method.instructions.insertBefore(nextNode, new VarInsnNode(Opcodes.ALOAD, 5));
            method.instructions.insertBefore(nextNode, new VarInsnNode(Opcodes.ALOAD, 6));
            method.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderGuiItemPost", "(Lnet/minecraft/item/ItemStack;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)V", false));
            method.instructions.insertBefore(nextNode, jumpNode);
            method.instructions.insertBefore(nextNode, new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

            print("[obfuscate] Successfully patched ItemRenderer#renderItemModelIntoGUI");
            return;
        }
    }
    print("[obfuscate] Failed to patch ItemRenderer#renderItemModelIntoGUI");
}

function patch_LivingRenderer_func_225623_a_(method) {
    var startNode = null;
    var endNode = null;
    var jumpNode = null;
    var instructions = method.instructions.toArray();
    for(var i = 0; i < instructions.length; i++) {
        var node = instructions[i];
        if(node.getOpcode() != Opcodes.GETFIELD)
            continue;
        if(!node.name.equals(ASMAPI.mapField("field_77045_g")))
            continue;
        var relativeNode = getNthRelativeNode(node, -5);
        if(relativeNode === null)
            continue;
        if(relativeNode.getOpcode() != Opcodes.INVOKESTATIC)
            continue;
        if(!relativeNode.name.equals(ASMAPI.mapMethod("func_229117_c_")))
            continue;
        startNode = node.getPrevious();
        break;
    }
    for(var j = 0; j < instructions.length; j++) {
        var node1 = instructions[j];
        if(node1.getOpcode() != Opcodes.INVOKEVIRTUAL)
            continue;
        if(!node1.name.equals(ASMAPI.mapMethod("func_225598_a_")))
            continue;
        if(node1.getPrevious().getOpcode() != -1)
            continue;
        endNode = node1.getNext();
        break;
    }
    if(startNode !== null && endNode !== null) {
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 1));  // entityIn
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 0));  // this
        method.instructions.insertBefore(startNode, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/entity/LivingRenderer", ASMAPI.mapField("field_77045_g"), "Lnet/minecraft/client/renderer/entity/model/EntityModel;"));
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 4));  // MatrixStack
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 20)); // IVertexBuilder
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ILOAD, 6));  // Light
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ILOAD, 21)); // Packed Overlay
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 14));
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 13));
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 12));
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 10));
        method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 11));
        method.instructions.insertBefore(startNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderPlayerPre", "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/model/EntityModel;Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFFF)Z", false));
        method.instructions.insertBefore(startNode, new JumpInsnNode(Opcodes.IFNE, endNode));

        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.insertBefore(endNode, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/entity/LivingRenderer", ASMAPI.mapField("field_77045_g"), "Lnet/minecraft/client/renderer/entity/model/EntityModel;"));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 4));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 20));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ILOAD, 6));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ILOAD, 21));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 14));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 13));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 12));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 10));
        method.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 11));
        method.instructions.insertBefore(endNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/obfuscate/client/Hooks", "fireRenderPlayerPost", "(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/model/EntityModel;Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFFF)V", false));

        print("[obfuscate] Successfully patched LivingRenderer#func_225623_a_");
        return;
    }
    print("[obfuscate] Failed to patch LivingRenderer#func_225623_a_");
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

