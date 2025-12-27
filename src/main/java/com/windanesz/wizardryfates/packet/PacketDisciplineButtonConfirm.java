package com.windanesz.wizardryfates.packet;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import com.windanesz.wizardryfates.item.ItemDisciplineBook;
import com.windanesz.wizardryfates.item.ItemSubDisciplineBook;
import com.windanesz.wizardryfates.registry.Sounds;
import electroblob.wizardry.constants.Element;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class PacketDisciplineButtonConfirm implements IMessageHandler<PacketDisciplineButtonConfirm.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {

		// Just to make sure that the side is correct
		if (ctx.side.isServer()) {

			final EntityPlayerMP player = ctx.getServerHandler().player;

			player.getServerWorld().addScheduledTask(() -> {

				if (!player.world.isRemote) {

					if (Settings.settings.book_of_fates_override) {
						if (message.isSubDiscipline) {
							DisciplineUtils.purgeDisciplines(player, DisciplineUtils.SECONDARY_DISCIPLINE_TAG);
							for (Element element : message.elements) {
								DisciplineUtils.addSubDiscipline(player, element, false, player);
							}
						} else {
							DisciplineUtils.addMainDiscipline(player, message.elements.get(0), true, player);
						}

						if (player.getHeldItemMainhand().getItem() instanceof ItemDisciplineBook || player.getHeldItemMainhand().getItem() instanceof ItemSubDisciplineBook) {
							player.getHeldItemMainhand().shrink(1);
						} else if (player.getHeldItemOffhand().getItem() instanceof ItemDisciplineBook || player.getHeldItemOffhand().getItem() instanceof ItemSubDisciplineBook) {
							player.getHeldItemOffhand().shrink(1);
						}
						player.world.playSound(null, player.getPosition(), Sounds.DISCIPLINE_BOOK_USE, SoundCategory.PLAYERS, 0.7F, 1.0F);

					} else {
						player.sendMessage(new TextComponentTranslation("message.wizardryfates:book_of_fates_cannot_override_discipline"));
					}
				}
			});
		}

		return null;
	}

	public static class Message implements IMessage {

		private List<Element> elements;
		private boolean isSubDiscipline;

		// This constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
		public Message() {
		}

		public Message(List<Element> elements, boolean isSubDiscipline) {
			this.elements = elements;
			this.isSubDiscipline = isSubDiscipline;
		}

		public Message(Element element) {
			this.elements = new ArrayList<>();
			this.elements.add(element);
			this.isSubDiscipline = false;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// The order is important
			int size = buf.readInt();
			this.elements = new ArrayList<>(size);
			for(int i = 0; i < size; i++) {
				this.elements.add(Element.values()[buf.readInt()]);
			}
			this.isSubDiscipline = buf.readBoolean();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(elements.size());
			for (Element element : elements) {
				buf.writeInt(element.ordinal());
			}
			buf.writeBoolean(isSubDiscipline);
		}
	}
}

