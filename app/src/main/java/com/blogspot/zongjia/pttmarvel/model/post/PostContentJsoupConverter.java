package com.blogspot.zongjia.pttmarvel.model.post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import pl.droidsonroids.jspoon.ElementConverter;
import pl.droidsonroids.jspoon.annotation.Selector;

public class PostContentJsoupConverter implements ElementConverter {

    @Override
    public String convert(Element node, Selector selector) {

        Document document = node.ownerDocument();

        document.outputSettings(new Document.OutputSettings().prettyPrint(false));

        //select all <br> tags and append \n after that
        document.select("br").after("\\n");

        //select all <p> tags and prepend \n before that
        document.select("p").before("\\n");

        //get the HTML from the document, and retaining original new lines
        String str = document.html().replaceAll("\\\\n", "\n");

        String strWithNewLines =
                Jsoup.clean(str, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

        return strWithNewLines;
    }
}
