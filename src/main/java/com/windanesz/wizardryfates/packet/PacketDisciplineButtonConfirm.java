package com.windanesz.wizardryfates.packet;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import com.windanesz.wizardryfates.item.ItemDisciplineBook;
import com.windanesz.wizardryfates.registry.Sounds;
import electroblob.wizardry.constants.Element;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * <b>[Client -> Server]</b> This packet is for handling the bauble-slot item casting buttons.
 */
public class PacketDisciplineButtonConfirm implements IMessageHandler<PacketDisciplineButtonConfirm.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {

		// Just to make sure that the side is correct
		if (ctx.side.isServer()) {

			final EntityPlayerMP player = ctx.getServerHandler().player;

			player.getServerWorld().addScheduledTask(() -> {

				if (!player.world.isRemote) {

					if (Settings.settings.book_of_fates_override) {
						if (DisciplineUtils.addPrimaryDiscipline(player, message.element, true, player)) {
							String elementName = Utils.getElementWithStyleFormat(message.element);
							if (message.element == Element.MAGIC) {
								elementName += " MAGIC";
							}
							player.sendMessage(new TextComponentTranslation("message.wizardryfates:discipline_granted", elementName));

							if (player.getHeldItemMainhand().getItem() instanceof ItemDisciplineBook) {
								player.getHeldItemMainhand().shrink(1);
							} else if (player.getHeldItemOffhand().getItem() instanceof ItemDisciplineBook) {
								player.getHeldItemOffhand().shrink(1);
							}
							player.world.playSound(null, player.getPosition(), Sounds.DISCIPLINE_BOOK_USE, SoundCategory.PLAYERS, 0.7F, 1.0F);
						}
					} else {
						player.sendMessage(new TextComponentTranslation("message.wizardryfates:book_of_fates_cannot_override_discipline"));
					}
				}
			});
		}

		return null;
	}

	public static class Message implements IMessage {

		private Element element;

		// This constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
		public Message() {
		}

		public Message(Element element) {
			this.element = element;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// The order is important
			this.element = Element.values()[buf.readInt()];
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(element.ordinal());
		}
	}
}
