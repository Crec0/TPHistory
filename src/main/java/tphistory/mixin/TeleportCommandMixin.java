package tphistory.mixin;

import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tphistory.FixedSizeQueue;
import tphistory.SendMeBackPls;

@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {
	@Redirect(
		method = "performTeleport",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;getId()I"
		)
	)
	private static int capturePlayer(Entity entity) {
		if (entity instanceof ServerPlayer player) {
			SendMeBackPls.OLD_LOCATIONS.computeIfAbsent(player.getStringUUID(), k -> new FixedSizeQueue<>(10)).push(
				new SendMeBackPls.Location(
					player.getLevel(),
					player.blockPosition(),
					player.getXRot(),
					player.getYRot()
				)
			);
		}
		return entity.getId();
	}
}
