package msifeed.mc.extensions.books.client;

import msifeed.mc.extensions.books.RemoteBook;
import msifeed.mc.extensions.books.client.inner.TextWrap;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.SpeechComposer;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookView extends Widget {
    public static final int BOOK_TEXT_WIDTH = 115;
    private static final int BOOK_LINES_PER_PAGE = 19;

    private BookParts bookStyle = BookParts.REGULAR;
    private final TextWrap textWrap;
    private final Controls controls;

    private final int defaultColor;

    BookView(TextWrap textWrap) {
        this.textWrap = textWrap;
        this.controls = new Controls(this);
        this.defaultColor = textWrap.getColor();

        setLayout(FreeLayout.INSTANCE);
        setSizeHint(192, 192);
        setSizePolicy(SizePolicy.FIXED);
        getMargin().set(36, 14);

        textWrap.setLineLimit(BOOK_LINES_PER_PAGE);
        textWrap.setMaxLineWidth(BOOK_TEXT_WIDTH);
        textWrap.setSizeHint(BOOK_TEXT_WIDTH, BOOK_LINES_PER_PAGE * RenderWidgets.lineHeight());
        Widget.setFocused(textWrap);

        flipPage(0);

        controls.setPos(0, 138);

        addChild(textWrap);
        addChild(controls);
    }

    public void setLines(List<String> lines) {
        textWrap.setLines(lines);
    }

    public void setBook(RemoteBook book) {
        final String text = doIKnowLanguage(book.lang)
                ? book.text
                : SpeechComposer.obfuscateWith(book.lang.obfuscator, book.text);
        setStyle(book.style);
        textWrap.setLines(breakLines(text));
    }

    public void flipPage(int pageDelta) {
        textWrap.updateLineSkip(BOOK_LINES_PER_PAGE * pageDelta);
    }

    @Override
    protected void renderSelf() {
        RenderParts.slice(bookStyle.bookBg, getGeometry());
        controls.updateControls(textWrap.getLineSkip() / BOOK_LINES_PER_PAGE + 1, (textWrap.getLineCount() - 1) / BOOK_LINES_PER_PAGE + 1);
    }

    public void setStyle(RemoteBook.Style style) {
        if (style == RemoteBook.Style.RICH)
            bookStyle = BookParts.RICH;
        else if (style == RemoteBook.Style.PAD)
            bookStyle = BookParts.PAD;
        else if (style == RemoteBook.Style.NOTE)
            bookStyle = BookParts.NOTE;
        else
            bookStyle = BookParts.REGULAR;

        controls.leftButton.normal = bookStyle.leftBtn;
        controls.leftButton.hover = bookStyle.leftBtnHover;
        controls.rightButton.normal = bookStyle.rightBtn;
        controls.rightButton.hover = bookStyle.rightBtnHover;

        textWrap.setColor(style == RemoteBook.Style.PAD ? Color.green.getRGB() : defaultColor);
        controls.setColor(textWrap.getColor());
    }

    private static boolean doIKnowLanguage(Language language) {
        return language == Language.VANILLA || CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }

    private static List<String> breakLines(String text) {
        if (text.trim().isEmpty())
            return Collections.emptyList();

        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        final BufferedReader reader = new BufferedReader(new StringReader(text));
        final ArrayList<String> lines = new ArrayList<>();

        try {
            String l = reader.readLine();
            while (l != null) {
                if (l.isEmpty()) {
                    lines.add(l);
                } else {
                    int offset = 0;
                    while (offset < l.length()) {
                        final String trimmed = fr.trimStringToWidth(l.substring(offset), BOOK_TEXT_WIDTH);
                        offset += trimmed.length();
                        lines.add(trimmed);
                    }
                }
                l = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static class Controls extends Widget {
        private ButtonIcon leftButton;
        private ButtonIcon rightButton;
        private CenteredLabel pageNum = new CenteredLabel();

        Controls(BookView view) {
            setZLevel(1);
//            setSizeHint(view.bookStyle.bookBg.size.x, view.bookStyle.leftBtn.size.y);
            getSizeHint().y = view.bookStyle.leftBtn.size.y;
            setSizePolicy(SizePolicy.FIXED);
            setLayout(ListLayout.HORIZONTAL);

            leftButton = new ButtonIcon(view.bookStyle.leftBtn, view.bookStyle.leftBtnHover);
            rightButton = new ButtonIcon(view.bookStyle.rightBtn, view.bookStyle.rightBtnHover);
            leftButton.setClickCallback(() -> view.flipPage(-1));
            rightButton.setClickCallback(() -> view.flipPage(1));

            pageNum.label.setText("1");
            pageNum.getSizeHint().x = BOOK_TEXT_WIDTH - view.bookStyle.leftBtn.size.x * 2;
            pageNum.label.setColor(view.textWrap.getColor());

            addChild(leftButton);
            addChild(pageNum);
            addChild(rightButton);
        }

        void setColor(int color) {
            pageNum.label.setColor(color);
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
