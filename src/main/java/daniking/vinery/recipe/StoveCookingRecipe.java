package daniking.vinery.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import daniking.vinery.registry.VineryRecipeTypes;
import daniking.vinery.util.VineryUtils;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class StoveCookingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    protected final DefaultedList<Ingredient> inputs;
    protected final ItemStack output;
    protected final float experience;

    public StoveCookingRecipe(Identifier id, DefaultedList<Ingredient> inputs, ItemStack output, float experience) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.experience = experience;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return VineryUtils.matchesRecipe(inventory, inputs, 1, 3);
    }


    @Override
    public ItemStack craft(Inventory inventory) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }


    public float getExperience() {
        return experience;
    }

    public DefaultedList<Ingredient> getInputs() {
        return inputs;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return VineryRecipeTypes.STOVE_COOKING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return VineryRecipeTypes.STOVE_RECIPE_TYPE;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<StoveCookingRecipe> {

        @Override
        public StoveCookingRecipe read(Identifier id, JsonObject json) {
            final var ingredients = VineryUtils.deserializeIngredients(JsonHelper.getArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for StoveCooking Recipe");
            } else if (ingredients.size() > 3) {
                throw new JsonParseException("Too many ingredients for StoveCooking Recipe");
            } else {
                final ItemStack outputStack = ShapedRecipe.outputFromJson(json);
                float xp = JsonHelper.getFloat(json, "experience", 0.0F);
                return new StoveCookingRecipe(id, ingredients, outputStack, xp);

            }

        }


        @Override
        public StoveCookingRecipe read(Identifier id, PacketByteBuf buf) {
            final var ingredients  = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromPacket(buf));
            final ItemStack output = buf.readItemStack();
            final float xp = buf.readFloat();
            return new StoveCookingRecipe(id, ingredients, output, xp);
        }

        @Override
        public void write(PacketByteBuf packet, StoveCookingRecipe recipe) {
            packet.writeVarInt(recipe.inputs.size());
            recipe.inputs.forEach(entry -> entry.write(packet));
            packet.writeItemStack(recipe.output);
            packet.writeFloat(recipe.experience);
        }
    }
}
