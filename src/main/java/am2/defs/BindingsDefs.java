package am2.defs;

import org.lwjgl.input.Keyboard;

import am2.ArsMagica2;
import am2.gui.AuraCustomizationMenu;
import am2.items.ItemSpellBook;
import am2.packet.AMNetHandler;
import am2.utils.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class BindingsDefs {
	public static final KeyBinding ICE_BRIDGE = new KeyBinding("key.bridge", Keyboard.KEY_I, "keybindings.am2");
	public static final KeyBinding ENDER_TP = new KeyBinding("key.teleport", Keyboard.KEY_N, "keybindings.am2");
	public static final KeyBinding SHAPE_GROUP = new KeyBinding("key.shape_groups", Keyboard.KEY_C, "keybindings.am2");
	public static final KeyBinding AURA_CUSTOMIZATION = new KeyBinding("key.AuraCustomization", Keyboard.KEY_B, "keybindings.am2");

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event){
		EntityPlayer clientPlayer = FMLClientHandler.instance().getClient().thePlayer;

//		if (Minecraft.getMinecraft().currentScreen != null){
//			if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory){
//				if (ManaToggleKey.isPressed()){
//					boolean curDisplayFlag = ArsMagica2.config.displayManaInInventory();
//					ArsMagica2.config.setDisplayManaInInventory(!curDisplayFlag);
//				}
//			}
//			return;
//		}

		if (SHAPE_GROUP.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (curItem == null || (curItem.getItem() != ItemDefs.spell && curItem.getItem() != ItemDefs.spellBook && curItem.getItem() != ItemDefs.arcaneSpellbook)){
				return;
			}
			int shapeGroup;
			if (curItem.getItem() == ItemDefs.spell){
				shapeGroup = SpellUtils.cycleShapeGroup(curItem);
			}else{
				ItemStack spellStack = ((ItemSpellBook)curItem.getItem()).GetActiveItemStack(curItem);
				if (spellStack == null){
					return;
				}
				shapeGroup = SpellUtils.cycleShapeGroup(spellStack);
				((ItemSpellBook)curItem.getItem()).replaceAciveItemStack(curItem, spellStack);
			}

			AMNetHandler.INSTANCE.sendShapeGroupChangePacket(shapeGroup, clientPlayer.getEntityId());

		}
//		else if (this.SpellBookNextSpellKey.isPressed()){
//			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
//			if (curItem == null){
//				return;
//			}
//			if (curItem.getItem() == ItemDefs.spellBook || curItem.getItem() == ItemDefs.arcaneSpellbook){
//				//send packet to server
//				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_NEXT_SPELL);
//			}
//		}else if (this.SpellBookPrevSpellKey.isPressed()){
//			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
//			if (curItem == null){
//				return;
//			}
//			if (curItem.getItem() == ItemDefs.spellBook || curItem.getItem() == ItemDefs.arcaneSpellbook){
//				//send packet to server
//				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_PREV_SPELL);
//			}
//		}
		else if (AURA_CUSTOMIZATION.isPressed()){
			if (ArsMagica2.proxy.playerTracker.hasAA(clientPlayer)){
				Minecraft.getMinecraft().displayGuiScreen(new AuraCustomizationMenu());
			}
		}
	}	
}
