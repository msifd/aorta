import msifeed.mc.aorta.chat.composer.parser.SpeechToken;
import msifeed.mc.aorta.chat.composer.parser.SpeechTokenParser;
import msifeed.mc.aorta.chat.obfuscation.KsheminObfuscator;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import org.junit.Test;

import java.util.List;

public class TestLangObfuscator {
    @Test
    public void test() {
        List<SpeechToken> tokens = SpeechTokenParser.parse("Для современного мира высокотехнологичная концепция общественного уклада создает предпосылки для системы обучения кадров, соответствующей насущным потребностям.");

        LangObfuscator ob = new KsheminObfuscator();
        String s = ob.obfuscate(tokens);
        System.out.println(s);
    }
}
