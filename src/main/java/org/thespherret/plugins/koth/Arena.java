package org.thespherret.plugins.koth;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.thespherret.plugins.koth.managers.ArenaManager;
import org.thespherret.plugins.koth.messages.Message;

import java.util.HashSet;

public class Arena implements Listener {

	ArenaManager am;

	private Cuboid cuboid;
	private Main main;
	private Countdown countdown;
	private Location spawnPoint;
	private String name;
	private boolean started;
	private HashSet<Player> players = new HashSet<>();

	public Arena(ArenaManager am, String arenaName)
	{
		this.name = arenaName;
		this.spawnPoint = am.getSpawnPoint(arenaName);
		this.am = am;
		this.main = am.getMain();
		this.countdown = new Countdown(this);

	}

	public String getArenaName()
	{
		return this.name;
	}

	public void addPlayers()
	{
		for (Player player : players)
		{
			Main.sendMessage(player, Message.STARTING);
			player.teleport(spawnPoint);
		}
	}

	public HashSet<Player> getPlayers()
	{
		return this.players;
	}

	public Cuboid getCuboid()
	{
		return cuboid;
	}

	public boolean hasStarted()

	{
		return started;
	}

	public void endArena()
	{
		if (!this.hasStarted())
			return;

		this.started = false;
		for (Player player : players)
		{
			this.main.getPM().revertPlayer(player);
		}
	}

	public void startArena()
	{
		if (this.hasStarted())
			return;

		this.started = true;
		addPlayers();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();
		if (players.contains(player))
		{
			e.setDroppedExp(0);
			e.getDrops().clear();
			e.setDeathMessage(Message.DEATH.getFormatted(player.getName()));
			players.remove(player);
		}
	}


}