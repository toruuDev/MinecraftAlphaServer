package net.minecraft.util.thread;

import net.minecraft.network.NetworkManager;

public class ThreadMonitorConnection extends Thread {
	final NetworkManager netManager;

	public ThreadMonitorConnection(NetworkManager var1) {
		this.netManager = var1;
	}

	public void run() {
		try {
			Thread.sleep(2000L);
			if(NetworkManager.isRunning(this.netManager)) {
				NetworkManager.getWriteThread(this.netManager).interrupt();
				this.netManager.networkShutdown("Connection closed");
			}
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}
}
