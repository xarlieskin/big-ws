package translator.service;

import translator.domain.TranslatedText;

/**
 * User: gmc
 * Date: 08/02/14
 */
public interface TranslatorService {

    TranslatedText translate(String langFrom, String langTo, String text);
}
