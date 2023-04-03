package mendingII.mixin;

import mendingII.utils.ExperienceUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Redirect(method = "interactItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"
    ))
    private TypedActionResult<ItemStack> obstacleUse(ItemStack instance, World world, PlayerEntity user, Hand hand) {
        if(instance.isDamageable() && instance.hasEnchantments() && user.isSneaking() && (ExperienceUtils.totalXpPoints(user) > 0 || user.isCreative())) {
            if(EnchantmentHelper.getLevel(Enchantments.MENDING, instance) == 2) {
                int damage = instance.getDamage();
                int i = Math.min(20, damage);
                if (!user.isCreative()) {
                    i = Math.min(i, ExperienceUtils.totalXpPoints(user) * 2);
                }
                if (i > 0) {
                    return TypedActionResult.pass(user.getStackInHand(hand));
                }
            }
        }
        return  instance.use(world, user, hand);
    }

    @Inject(method = "interactItem", at = @At("RETURN"), cancellable = true)
    private void MendingDue(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(stack.isDamageable() && stack.hasEnchantments() && player.isSneaking() && (ExperienceUtils.totalXpPoints(player) > 0 || player.isCreative())) {
            if(EnchantmentHelper.getLevel(Enchantments.MENDING, stack) == 2) {
                int damage = stack.getDamage();
                int i = Math.min(20, damage);
                if(!player.isCreative()) {
                    i = Math.min(i, ExperienceUtils.totalXpPoints(player) * 2);
                    player.addExperience(-i/2);
                }
                stack.setDamage(damage - i);

                player.playerScreenHandler.syncState();
                cir.setReturnValue(ActionResult.success(true));
            }
        }
    }
}