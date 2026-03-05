package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.command.CommandManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ConsoleLogManager;
import net.minecraft.player.EntityPlayerMP;
import net.minecraft.entity.EntityTracker;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.player.IUpdatePlayerListBox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkListenThread;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.util.PropertyManager;
import net.minecraft.util.thread.ThreadCommandReader;
import net.minecraft.util.thread.ThreadServerApplication;
import net.minecraft.util.thread.ThreadSleepForeverServer;
import net.minecraft.util.Vec3D;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;

public class MinecraftServer implements ICommandListener, Runnable {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static HashMap playerList = new HashMap();
	public NetworkListenThread networkServer;
	public PropertyManager propertyManagerObj;
	public WorldServer worldMngr;
	public ServerConfigurationManager configManager;
	public boolean serverRunning = true;
	public boolean serverStopped = false;
	int deathTime = 0;
	public String currentTask;
	public int percentDone;
	private List playersOnline = new ArrayList();
	public List commands = Collections.synchronizedList(new ArrayList());
	public EntityTracker entityTracker;
	public boolean onlineMode;

	public CommandManager commandManager;

	public MinecraftServer() {
		new ThreadSleepForeverServer(this);
	}

	private boolean startServer() throws IOException {
		ThreadCommandReader var1 = new ThreadCommandReader(this);

		var1.setDaemon(true);
		var1.start();
		ConsoleLogManager.init();
		logger.info("Starting minecraft server version 0.2.1");
		if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
			logger.warning("**** NOT ENOUGH RAM!");
			logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
		}

		logger.info("Loading properties");
		this.propertyManagerObj = new PropertyManager(new File("server.properties"));
		String var2 = this.propertyManagerObj.getStringProperty("server-ip", "");
		this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
		InetAddress var3 = null;
		if(var2.length() > 0) {
			var3 = InetAddress.getByName(var2);
		}

		commandManager = new CommandManager();
		logger.info("Loading Commands");

		int var4 = this.propertyManagerObj.getIntProperty("server-port", 25565);
		logger.info("Starting Minecraft server on " + (var2.length() == 0 ? "*" : var2) + ":" + var4);

		try {
			this.networkServer = new NetworkListenThread(this, var3, var4);
		} catch (IOException var6) {
			logger.warning("**** FAILED TO BIND TO PORT!");
			logger.log(Level.WARNING, "The exception was: " + var6.toString());
			logger.warning("Perhaps a server is already running on that port?");
			return false;
		}

		if(!this.onlineMode) {
			logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
			logger.warning("The server will make no attempt to authenticate usernames. Beware.");
			logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
			logger.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
		}

