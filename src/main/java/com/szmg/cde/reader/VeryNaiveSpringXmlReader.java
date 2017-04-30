package com.szmg.cde.reader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class VeryNaiveSpringXmlReader implements ExtraReferencedClassReader {

    private static final Logger LOGGER = Logger.getLogger(VeryNaiveSpringXmlReader.class.getName());
    private static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

    @Override
    public Collection<String> readAll(String name, InputStream inputStream, String libName, boolean rootLib) throws IOException {
        if (name.endsWith(".xml")) {
            return readAllFromXml(name, inputStream);
        }
        return Collections.emptySet();
    }

    private Collection<String> readAllFromXml(String name, InputStream inputStream) throws IOException {
        SpringDefaultHandler handler = new SpringDefaultHandler();
        try {
            SAX_PARSER_FACTORY.newSAXParser().parse(new NonClosableInputStream(inputStream), handler);
        } catch (SAXException e) {
            LOGGER.warning(String.format("Cannot parse XML [%s] - %s", name, e.getMessage()));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return handler.getClassesUsed();
    }

    private static class SpringDefaultHandler extends DefaultHandler {

        private boolean isInBeans = false;
        private Set<String> classesUsed = new HashSet<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (isInBeans && "bean".equals(localName)) {
                addClassIfPresent(attributes);
            } else if ("beans".equals(localName)) {
                isInBeans = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("beans".equals(localName)) {
                isInBeans = false;
            }
        }

        private void addClassIfPresent(Attributes attributes) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("class".equals(attributes.getLocalName(i))) {
                    classesUsed.add(attributes.getValue(i));
                    break;
                }
            }
        }

        public Set<String> getClassesUsed() {
            return classesUsed;
        }
    }

}
