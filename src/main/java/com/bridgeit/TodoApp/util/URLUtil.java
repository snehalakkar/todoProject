package com.bridgeit.TodoApp.util;

import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class URLUtil {

	public static boolean isValidUrl(String string) {
		try
	    {
	        URL url = new URL(string);
	        url.toURI();
	        return true;
	    } catch (Exception exception)
	    {
	        return false;
	    }
	}
}
