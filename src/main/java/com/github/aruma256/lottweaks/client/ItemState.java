package com.github.aruma256.lottweaks.client;

import net.minecraft.item.ItemStack;

public class ItemState {

	protected ItemStack cachedStack;

	public ItemState(ItemStack itemStack) {
		this.cachedStack = itemStack.copy();
		this.cachedStack.setCount(1);
	}

	public ItemStack toItemStack() {
		return this.cachedStack.copy();
	}

	@Override
	public boolean equals(Object obj) {
		ItemState other = (ItemState)obj;
		return ItemStack.areItemStacksEqual(this.cachedStack, other.cachedStack);
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
