package translator.domain;

import java.util.concurrent.Future;

public interface Translator {

    public Future<String> translate(LanguageSourceTarget languageSourceTarget, String text);

}
