package sanandreasp.core.manpack.helpers;

import org.lwjgl.opengl.GL11;

import sanandreasp.mods.EnderStuffPlus.client.registry.TickHandlerClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderBlockGlowOverlay implements ISimpleBlockRenderingHandler {
	
	public static int renderID = 0;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IGlowBlockOverlay blockOverlay = ( block instanceof IGlowBlockOverlay ) ? (IGlowBlockOverlay)block : null;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        
        if( blockOverlay != null ) {
            float brightX = OpenGlHelper.lastBrightnessX;
            float brightY = OpenGlHelper.lastBrightnessY;
            
            char var5 = 0x000F0;
            float var6 = var5 % 65536;
            float var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6, var7);
            
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(1, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(2, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(3, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(4, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, blockOverlay.getOverlayInvTexture(5, metadata));
            tessellator.draw();
            
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
        }
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 1F, 1F, 1F); // rendering of the standard block
        
        if( block instanceof IGlowBlockOverlay ) {
        	IGlowBlockOverlay icn = (IGlowBlockOverlay)block; // second icon of the overlaying, glowing texture, here it's glass
		    Tessellator.instance.setBrightness(240); // set brightness to max. value (0xF0 or 240).
		    Tessellator.instance.setColorOpaque(255, 255, 255);
		    // render the overlaying block START
		    renderer.renderFaceYNeg(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 0));
		    renderer.renderFaceYPos(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 1));
		    renderer.renderFaceZNeg(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 2));
		    renderer.renderFaceZPos(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 3));
		    renderer.renderFaceXNeg(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 4));
		    renderer.renderFaceXPos(block, x, y, z, icn.getOverlayTexture(world, x, y, z, 5));
		    // render the overlaying block END
		    Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
