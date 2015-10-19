package translator.infrastructure;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.StringUtils;

import translator.domain.LanguageSourceTarget;
import translator.domain.Translator;
import translator.exception.TranslatorException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.Future;

public abstract class TranslatorImpl implements Translator {
    protected Logger LOG = LoggerFactory.getLogger(getClass());

    protected static final String ENCODING_UTF_8 = "UTF-8";

    @Override
	@Async
    public Future<String> translate(LanguageSourceTarget languageSourceTarget, String text) {
        try {
            String encodedText = URLEncoder.encode(text, ENCODING_UTF_8);
            String from = languageSourceTarget.getSource().asStr();
            String to = languageSourceTarget.getTarget().asStr();
            return new AsyncResult<String>(translateInternal(from, to, text, encodedText));
        } catch (IOException e) {
            LOG.error("Problems translating:" + e.getMessage(), e);
            throw new TranslatorException("Problems translating:" + e.getMessage(), e);
        }
    }

    protected String translateInternal(String from, String to, String text, String encodedText) throws IOException {
        HttpRequestBase requestBase = getHttpRequest(from, to, text, encodedText);
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = httpclient.execute(requestBase);
        HttpEntity responseEntity = response.getEntity();
        String responseAsStr = transformToString(responseEntity);
        if (StringUtils.hasText(responseAsStr)) {
            return getTranslationFrom(responseAsStr);
        }
        return "";
    }

    protected abstract HttpRequestBase getHttpRequest(String from, String to, String text, String encodedText);

    /**
     * Get the translation text from the response
     *
     * @param responseAsStr: Response string that contains the translation
     * @return
     */
    protected abstract String getTranslationFrom(String responseAsStr);


    protected static String transformToString(HttpEntity entity) throws IOException {
        if (entity == null) {
            return "";
        }
        try (
        	InputStream instream = entity.getContent();
        	Scanner scanner = new Scanner(instream, ENCODING_UTF_8)
        ) {
        	return scanner.useDelimiter("\\A").next();
        }
    }

}
