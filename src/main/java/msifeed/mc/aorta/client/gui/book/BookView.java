package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBook;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.composer.SpeechComposer;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.TextWall;
import msifeed.mc.mellow.widgets.button.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookView extends Widget {
    private static final int BOOK_TEXT_WIDTH = 115;
    private static final int BOOK_MAX_LINES = 15;

    private BookParts bookParts = BookParts.REGULAR;
    private TextWall textWall = new TextWall();
    private Controls controls = new Controls(this);

    BookView() {
        setSizeHint(192, 192);
        setSizePolicy(SizePolicy.FIXED);
        getMargin().set(36, 14);
        setLayout(FreeLayout.INSTANCE);

        textWall.setLines(Collections.singletonList("Looking for book..."));
        textWall.setMaxLines(BOOK_MAX_LINES);
        flipPage(0);

        controls.setPos(0, 138);

        addChild(textWall);
        addChild(controls);
    }

    public void setBook(RemoteBook book) {
        final String text = doIKnowLanguage(book.lang)
                ? book.text
                : SpeechComposer.obfuscateWith(book.lang.obfuscator, book.text);
        setStyle(book.style);
        textWall.setLines(breakLines(text));
        flipPage(0);
    }

    public void flipPage(int n) {
        final int target = Math.max(0, textWall.getStartLine() + BOOK_MAX_LINES * n);
        textWall.setStartLine(target);
        controls.updateControls(target / BOOK_MAX_LINES + 1, textWall.getLines().size() / BOOK_MAX_LINES + 1);
    }

    @Override
    protected void renderSelf() {
        RenderParts.slice(bookParts.bookBg, getGeometry());
    }

    private void setStyle(RemoteBook.Style style) {
        if (style == RemoteBook.Style.RICH)
            bookParts = BookParts.RICH_BOOK;
        else if (style == RemoteBook.Style.PAD)
            bookParts = BookParts.PAD;
        else if (style == RemoteBook.Style.NOTE)
            bookParts = BookParts.NOTE;
        else
            bookParts = BookParts.REGULAR;

        controls.leftButton.normal = bookParts.leftBtn;
        controls.leftButton.hover = bookParts.leftBtnHover;
        controls.rightButton.normal = bookParts.rightBtn;
        controls.rightButton.hover = bookParts.rightBtnHover;
    }

    private static boolean doIKnowLanguage(Language language) {
        return language == Language.VANILLA || CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }

    private static List<String> breakLines(String text) {
        if (text.trim().isEmpty())
            return Collections.emptyList();

        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

        final ArrayList<String> lines = new ArrayList<>();
        int begin = 0;
        while (begin < text.length()) {
            String line = fr.trimStringToWidth(text.substring(begin), BOOK_TEXT_WIDTH);

            final int breakIndex = line.indexOf('\n');
            if (breakIndex > 0) {
                line = line.substring(0, breakIndex);
                begin += 1; // skip linebreak
            }

            line = line.replaceAll("[\n\r]", "");

            lines.add(line);
            begin += line.length();
        }

        return lines;
    }

    private static class Controls extends Widget {
        private ButtonIcon leftButton;
        private ButtonIcon rightButton;
        private CenteredLabel pageNum = new CenteredLabel();

        Controls(BookView view) {
            setZLevel(1);
            setSizeHint(view.bookParts.bookBg.size.x, view.bookParts.leftBtn.size.y);
            setSizePolicy(SizePolicy.FIXED);
            setLayout(ListLayout.HORIZONTAL);

            leftButton = new ButtonIcon(view.bookParts.leftBtn, view.bookParts.leftBtnHover);
            rightButton = new ButtonIcon(view.bookParts.rightBtn, view.bookParts.rightBtnHover);
            leftButton.setClickCallback(() -> view.flipPage(-1));
            rightButton.setClickCallback(() -> view.flipPage(1));

            pageNum.label.setText("1");
            pageNum.getSizeHint().x = BOOK_TEXT_WIDTH - view.bookParts.leftBtn.size.x * 2;
            pageNum.label.setColor(pageNum.label.darkColor);

            addChild(leftButton);
            addChild(pageNum);
            addChild(rightButton);
        }

        void updateControls(int page, int totalPages) {
            leftButton.setDisabled(page == 1);
            rightButton.setDisabled(page == totalPages);
            pageNum.label.setText(String.valueOf(page));
        }
    }

    private static class ButtonIcon extends Button {
        private Part normal;
        private Part hover;

        ButtonIcon(Part normal, Part hover) {
            this.normal = normal;
            this.hover = hover;

            setZLevel(1);
            setSizeHint(normal.size);
            setSizePolicy(SizePolicy.FIXED);
        }

        @Override
        protected void renderSelf() {
            if (isDisabled())
                return;

            if (isHovered())
                RenderParts.slice(hover, getGeometry());
            else
                RenderParts.slice(normal, getGeometry());
        }
    }

    private static class CenteredLabel extends Widget {
        Label label = new Label();

        CenteredLabel() {
            setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
            setLayout(new AnchorLayout());
            addChild(label);
        }
    }
}
