/*
 * Copyright © 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient;

import net.wurstclient.analytics.AnalyticsManager;
import net.wurstclient.compatibility.WMinecraft;
import net.wurstclient.events.EventManager;
import net.wurstclient.features.commands.CmdManager;
import net.wurstclient.features.mods.ModManager;
import net.wurstclient.features.special_features.SpfManager;
import net.wurstclient.files.ConfigFiles;
import net.wurstclient.files.FileManager;
import net.wurstclient.files.WurstFolders;
import net.wurstclient.font.Fonts;
import net.wurstclient.hooks.FrameHook;
import net.wurstclient.keybinds.KeybindManager;
import net.wurstclient.navigator.Navigator;
import net.wurstclient.options.FriendsList;
import net.wurstclient.options.OptionsManager;
import net.wurstclient.update.Updater;

public enum WurstClient
{
	INSTANCE;
	
	public static final String VERSION = "6.8";
	
	public AnalyticsManager analytics;
	public CmdManager commands;
	public EventManager events;
	public FileManager files;
	public FriendsList friends;
	public ModManager mods;
	public Navigator navigator;
	public final KeybindManager keybinds = new KeybindManager();
	public OptionsManager options;
	public SpfManager special;
	public Updater updater;
	
	private boolean enabled = true;
	private String enableCode = "";
	
	public void startClient()
	{
		Fonts.loadFonts();
		
		events = new EventManager();
		mods = new ModManager();
		commands = new CmdManager();
		special = new SpfManager();
		files = new FileManager();
		updater = new Updater();
		keybinds.loadDefaults();
		options = new OptionsManager();
		friends = new FriendsList();
		navigator = new Navigator();
		
		WurstFolders.initialize();
		ConfigFiles.initialize();
		files.init();
		
		navigator.sortFeatures();
		updater.checkForUpdate();
		analytics =
			new AnalyticsManager("UA-52838431-5", "client.wurstclient.net");
		analytics.trackPageView(
			"/mc" + WMinecraft.VERSION + (WMinecraft.OPTIFINE ? "-of" : "")
				+ "/v" + VERSION,
			"Wurst " + VERSION + " MC" + WMinecraft.VERSION
				+ (WMinecraft.OPTIFINE ? " OF" : ""));
		ConfigFiles.OPTIONS.save();
		
		FrameHook.maximize();
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		if(!enabled)
		{
			mods.panicMod.setEnabled(true);
			mods.panicMod.onUpdate();
			if(special.disableSpf.enableMode.getSelected() == 1)
 				generateCode();
 			else
 				WChat.clearMessages();
		}
	}
	
	private void generateCode()
 	{
 		SecureRandom random = new SecureRandom();
		enableCode = "." + new BigInteger(50, random).toString(16);
		StringSelection selection = new StringSelection(enableCode);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, null);
	    ChatUtils.message("To enable Wurst again type this code: §l" + enableCode);
	    ChatUtils.message("It has been copied to your clipboard.");	    
	    ChatUtils.message("The chat will be automatically cleared after 15 seconds.");
	    new Thread(() -> {
	    	try
			{
				Thread.sleep(15000);
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
	    	WChat.clearMessages();
	    }).start();
 	}
 	
 	public boolean checkCode(String message)
 	{
 		if(message.equals(enableCode))
 		{
 			setEnabled(true);
 			return true;
 		}
 		return false;
 	}
}
