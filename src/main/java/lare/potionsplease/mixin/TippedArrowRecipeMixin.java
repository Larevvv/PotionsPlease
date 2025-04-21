package lare.potionsplease.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.TippedArrowRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lare.potionsplease.PotionsPlease.CONFIG;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
    @Inject(at = @At("RETURN"), method = "craft*", cancellable = true)
    private static void adjustTippedPotion(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnStack = cir.getReturnValue();
        PotionContentsComponent potionContents = returnStack.get(DataComponentTypes.POTION_CONTENTS);

        if (potionContents != null && potionContents.potion().isPresent()) {
            RegistryEntry<Potion> potion = potionContents.potion().get();
            CONFIG.ApplyPotionDuration(returnStack, potion);
        }

        cir.setReturnValue(returnStack);
    }
}
