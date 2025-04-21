package lare.potionsplease.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lare.potionsplease.PotionsPlease.CONFIG;

@Mixin(SetPotionLootFunction.class)
public class PotionLootMixin {

    @Shadow @Final private RegistryEntry<Potion> potion;

    @Inject(at = @At("RETURN"), method = "process", cancellable = true)
    private void adjustPotion(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnStack = cir.getReturnValue();
        CONFIG.ApplyPotionDuration(returnStack, this.potion);
        cir.setReturnValue(returnStack);
    }
}
