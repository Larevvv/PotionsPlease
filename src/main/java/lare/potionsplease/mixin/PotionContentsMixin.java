package lare.potionsplease.mixin;

import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lare.potionsplease.PotionsPlease.CONFIG;


@Mixin(PotionContentsComponent.class)
public class PotionContentsMixin {
	@Inject(at = @At("RETURN"), method = "createStack", cancellable = true)
	private static void adjustBrewingResult(Item item, RegistryEntry<Potion> potion, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = cir.getReturnValue();
		CONFIG.ApplyPotionDuration(stack, potion);
		cir.setReturnValue(stack);
	}
}