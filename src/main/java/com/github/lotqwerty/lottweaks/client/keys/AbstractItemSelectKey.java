package com.github.lotqwerty.lottweaks.client.keys;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public abstract class AbstractItemSelectKey extends AbstractLTKey{

	protected final Deque<ItemStack> candidates = new LinkedList<>();
	protected int lastRotateTime = -1;
	protected byte rotateDirection = 0;

	public AbstractItemSelectKey(String description, int keyCode, String category) {
		super(description, keyCode, category);
	}

	protected void addToCandidates(ItemStack itemStack) {
		for (ItemStack c: candidates) {
			if (ItemStack.areItemStacksEqual(c, itemStack)) {
				return;
			}
		}
		candidates.add(itemStack);
	}
	
	protected void rotateCandidatesForward() {
		candidates.addFirst(candidates.pollLast());
		this.updateLastRotateTime();
		this.rotateDirection = 1;
	}

	protected void rotateCandidatesBackward() {
		candidates.addLast(candidates.pollFirst());
		this.updateLastRotateTime();
		this.rotateDirection = -1;
	}

	protected void updateCurrentItemStack(ItemStack itemStack) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.player.inventory.setInventorySlotContents(mc.player.inventory.currentItem, itemStack);
        mc.playerController.sendSlotPacket(mc.player.getHeldItem(EnumHand.MAIN_HAND), 36 + mc.player.inventory.currentItem);
	}

	@Override
	protected void onKeyPressStart() {
		this.resetLastRotateTime();
		this.rotateDirection = 0;
	}

	private void resetLastRotateTime() {
		this.lastRotateTime = 0;
	}

	private void updateLastRotateTime() {
		this.lastRotateTime = this.pressTime;
	}
	
}
