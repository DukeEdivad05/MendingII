package mendingII.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Inject(method = "interactItem", at = @At("HEAD"))
    private void MendingDue(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(stack.isDamageable() && stack.hasEnchantments() && player.isSneaking() && (player.experienceProgress > 0 || player.experienceLevel > 0 || player.isCreative())) {
            if(EnchantmentHelper.getEquipmentLevel(Enchantments.MENDING, player) == 2) {
                ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(player.world, player.getX(), player.getY(), player.getZ(), 1);
                experienceOrbEntity.onPlayerCollision(player);
                if(!player.isCreative()) {
                    player.addExperience(-1);
                }
            }
        }
    }
}
