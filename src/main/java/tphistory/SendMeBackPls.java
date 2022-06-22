package tphistory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;

public class SendMeBackPls implements ModInitializer {
	public static Map<String, FixedSizeQueue<Location>> OLD_LOCATIONS = new HashMap<>();

	@Override
	public void onInitialize() {}

	public record Location(ServerLevel dimension, BlockPos pos, float xRot, float yRot){}
}
