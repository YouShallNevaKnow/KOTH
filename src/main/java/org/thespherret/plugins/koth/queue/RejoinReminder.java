package org.thespherret.plugins.koth.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.thespherret.plugins.koth.Main;
import org.thespherret.plugins.koth.date.time.Time;
import org.thespherret.plugins.koth.messages.Message;
import org.thespherret.plugins.koth.utils.Chat;

import java.util.HashSet;

public class RejoinReminder implements Listener {

	Main main;

	public RejoinReminder(Main main)
	{
		this.main = main;
	}

	private final HashSet<String> leftQueue = new HashSet<>();

	@EventHandler
	public void onPlayerLeave(final PlayerQuitEvent e)
	{
		HashSet<Player> players = main.getQM().getQueue();
		if (players.contains(e.getPlayer()))
		{
			players.remove(e.getPlayer());
			leftQueue.add(e.getPlayer().getName());
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				@Override
				public void run()
				{
					leftQueue.remove(e.getPlayer().getName());
				}
			}, 400L);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if (leftQueue.contains(e.getPlayer().getName()))
			Chat.sendMessage(e.getPlayer(), Message.RETURN_TO_QUEUE);

		Chat.sendFormattedMessage(e.getPlayer(), Message.GAME_STARTING_IN, Time.compareInaccurate(Time.currentInnacurateTime(), main.getDM().getNextArena().getInaccurateTime()).toString());

	}
}
