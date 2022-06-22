package tphistory.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tphistory.FixedSizeQueue;
import tphistory.SendMeBackPls;

@Mixin(Commands.class)
public class CommandsMixin {
	@Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(
		method = "<init>",
		at = @At("RETURN")
	)
	private void onCommandRegistration(Commands.CommandSelection commandSelection, CallbackInfo ci) {
		this.dispatcher.register(
			Commands.literal("back")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					if (SendMeBackPls.OLD_LOCATIONS.containsKey(player.getStringUUID())) {
						FixedSizeQueue<SendMeBackPls.Location> locations = SendMeBackPls.OLD_LOCATIONS.get(player.getStringUUID());
						if (locations.size() > 0) {
							SendMeBackPls.Location location = locations.pop();
							BlockPos pos = location.pos();
							player.teleportTo(
								location.dimension(),
								pos.getX(),
								pos.getY(),
								pos.getZ(),
								location.yRot(),
								location.xRot()
							);
							if (locations.size() == 0) {
								SendMeBackPls.OLD_LOCATIONS.remove(player.getStringUUID());
							}
							return 0;
						}
					}
					context.getSource().sendSuccess(new TextComponent("No old position found").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
					return 0;
				})
		);
	}
}
