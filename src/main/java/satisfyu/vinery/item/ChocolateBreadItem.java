package satisfyu.vinery.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChocolateBreadItem extends Item {

    public ChocolateBreadItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.vinery.chocolatebread_line1.tooltip"));
            tooltip.add(Text.translatable("item.vinery.chocolatebread_line2.tooltip"));
            tooltip.add(Text.translatable("item.vinery.chocolatebread_line3.tooltip"));
            tooltip.add(Text.translatable("item.vinery.oven.tooltip"));
        } else {
            tooltip.add(Text.translatable("item.vinery.ingredient.tooltip").formatted(Formatting.GRAY, Formatting.ITALIC));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

}