package msifeed.mc.aorta.client.gui.book;

import com.google.common.hash.Hashing;
import msifeed.mc.aorta.books.RemoteBookManager;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class ScreenBookEditor extends MellowGuiScreen {
    private final ButtonLabel getBtn;
    private Label statusLabel = new Label();
    private CheckStatus status = CheckStatus.NONE;

    public ScreenBookEditor(EntityPlayer player) {
        final Window window = new Window();
        window.setTitle(L10n.tr("item.remote_book.name"));
        scene.addChild(window);

        final Widget content = window.getContent();
        content.setLayout(ListLayout.VERTICAL);

        final TextInput input = new TextInput();
        input.setPlaceholderText("ITDB-ключ текста");
        input.setCallback(s -> {
            status = CheckStatus.NONE;
            updateStatus("");
        });
        content.addChild(input);

        getBtn = new ButtonLabel(status.buttonLabel);
        getBtn.setClickCallback(() -> {
            switch (status) {
                case NONE:
                    check(input.getText());
                    break;
                case EXISTS:
                    RemoteBookManager.INSTANCE.sendSign(input.getText());
                    closeGui();
                    break;
                case MISSING:
                    status = CheckStatus.NONE;
                    input.setText("");
                    updateStatus("");
                    break;
                default:
                    break;
            }
        });
        content.addChild(getBtn);

        updateStatus("");
        content.addChild(statusLabel);
    }

    private void check(String index) {
        status = CheckStatus.CHECKING;
        updateStatus(index);
        RemoteBookManager.INSTANCE.sendCheck(index, exists -> {
            status = exists ? CheckStatus.EXISTS : CheckStatus.MISSING;
            updateStatus(index);
        });
    }

    private void updateStatus(String index) {
        final String text;
        if (status == CheckStatus.CHECKING) {
            final int randomDatabase = stringSeededRandom(index).nextInt(Short.MAX_VALUE);
            text = "Соединение с инфосферой#0x" + Integer.toHexString(randomDatabase) + "...";
        } else if (status == CheckStatus.MISSING) {
            text = "Реультат поиска: отрицательный";
        } else if (status == CheckStatus.EXISTS) {
            text = "Реультат поиска: положительный";
        } else {
            text = "Ожидание запроса";
        }

        getBtn.setLabel(status.buttonLabel);
        statusLabel.setText("> " + text);
    }

    private static void closeGui() {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

    private static Random stringSeededRandom(String s) {
        return new Random(Hashing.murmur3_128(0).hashUnencodedChars(s).asLong());
    }

    private enum CheckStatus {
        NONE("Проверить"), CHECKING("..."), EXISTS("Загрузить"), MISSING("Отмена");

        String buttonLabel;
        CheckStatus(String bl) {
            buttonLabel = bl;
        }
    }
}
