package com.nixmash.springdata.jsoup.service;

import com.nixmash.springdata.jsoup.base.JsoupHtmlParser;
import com.nixmash.springdata.jsoup.dto.PagePreviewDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupServiceImpl implements JsoupService {

    private static final Logger logger = LoggerFactory.getLogger(JsoupServiceImpl.class);

    @Value("${jsoup.connect.useragent}")
    private String userAgent;

    @Autowired
    @Qualifier("pagePreviewParser")
    JsoupHtmlParser<PagePreviewDTO> pagePreviewParser;

    @Override
    public PagePreviewDTO getPagePreview(String url) {
        PagePreviewDTO pagePreviewDTO;
        try {
            Document doc =  Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(12000)
                    .referrer("http://www.google.com")
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .validateTLSCertificates(false)
                    .get();
            pagePreviewDTO = pagePreviewParser.parse(doc);
        } catch (IOException e) {
            logger.info(String.format("Jsoup IOException for url [%s] : %s", url, e.getMessage()));
            return null;
        }
        return pagePreviewDTO;
    }

}