package tfar.chickenvshunter.client;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class BasicArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T> {
	public BasicArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
	}
}
