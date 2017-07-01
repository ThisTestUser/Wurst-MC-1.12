/*
 * Copyright © 2014 - 2017 | Wurst-Imperium | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.features.special_features;

import net.wurstclient.features.SearchTags;
import net.wurstclient.features.Spf;

@SearchTags({"turn off", "hide wurst logo", "ghost mode", "stealth mode",
	"vanilla Minecraft"})
public final class DisableSpf extends Spf
{
	public final ModeSetting enableMode =
		new ModeSetting("Mode", new String[]{"Button", "Code", "Restart"}, 0);
		
	public DisableSpf()
	{
		super("Disable Wurst",
			"To disable Wurst, go to the Statistics screen and press the \"Disable Wurst\" button.\n"
				+ "There are 3 modes for re-enabling:\n"
				+ "§Button§r mode adds an Enable Wurst button in the Statistics screen.\n"
				+ "§Code§r mode generates a code that you have to type to re-enable.\n"
				+ "§Restart§r mode forces a restart to reenable the client.");
		addSetting(enableMode);
	}
}