		this.configManager = new ServerConfigurationManager(this);
		this.entityTracker = new EntityTracker(this);
		String var5 = this.propertyManagerObj.getStringProperty("level-name", "world");
		logger.info("Preparing level \"" + var5 + "\"");
		this.initWorld(var5);
		logger.info("Done! For help, type \"help\" or \"?\"");
		return true;
	}

	private void initWorld(String var1) {
		logger.info("Preparing start region");
		this.worldMngr = new WorldServer(new File("."), var1, this.propertyManagerObj.getBooleanProperty("monsters", false));
		this.worldMngr.addWorldAccess(new WorldManager(this));
		this.worldMngr.difficultySetting = 1;
		this.configManager.setPlayerManager(this.worldMngr);
		byte var2 = 10;

		for(int var3 = -var2; var3 <= var2; ++var3) {
			this.outputPercentRemaining("Preparing spawn area", (var3 + var2) * 100 / (var2 + var2 + 1));

			for(int var4 = -var2; var4 <= var2; ++var4) {
				if(!this.serverRunning) {
					return;
				}

				this.worldMngr.chunkProviderServer.loadChunk((this.worldMngr.spawnX >> 4) + var3, (this.worldMngr.spawnZ >> 4) + var4);
			}
		}

		this.clearCurrentTask();
	}

	private void outputPercentRemaining(String var1, int var2) {
		this.currentTask = var1;
		this.percentDone = var2;
		System.out.println(var1 + ": " + var2 + "%");
	}

	private void clearCurrentTask() {
		this.currentTask = null;
		this.percentDone = 0;
	}

	private void save() {
		logger.info("Saving chunks");
		this.worldMngr.saveWorld(true, (IProgressUpdate)null);
	}

	private void stop() {
		logger.info("Stopping server");
		if(this.configManager != null) {
			this.configManager.savePlayerStates();
		}

		if(this.worldMngr != null) {
			this.save();
		}

	}

	public void stopRunning() {
		this.serverRunning = false;
	}

	public void run() {
		try {
			if(this.startServer()) {
				long var1 = System.currentTimeMillis();
				long var3 = 0L;

				while(this.serverRunning) {
					long var5 = System.currentTimeMillis();
					long var7 = var5 - var1;
					if(var7 > 2000L) {
						logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
						var7 = 2000L;
					}

					if(var7 < 0L) {
						logger.warning("Time ran backwards! Did the system time change?");
						var7 = 0L;
					}

					var3 += var7;
					var1 = var5;

					while(var3 > 50L) {
						var3 -= 50L;
						this.doTick();
					}

					Thread.sleep(1L);
				}
			} else {
				while(this.serverRunning) {
					this.commandLineParser();

					try {
						Thread.sleep(10L);
					} catch (InterruptedException var15) {
						var15.printStackTrace();
					}
				}
			}
		} catch (Exception var16) {
			var16.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected exception", var16);

			while(this.serverRunning) {
				this.commandLineParser();

				try {
					Thread.sleep(10L);
				} catch (InterruptedException var14) {
					var14.printStackTrace();
				}
			}
		} finally {
			this.stop();
			this.serverStopped = true;
			System.exit(0);
		}

	}

	private void doTick() throws IOException {
		ArrayList var1 = new ArrayList();
		Iterator var2 = playerList.keySet().iterator();

		while(var2.hasNext()) {
			String var3 = (String)var2.next();
			int var4 = ((Integer)playerList.get(var3)).intValue();
			if(var4 > 0) {
				playerList.put(var3, Integer.valueOf(var4 - 1));
			} else {
				var1.add(var3);
			}
		}

		int var6;
		for(var6 = 0; var6 < var1.size(); ++var6) {
			playerList.remove(var1.get(var6));
		}

		AxisAlignedBB.clearBoundingBoxPool();
		Vec3D.initialize();
		++this.deathTime;
		if(this.deathTime % 20 == 0) {
			this.configManager.sendPacketToAllPlayers(new Packet4UpdateTime(this.worldMngr.worldTime));
		}

		this.worldMngr.tick();

		while(this.worldMngr.updatingLighting()) {
		}

		this.worldMngr.updateEntities();
		this.networkServer.handleNetworkListenThread();
		this.configManager.onTick();
		this.entityTracker.updateTrackedEntities();

		for(var6 = 0; var6 < this.playersOnline.size(); ++var6) {
			((IUpdatePlayerListBox)this.playersOnline.get(var6)).addAllPlayers();
		}

		try {
			this.commandLineParser();
		} catch (Exception var5) {
			logger.log(Level.WARNING, "Unexpected exception while parsing console command", var5);
		}

	}

	public void addCommand(String var1, ICommandListener var2) {
		this.commands.add(new ServerCommand(var1, var2));
	}

	public void commandLineParser() {
		// rewritten and implemented by toru
		while (this.commands.size() > 0) {
			ServerCommand serverCommand = (ServerCommand) this.commands.remove(0);

			commandManager.executeCommand(
					this, serverCommand.commandListener,
					serverCommand.command
			);
		}
	}

	public void print(String var1, String var2) {
		String var3 = var1 + ": " + var2;
		this.configManager.sendChatMessageToAllOps("\u00a77(" + var3 + ")");
		logger.info(var3);
	}

	public int parseInt(String var1, int var2) {
		try {
			return Integer.parseInt(var1);
		} catch (NumberFormatException var4) {
			return var2;
		}
	}

	public void addToOnlinePlayerList(IUpdatePlayerListBox var1) {
		this.playersOnline.add(var1);
	}

	public static void main(String[] var0) {
		try {
			MinecraftServer var1 = new MinecraftServer();
			if(!GraphicsEnvironment.isHeadless() && (var0.length <= 0 || !var0[0].equals("nogui"))) {
				ServerGUI.initGui(var1);
			}

			(new ThreadServerApplication("Server thread", var1)).start();
		} catch (Exception var2) {
			logger.log(Level.SEVERE, "Failed to start the minecraft server", var2);
		}

	}

	public File getFile(String var1) {
		return new File(var1);
	}

	public void addHelpCommandMessage(String var1) {
		logger.info(var1);
	}

	public String getUsername() {
		return "CONSOLE";
	}

	public static boolean isServerRunning(MinecraftServer var0) {
		return var0.serverRunning;
	}
}
