package zabi.minecraft.perkmastery.blocks.special;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.perkmastery.Config;
import zabi.minecraft.perkmastery.blocks.Block6EqualFaces;
import zabi.minecraft.perkmastery.blocks.BlockList;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer;
import zabi.minecraft.perkmastery.entity.ExtendedPlayer.PlayerClass;


public class BlockScaffold extends Block6EqualFaces {

	public BlockScaffold(String name, float hardness, Material material) {
		super(name, hardness, material);
		setLightOpacity(1);
		this.setCreativeTab(null);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float ux, float uy, float uz) {
		if (ExtendedPlayer.isEnabled(player, PlayerClass.BUILDER, 6)) {
			if (player.getHeldItem() != null) {
				if (Block.getBlockFromItem(player.getHeldItem().getItem()) != null) {
					if (Block.getBlockFromItem(player.getHeldItem().getItem()).equals(BlockList.scaffold)) return false;
					if (Block.getBlockFromItem(player.getHeldItem().getItem()).isOpaqueCube()) {
						recursiveSubstitution(world, x, y, z, player.getHeldItem(), player.capabilities.isCreativeMode, 0);
						if (player.getHeldItem().stackSize < 1) player.inventory.mainInventory[player.inventory.currentItem] = null;
						return true;
					}
				}
			}
		} else {
			if (world.isRemote) player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("general.machinery.notenabled")));
			return true;
		}

		return false;
	}

	private static void recursiveSubstitution(World world, int x, int y, int z, ItemStack itemInUse, boolean isCreative, int iteration) {
		if (iteration > Config.maxIterations) { return; }
		if (world.getBlock(x, y, z).equals(BlockList.scaffold)) {

			Block block = Block.getBlockFromItem(itemInUse.getItem());
			int meta = itemInUse.getItemDamage();
			world.setBlock(x, y, z, block, meta, 3);
			if (!isCreative) itemInUse.stackSize--;
			if (itemInUse.stackSize < 1) return;

			for (int dx = -1; dx <= 1; dx++)
				for (int dy = -1; dy <= 1; dy++)
					for (int dz = -1; dz <= 1; dz++)
						if (dx != 0 || dy != 0 || dz != 0) recursiveSubstitution(world, x + dx, y + dy, z + dz, itemInUse, isCreative, iteration + 1);
		}
	}

	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		if (player.isSneaking()) recursiveBreak(world, x, y, z, 0);
	}

	private void recursiveBreak(World world, int x, int y, int z, int iteration) {
		if (iteration > Config.maxIterations) { return; }
		if (world.getBlock(x, y, z).equals(BlockList.scaffold)) {
			world.setBlockToAir(x, y, z);
			recursiveBreak(world, x + 1, y, z, iteration + 1);
			recursiveBreak(world, x - 1, y, z, iteration + 1);
			recursiveBreak(world, x, y + 1, z, iteration + 1);
			recursiveBreak(world, x, y - 1, z, iteration + 1);
			recursiveBreak(world, x, y, z + 1, iteration + 1);
			recursiveBreak(world, x, y, z - 1, iteration + 1);
		}

	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isAir(IBlockAccess world, int x, int y, int z) { // TODO rimuovi
		return false;
	}

}
