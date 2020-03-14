package msifeed.mc.extensions.books;

import com.google.common.base.Enums;
import msifeed.mc.extensions.chat.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

class RemoteBookParser {
    static RemoteBook parse(String text) throws RuntimeException {
        if (text.trim().isEmpty())
            return null;

        try {
            final BufferedReader reader = new BufferedReader(new StringReader(text));
            final String firstLine = reader.readLine();
            if (firstLine == null)
                return null;
            final RemoteBook book = new RemoteBook();
            int minBodyOffset = firstLine.length();

            if (firstLine.startsWith("#!")) {
                final String[] header = firstLine.substring(2).trim().split(" ");

                if (header.length > 0)
                    book.style = Enums.getIfPresent(RemoteBook.Style.class, header[0].toUpperCase()).or(RemoteBook.Style.REGULAR);
                if (header.length > 1)
                    book.lang = Enums.getIfPresent(Language.class, header[1].toUpperCase()).or(Language.VANILLA);
                book.title = reader.readLine();
                if (book.title == null)
                    return null;
                minBodyOffset += book.title.length();
            } else {
                book.title = firstLine;
            }

            final String bodyLine = reader.readLine();
            if (bodyLine == null)
                return null;

            final String bodyLineTrimmed = bodyLine.substring(0, Math.min(bodyLine.length(), 10));
            final int bodyIndex = text.indexOf(bodyLineTrimmed, minBodyOffset);

            book.text = text.substring(bodyIndex);

            return book;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
