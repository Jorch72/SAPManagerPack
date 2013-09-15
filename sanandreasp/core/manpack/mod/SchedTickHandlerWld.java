package sanandreasp.core.manpack.mod;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import sanandreasp.core.manpack.ManPackLoadingPlugin;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.storage.WorldInfo;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SchedTickHandlerWld implements IScheduledTickHandler {
	public static boolean prevThunderState = false;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		World worldObj = (World)tickData[0];
		
		if( worldObj.isRemote || worldObj.provider.getClass() != WorldProviderSurface.class ) return;
		
		short ID = -1;
		WorldInfo wInfo = worldObj.getWorldInfo();
		int time = Math.min(wInfo.getRainTime(), wInfo.getThunderTime());
		
		if( prevThunderState && !worldObj.isThundering() ) {
			ID = 0;
		} else if( !prevThunderState && worldObj.isThundering() ) {
			ID = 1;
		}
		
		if( ID >= 0 ) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			
			try {
				dos.writeShort(ID);
				dos.writeInt(time);
				PacketDispatcher.sendPacketToAllPlayers(new Packet250CustomPayload(ModContainerManPack.channel, bos.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		prevThunderState = worldObj.isThundering();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "SAPManWeather";
	}

	@Override
	public int nextTickSpacing() {
		return 10;
	}

}
