package client.world;


import java.util.Vector;

import server.entities.Entity;
import server.entities.EntityPlayer;
import transmission.ServerUpdate;
import transmission.WorldData;
import blocks.Block;
import blocks.Chunk;
import blocks.MinimalBlock;
import enums.EnumEventType;
import world.Biome;
import world.World;

public class WorldClient extends World
{

	public WorldClient(String name) {
		super(name);
	}

	@Override
	public Biome getBiome(String pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBiomeColumn(String pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getBackBlock(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBlock(Block block, int x, int y, EnumEventType eventType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getEntityByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void overwriteEntityByID(Vector<EntityPlayer> players, int id,
			Entity newEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntityByID(Vector<EntityPlayer> players, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Block getAssociatedBlock(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getBackBlock(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MinimalBlock getBlock(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackBlock(Block block, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MinimalBlock getBlock(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunk(int x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerChunk(Chunk chunk, int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEntityToEnemyList(Object enemy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntityToNPCList(Object npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntityToProjectileList(Object projectile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItemStackToItemList(Object stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakBackBlock(ServerUpdate update, EntityPlayer player, int x,
			int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakBlock(ServerUpdate update, EntityPlayer player, int x,
			int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakCactus(ServerUpdate update, EntityPlayer player, int mx,
			int my) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakTree(ServerUpdate update, EntityPlayer player, int mx,
			int my) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlockGenerate(Block block, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBitMap(int x, int y, int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WorldData getWorldData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWorldCenterBlock() {
		// TODO Auto-generated method stub
		return 0;
	}

}
