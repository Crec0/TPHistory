package tphistory.mixin;

import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tphistory.FixedSizeQueue;
import tphistory.SendMeBackPls;

import java.util.Iterator;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow public ServerPlayer player;

	@Inject(
		method = "handleTeleportToEntityPacket",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V"
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onTeleport(ServerboundTeleportToEntityPacket serverboundTeleportToEntityPacket, CallbackInfo ci, Iterator<ServerLevel> levels, ServerLevel serverLevel, Entity entity) {
		SendMeBackPls.OLD_LOCATIONS.computeIfAbsent(player.getStringUUID(), k -> new FixedSizeQueue<>(10)).push(
			new SendMeBackPls.Location(
				this.player.getLevel(),
				this.player.blockPosition(),
				this.player.getXRot(),
				this.player.getYRot()
			)
		);
	}
}
