package com.softwareag.mediator.utils;

import org.codehaus.jettison.badgerfish.BadgerFishDOMDocumentSerializer;
import org.codehaus.jettison.json.JSONTokener;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author mgunasek
 * @since 6/21/11
 */
public class XmlToJsonConverter {

    static String xml = "<placeOrder xmlns=\"http://services.samples/xsd\">\n" +
            "<price>123</price>\n" +
            "<quantity>1000</quantity>\n" +
            "<symbol>TEST</symbol>\n" +
            "</placeOrder>";

    public static void main(String[] args) throws Exception {
        System.out.println("Input xml = " + xml);
        String jsonStr = XmlToJsonConverter.xmlToJsonUsingBadgerFish(xml);
        System.out.println(jsonStr);

//        JSONTokener tokener = new JSONTokener(jsonStr);
//        while(tokener.more()) {
//            System.out.println(tokener.nextValue());
//        }
//

    }

    public static String xmlToJsonUsingBadgerFish(String xml)
            throws Exception {

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element el = doc.getDocumentElement();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final BadgerFishDOMDocumentSerializer serializer = new BadgerFishDOMDocumentSerializer(baos);
        serializer.serialize(el);

        return baos.toString();
    }
}
