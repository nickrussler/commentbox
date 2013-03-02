package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.w3c.tidy.Tidy;

public class StripHtml {
	public static String StripTagsAndAttributes(String Input, String[] AllowedTags) {		
		Whitelist whitelist = Whitelist.none();
		whitelist.addTags(AllowedTags);
		whitelist.addAttributes("a", "href");
		whitelist.addAttributes("img", "src", "alt", "width", "height", "title");
		
		whitelist.addAttributes("nickundahmetdiv", "class");
		whitelist.addAttributes("nickundahmetp", "class");
		whitelist.addAttributes("nickundahmetvideo", "title", "class", "type", "width", "height", "src2", "src", "frameborder", "wmode", "webkitAllowFullScreen", "mozallowfullscreen", "allowFullScreen");
		
		Document doc = Jsoup.parse(Input);
		
		Elements select = doc.select("div");		
		for (int i = 0; i < select.size(); i++) {
			Element element = select.get(i);
			
			if (element.hasClass("quote_div")) {
				element.tagName("nickundahmetdiv");
			}
		}
		
		select = doc.select("p");		
		for (int i = 0; i < select.size(); i++) {
			Element element = select.get(i);
			
			if (element.hasClass("quote_p")) {
				element.tagName("nickundahmetp");
			}
		}
		
		select = doc.select("iframe");		
		for (int i = 0; i < select.size(); i++) {
			Element element = select.get(i);
			
			String src = element.attr("src");			
			if (!src.startsWith("http://www.youtube.com/embed/") && !src.startsWith("http://player.vimeo.com/video/") && !src.startsWith("http://www.dailymotion.com/embed/video/")) {
				continue;
			}
			
			String src2 = element.attr("src2");
			if (!src2.startsWith("http://www.youtube.com/embed/") && !src2.startsWith("http://player.vimeo.com/video/") && !src2.startsWith("http://www.dailymotion.com/embed/video/")) {
				continue;
			}
			
			element.tagName("nickundahmetvideo");
		}

		String safe = Jsoup.clean(doc.body().html(), whitelist);
		
		doc = Jsoup.parse(safe);
		
		select = doc.select("nickundahmetdiv");
		select.tagName("div");
		
		select = doc.select("nickundahmetp");
		select.tagName("p");
		
		select = doc.select("nickundahmetvideo");
		select.tagName("iframe");
		
		return StringEscapeUtils.unescapeXml(doc.body().html());
	}
	
	public static String tidyHTML(String data) {
		try {
			Tidy tidy = new Tidy();
			
			tidy.setQuiet(true);
			tidy.setShowErrors(0);
			tidy.setShowWarnings(false);
			
			tidy.setInputEncoding("UTF-8");
			tidy.setOutputEncoding("UTF-8");
			tidy.setWraplen(Integer.MAX_VALUE);
			tidy.setPrintBodyOnly(true);
			tidy.setXmlOut(true);
			tidy.setSmartIndent(true);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			tidy.parseDOM(inputStream, outputStream);
			
			return outputStream.toString("UTF-8");
		} catch (Exception e) {
			Utils.log("tidyHTML", e.getMessage());
		}
		
		return null;		
	}
	
	public static String stringify(String Input) {
		Document doc = Jsoup.parse(Input);
		return doc.body().text().replaceAll("\\p{Z}+", " ").trim();
	}
	
	public static String cleanHTML(String Input) {		
		return Jsoup.clean(Input, Whitelist.none());
	}

//	public static void main(String[] args) {
//		System.out.println("result:\n\n" + StripTagsAndAttributes("<p class=\"quote_p\">asd</div>", Constants.ALLOWED_TAGS_ARTICLE));
//	}
	
//	public static void main(String[] args) {
//		System.out.println(cleanHTMLLeaveDivNBr("novapazar %50 ye      varan indirim&nbsp;<div> <br/></div><div> maxkitap ta %30 a varan&nbsp;</div><div> <br/></div><div> btsony %5 indirim&nbsp;</div><div> <br/></div><div> bunlarkacmaz da %5 indirim&nbsp;</div><div> <br/></div><div> 724tikla.com da 2 çeyrek yilbasi bileti üstelik  <strong>NIMET</strong> abladan&nbsp;</div><div> <br/></div><div> sanalmarket imden %5 indirim&nbsp;</div><div> <br/></div><div> morfare de %5 indirim&nbsp;</div><div> <br/></div><div> fotografium da %5 indirim&nbsp;</div><div> <br/></div><div> parfum.com.tr de krem hediye&nbsp;</div><div> <br/></div><div> erkekpaketi %15 indirim&nbsp;</div><div> <br/></div><div> <br/></div><div> <div>  <div>   <br/>  </div> </div></div>"));
//	}
}
