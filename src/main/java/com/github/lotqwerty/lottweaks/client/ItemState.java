package com.github.lotqwerty.lottweaks.client;

import net.minecraft.item.ItemStack;

public class ItemState {

	protected ItemStack cachedStack;

	public ItemState(ItemStack itemStack) {
		this.cachedStack = itemStack;
	}

	public ItemStack toItemStack() {
		return this.cachedStack.copy();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemState)) {
			return false;
		}
		ItemState other = (ItemState)obj;
		return ItemStack.areItemsEqual(this.cachedStack, other.cachedStack) && ItemStack.areItemStackTagsEqual(this.cachedStack, other.cachedStack);
	}

	@Override
	public int hashCode() {
		int hash = 17 * this.cachedStack.getItem().hashCode() + this.cachedStack.getItemDamage();
		if (this.cachedStack.hasTagCompound()) {
			hash += this.cachedStack.getTagCompound().hashCode();
		}
		return hash;
	}

}
