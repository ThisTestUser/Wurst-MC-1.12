/*
 * Copyright © 2014 - 2017 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.mods.other;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.wurstclient.compatibility.WMath;
import net.wurstclient.compatibility.WMinecraft;
import net.wurstclient.compatibility.WMisc;
import net.wurstclient.events.listeners.UpdateListener;
import net.wurstclient.features.Category;
import net.wurstclient.features.Mod;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.ModeSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;

@Mod.Bypasses
public final class FakeHackersMod extends Mod implements UpdateListener
{
	private final CheckboxSetting aimbot = new CheckboxSetting(
		"Make Players Lock to You", true);
	private final CheckboxSetting attack = new CheckboxSetting(
		"Make Players Swing Arm", false);  
	private final CheckboxSetting derp = new CheckboxSetting(
		"Spin Players' Heads (Overrides Aimbot Setting)", false)
	{
		@Override
		public void update()
		{
			derpRange.setDisabled(!isChecked());  
		};
	};
	private final SliderSetting derpRange = new SliderSetting("Derp Range", 6, 3, 150,
		3, ValueDisplay.DECIMAL);
	private final CheckboxSetting sneak = new CheckboxSetting(
		"Make Players Sneak", false);
	private final ModeSetting mode = new ModeSetting("Victims", new String[]{
		"All", "fakeHackerVictim"}, 0);
	
	public FakeHackersMod()
	{
		super("FakeHackers",
			"Makes other players look like aimbot hackers."
				+ "You can make them appear to attack you also.\n"
				+ "Use Panic and RecordingMode to record them.");
		setCategory(Category.OTHER);
	}
	
	@Override
	public void initSettings()
	{
		addSetting(mode);
		addSetting(aimbot);
		addSetting(attack);
		addSetting(derp);
		addSetting(derpRange);
		addSetting(sneak);
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		for(EntityPlayer entity : WMinecraft.getWorld().playerEntities)
			if(entity instanceof EntityOtherPlayerMP)   
	        {  
				if(entity.getDisplayName().getUnformattedText().equals(wurst.options.fakeHackerVictim) || mode.getSelected() == 0)
				{
					if(derp.isChecked() && WMinecraft.getPlayer().getDistanceToEntity(entity) <= derpRange.getValueF())
					{
						entity.rotationYawHead = (float)(Math.random() * 360 - 180);
						entity.rotationYaw = (float)(Math.random() * 360 - 180);
						entity.rotationPitch = (float)(Math.random() * 180 - 90);  
					}
					if(WMinecraft.getPlayer().getDistanceToEntity(entity) <= 3.8D)
					{
						if(aimbot.isChecked() && !derp.isChecked())
			        	{
							double lookX = WMinecraft.getPlayer().posX - entity.posX;
							double lookZ = WMinecraft.getPlayer().posZ - entity.posZ;
							double lookY = WMinecraft.getPlayer().posY + WMinecraft.getPlayer().getEyeHeight() - 
								(entity.posY + entity.getEyeHeight());
							double dist = Math.sqrt((lookX * lookX + lookZ * lookZ));
							float yaw = (float)(Math.atan2(lookZ, lookX) * 180.0D / Math.PI) - 90.0F;
							float pitch = (float)-(Math.atan2(lookY, dist) * 180.0D / Math.PI);
							entity.rotationYawHead += WMath.wrapDegrees(yaw - entity.rotationYawHead);
							entity.rotationYaw += WMath.wrapDegrees(yaw - entity.rotationYaw);
							entity.rotationPitch += WMath.wrapDegrees(pitch - entity.rotationPitch);
			        	}
						if(attack.isChecked() && !entity.isSwingInProgress)
							WMisc.swingEntityItem(entity);
					}
					if(sneak.isChecked())
						entity.setSneaking(true);
				}
	}
	
	public boolean isFakeHackerVictim(EntityOtherPlayerMP en)
	{
		return isActive() && 
			(en.getDisplayName().getUnformattedText().equals(wurst.options.fakeHackerVictim) || mode.getSelected() == 0);
	}
}
