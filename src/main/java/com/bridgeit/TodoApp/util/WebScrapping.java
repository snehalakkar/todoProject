package com.bridgeit.TodoApp.util;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bridgeit.TodoApp.DTO.WebScrapper;
import com.bridgeit.TodoApp.Service.ScrapperService;

@Component
public class WebScrapping {
	
	WebScrapper webScrapper=new WebScrapper();
	
	@Autowired
	ScrapperService scrapperService;
	
	public WebScrapper checkUrlForDescScrapping(String description) {
		// for checkinh description as a url
		// fetch the document over HTTP
		Document doc1;
		try {
			doc1 = Jsoup.connect(description).get();
		   
			URL aURL = new URL(description);
		    System.out.println("host = " + aURL.getHost());
		    
			String title = null;
		    Elements metaOgTitle = doc1.select("meta[property=og:title]");
		    System.out.println("metaOgTitle "+metaOgTitle);
		    if (metaOgTitle!=null) {
		        title = metaOgTitle.attr("content");
		    }
		    else {
		        title = doc1.title();
		    }
		    String imageUrl = null;
		    Elements metaOgImage = doc1.select("meta[property=og:image]");
		    if (metaOgImage!=null) {
		        imageUrl = metaOgImage.attr("content");
		    }
		    System.out.println("imageUrl "+ imageUrl);
		    
		    //set all parameters value in scrapper table
		    webScrapper.setHost(aURL.getHost());
		    webScrapper.setTitle(title);
		    webScrapper.setImage(imageUrl);
		    
		    scrapperService.saveScrapper(webScrapper);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return webScrapper;
		
	}
}
