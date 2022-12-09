package url.busca;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class PesquisaURL2 {
	
	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		
		System.out.println("Informe url: ");
		String url = sc.nextLine();
//		String url = "https://www.beecrowd.com.br";
		//https://en.wikipedia.org/
		
		Document document = Jsoup.connect(url).get();
		
		buscaDados(document);

	}
	
	public static void buscaDados(Document document) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		
		Integer[] link = pegarLinkExternoInterno(document);
		String linkExterno = link[0].toString();
		String linkInterno = link[1].toString();

	    mapa.put("Versão HTML: ", pegarVersaoHTML(document));
	    mapa.put("Titulo da Pagína: ", pegarTituloPagina(document));
	    mapa.put("Link Externo: ", linkExterno);
	    mapa.put("Link Interno: ", linkInterno);

	    for (Map.Entry<String, String> entrada : mapa.entrySet()) {
	      System.out.println(entrada.getKey() + ": " + entrada.getValue());
	    }
	}
	
	public static String pegarVersaoHTML(Document document) {
		
		String htmlVersion = "";
		try {
			
			String docType = pegarDocType(document);
			htmlVersion = getHtmlVersion(docType);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("Erro para pegar a versão HTML da pagina.");
		}
		
		return htmlVersion;
	}
	
	public static String pegarTituloPagina(Document document) {
		
		String title = "";
		try {
			title = document.title();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro para pegar o titulo da pagina.");
		}
		
		return title;
	}
	
	public static Integer[] pegarLinkExternoInterno(Document document) {
		
		int linkExterno = 0; 
		int linkInterno = 0;
		
		try {
			
			Elements links = document.select("a");
			
			for(Element e : links)
			{
				String href = e.attr("href");
				if (href.startsWith("http")){
					linkExterno++;
				
				} else {
					linkInterno++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro para pegar os links internos e externos da pagina.");
		}
		
		Integer[] link = new Integer[2];
		link[0] = linkExterno;
		link[1] = linkInterno;
		
		return link;
	}
	
	public static String pegarDocType(Document doc) {
		
		List<Node>nods = doc.childNodes();
        for (Node node : nods) {
           if (node instanceof DocumentType) {
               DocumentType documentType = (DocumentType)node;
                 return documentType.toString(); 
           }
       }
        return "";
	}
	
	public static String getHtmlVersion(String src) 
	{
			String html = regexHelper(src, "(?<=<!)(.*?)(?=>)"); 
				if (html.equalsIgnoreCase("doctype html"))
					return "HTML 5";
				else {
					String html2 = regexHelper(src, "\\w{4,5}\\s\\d\\..=?\\W");
					if (!html2.isEmpty()) {
						return html2;
					} else
						return "No match";
		}
	}
	
	public static String regexHelper(String src, String ptrn) {
		Pattern pattern = Pattern.compile(ptrn);
		Matcher matcher = pattern.matcher(src);
		if (matcher.find()) {
			return matcher.group();
		}else
			return "";
	}
}
