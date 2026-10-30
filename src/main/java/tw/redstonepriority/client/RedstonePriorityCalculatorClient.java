package tw.redstonepriority.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import tw.redstonepriority.client.gui.PriorityCalculatorScreen;

public class RedstonePriorityCalculatorClient implements ClientModInitializer {
    private static KeyBinding openKey;
    @Override public void onInitializeClient() {
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.redstone_priority_calculator.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "category.redstone_priority_calculator"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> { while (openKey.wasPressed()) client.setScreen(new PriorityCalculatorScreen()); });
    }
}
