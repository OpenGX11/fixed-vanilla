package net.greenjab.fixedminecraft.mixin.food;


import net.greenjab.fixedminecraft.FixedMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.greenjab.fixedminecraft.network.SyncHandler;

import java.util.Iterator;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity
{

    public ServerPlayerEntityMixin(EntityType<?> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    void onUpdate(CallbackInfo info)
    {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        SyncHandler.onPlayerUpdate(player);
    }

    @ModifyConstant(method = "increaseTravelMotionStats", constant = @Constant(floatValue = 0.1f))
    public float armorDrainsStamina(float constant) {
        int weight = 0;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        for (ItemStack item : FixedMinecraft.getArmor(player)) {
            String s = item.getItemName().toString();
            if (s.contains("iron")||s.contains("gold")) weight+=1;
            if (s.contains("diamond")||s.contains("netherite")) weight+=2;
        }
        int diff = this.getWorld().getDifficulty().getId();
        float multiplier = (diff*weight)/48.0f;
        return 0.1f*(multiplier+1.0f);
    }

    @ModifyConstant(method = "increaseTravelMotionStats", constant = @Constant(floatValue = 0.01f, ordinal = 0))
    public float swimDrainsStamina(float constant) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (player.isUsingRiptide()) {
            return 0;
        }
        int weight = 0;
        for (ItemStack item : FixedMinecraft.getArmor(player)) {
            String s = item.getItemName().toString();
            if (s.contains("iron")||s.contains("gold")) weight+=1;
            if (s.contains("diamond")||s.contains("netherite")) weight+=2;
        }
        int diff = this.getWorld().getDifficulty().getId();
        float multiplier = (diff*weight)/48.0f;
        return 0.03f*(multiplier+1.0f);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void shieldDrainsStamina(CallbackInfo ci) {
        ServerPlayerEntity SPE = (ServerPlayerEntity) (Object)this;
        if (SPE.isBlocking()) SPE.addExhaustion(0.01f);
    }

    @ModifyConstant(method = "jump", constant = @Constant(floatValue = 0.05f))
    private float noStaminaNormalJump(float constant) {
        return 0;
    }
}
