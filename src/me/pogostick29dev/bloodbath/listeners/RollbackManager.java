package me.pogostick29dev.bloodbath.listeners;

import me.pogostick29dev.bloodbath.Arena;
import me.pogostick29dev.bloodbath.ArenaManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class RollbackManager implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

        Arena arena = ArenaManager.getInstance().getArena(event.getPlayer());

        if (arena == null) return;
        else if (arena.getState() == Arena.ArenaState.COUNTDOWN || arena.getState() == Arena.ArenaState.WAITING) event.setCancelled(true);

        if (arena.getBounds().contains(event.getBlock().getState().getLocation())) {
            if (arena.getState() == Arena.ArenaState.COUNTDOWN){
                event.setCancelled(true);
                return;
            }

        }
        handle(event.getBlock().getState(), event.getPlayer());

	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
        Arena arena = ArenaManager.getInstance().getArena(e.getPlayer());

        if (arena == null) return;
        else if (arena.getState() == Arena.ArenaState.COUNTDOWN || arena.getState() == Arena.ArenaState.WAITING) e.setCancelled(true);

        if (arena.getBounds().contains(e.getBlockReplacedState().getLocation())) {
            if (arena.getState() == Arena.ArenaState.COUNTDOWN){
                e.setCancelled(true);
                return;
            }
        }
        handle(e.getBlockReplacedState(), e.getPlayer());
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent e) {
		handle(e.getBlock().getState());
	}

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        for (Block b : e.blockList()) {
            handle(b.getState());
        }
    }
//
//    @EventHandler
//    public void onBlockExp(org.bukkit.event.block.BlockExpEvent e){
//        handle(e.getBlock().getState());
//    }
//
//    @EventHandler
//    public void onBlockFade(org.bukkit.event.block.BlockFadeEvent e){
//        handle(e.getBlock().getState());
//    }
//
//    @EventHandler
//    public void onBlockFromTo(org.bukkit.event.block.BlockFromToEvent e){
//        handle(e.getBlock().getState());
//    }
//
    @EventHandler
    public void onBlockGrow(org.bukkit.event.block.BlockGrowEvent e){
        handle(e.getNewState());
    }
//
//    @EventHandler
//    public void onBlockIgnite(org.bukkit.event.block.BlockIgniteEvent e){
//      if (e.getPlayer() != null) {
//          handle(e.getIgnitingBlock().getState(), e.getPlayer());
//      }else{
//            handle(e.getIgnitingBlock().getState());
//        }
//    }
//
//    @EventHandler
//    public void onBlockSpread(org.bukkit.event.block.BlockSpreadEvent e){
//
//        handle(e.getNewState());
//    }
//
//    @EventHandler
//    public void onBlockRedstone(org.bukkit.event.block.BlockRedstoneEvent e){
//        handle(e.getBlock().getState());
//    }
//
    @EventHandler
    public void onBlockMultiPlace(org.bukkit.event.block.BlockMultiPlaceEvent e){
        for(BlockState b : e.getReplacedBlockStates()) {
            handle(b, e.getPlayer());
        }
    }
//
//    @EventHandler
//    public void onBlockForm(org.bukkit.event.block.BlockFormEvent e){
//        handle(e.getNewState());
//    }
//
    @EventHandler
    public void onBlockPhysics(org.bukkit.event.block.BlockPhysicsEvent e){
        for (Arena arena : ArenaManager.getInstance().getArenas()) {

            if (arena.getBounds().contains(e.getBlock().getLocation()) && arena.getState() == Arena.ArenaState.RESTTING) {
                e.setCancelled(true);
                break;
            }

        }
    }
//
//    @EventHandler
//    public void onBlockPistonExtend(org.bukkit.event.block.BlockPistonExtendEvent e){
//        for(Block b : e.getBlocks()) {
//            handle(b.getState());
//        }
//    }
//
//    @EventHandler
//    public void onBlockPistonRetract(org.bukkit.event.block.BlockPistonRetractEvent e){
//
//            handle(e.getBlock().getState());
//
//
//    }
//
//    @EventHandler
//    public void onBlockForm(org.bukkit.event.block.EntityBlockFormEvent e){
//        if (e.getEntity() instanceof Player) handle(e.getNewState(), (Player) e.getEntity());
//        else handle(e.getNewState());
//    }
//
    @EventHandler
    public void onLeavesDecay(org.bukkit.event.block.LeavesDecayEvent e){
        handle(e.getBlock().getState());
    }

	/*
	 * If you don't have a player.
	 */
	private void handle(BlockState state) {
		for (Arena arena : ArenaManager.getInstance().getArenas()) {

			if (arena.getBounds().contains(state.getLocation())) {
				handle(state, arena);
				break;
			}

		}
	}
	
	private void handle(BlockState state, Player player) {
		Arena arena = ArenaManager.getInstance().getArena(player);
		
		if (arena == null) {
            return;
        }
		handle(state, arena);
	}
	
	private void handle(BlockState state, Arena arena) {
		arena.addBlockState(state);
	}
}