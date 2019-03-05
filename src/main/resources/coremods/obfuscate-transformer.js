function initializeCoreMod() {
	return {
		'coremodone': {
			'target': {
				'type': 'CLASS',
				'name': 'net.minecraft.client.renderer.ItemRenderer'
			},
			'transformer': function(classNode) {
			    print("Patching ItemRenderer...");
                var methods = classNode.methods;
                var length = methods.length;
                for(var i = 0; i < length; i++) {
                    var method = methods[i];
                    if("renderItemModelIntoGUI".equals(method.name) || "func_191962_a".equals(method.name)) {
                        injectItemRendererEvent(method);
                        break;
                    }
                }
				return classNode;
			}
		}
	}
}

var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

// The starting target instruction to find in the method 'renderItemModelIntoGUI'
var startInstruction = {
    obfName: "func_180452_a",
    name: "setupGuiTransform",
    matches: function(s) {
        return s.equals(this.obfName) || s.equals(this.name);
    }
};

// The ending target instruction to find in the method 'renderItemModelIntoGUI'
var endInstruction = {
    obfName: "func_180454_a",
    name: "renderItem",
    matches: function(s) {
        return s.equals(this.obfName) || s.equals(this.name);
    }
};

/* Injects the custom code into renderItemModelIntoGUI method */
function injectItemRendererEvent(method) {
    var startTarget;
    var endTarget;

    var instructionsArray = method.instructions.toArray();
    var length = instructionsArray.length;

    // Finds the starting target node
    for (var i = 0; i < length; i++) {
        var instruction = instructionsArray[i];
        if(instruction instanceof MethodInsnNode && startInstruction.matches(instruction.name)) {
            startTarget = instruction;
            print("Found start target " + instruction);
            break;
        }
    }

    // Finds the ending target node
    for (var j = 0; j < length; j++) {
        var instruction = instructionsArray[j];
        if(instruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            if(endInstruction.matches(instruction.name) && instruction.getPrevious().getOpcode() == Opcodes.ALOAD) {
                endTarget = instruction;
                print("Found end target " + instruction);
                break;
            }
        }
    }

    // If both were found, go ahead and insert the event code
    if(startTarget && endTarget) {
        var preEvent = [];
        preEvent.push(new LabelNode());
        preEvent.push(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/eventbus/api/IEventBus;"))
        preEvent.push(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Pre"));
        preEvent.push(new InsnNode(Opcodes.DUP));
        preEvent.push(new VarInsnNode(Opcodes.ALOAD, 1));
        preEvent.push(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Pre", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false));
        preEvent.push(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraftforge/eventbus/api/IEventBus", "post", "(Lnet/minecraftforge/eventbus/api/Event;)Z", true));

        // Added jump node if event returns false
        var jumpNode = new LabelNode();
        preEvent.push(new JumpInsnNode(Opcodes.IFNE, jumpNode));

        // Inserts the pre event (as an if statetment) after the start target node
        insertInstructions(method, startTarget, preEvent);

        var postEvent = [];
        postEvent.push(new LabelNode());
        postEvent.push(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/eventbus/api/IEventBus;"));
        postEvent.push(new TypeInsnNode(Opcodes.NEW, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post"));
        postEvent.push(new InsnNode(Opcodes.DUP));
        postEvent.push(new VarInsnNode(Opcodes.ALOAD, 1));
        postEvent.push(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/mrcrayfish/obfuscate/client/event/RenderItemEvent$Gui$Post", "<init>", "(Lnet/minecraft/item/ItemStack;)V", false));
        postEvent.push(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraftforge/eventbus/api/IEventBus", "post", "(Lnet/minecraftforge/eventbus/api/Event;)Z", true));
        postEvent.push(new InsnNode(Opcodes.POP));
        postEvent.push(jumpNode);
        postEvent.push(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        insertInstructions(method, endTarget, postEvent);

        print("Successfully patched ItemRenderer");
    }
}

/* At the time of writing this core mod InsnList class access has not been added. Instead a simple
 * array that inserts the instructions in reverse will solve the problem for now. */
function insertInstructions(method, target, instructions) {
    var length = instructions.length;
    for(var i = length - 1; i >= 0; i--) {
        method.instructions.insert(target, instructions[i]);
    }
}

