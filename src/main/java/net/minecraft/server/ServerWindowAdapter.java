package net.minecraft.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter {
	final MinecraftServer mcServer;

	ServerWindowAdapter(MinecraftServer var1) {
		this.mcServer = var1;
	}

	public void windowClosing(WindowEvent var1) {
		this.mcServer.stopRunning();

		while(!this.mcServer.serverStopped) {
			try {
				Thread.sleep(100L);
			} catch (InterruptedException var3) {
				var3.printStackTrace();
			}
		}

		System.exit(0);
	}
}
