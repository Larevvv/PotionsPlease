package lare.potionsplease.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lare.potionsplease.PotionsPlease.CONFIG;

@Mixin(TradeOffers.SellPotionHoldingItemFactory.class)
public class PotionTradeMixin {
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void adjustSellItem(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir, @Local LocalRef<ItemStack> stack) {
        ItemStack returnStack = stack.get();
        PotionContentsComponent potionContents = returnStack.get(DataComponentTypes.POTION_CONTENTS);

        if (potionContents != null && potionContents.potion().isPresent()) {
            RegistryEntry<Potion> potion = potionContents.potion().get();
            CONFIG.ApplyPotionDuration(returnStack, potion);
        }

        stack.set(returnStack);
    }
}
