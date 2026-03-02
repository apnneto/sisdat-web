package com.frw.base.negocio.siclo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

/**
 * @author Gabriel Braga
 */
public class BuscaCepWS {
	private static Logger logger = Logger.getLogger(BuscaCepWS.class.getName());

	public static JSONObject getEnderecoAPI(String cep)
			throws UnsupportedEncodingException, MalformedURLException,
			IOException {

		String urlString = "http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=json";

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
		String inputLine;
		String stringJSON = "";

		try {
			while ((inputLine = in.readLine()) != null) {
				stringJSON += inputLine;
			}

			org.json.JSONObject jObject = new org.json.JSONObject(stringJSON);

			return jObject;

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao buscar coordenadas.", e);
			e.printStackTrace();
		} finally {
			in.close();
		}
		return null;
	}

	@SuppressWarnings("finally")
	public static JSONObject getEnderecoCEP(String cep) {

		JSONObject jsn = new JSONObject();
		
		try {

			jsn = getEnderecoAPI(cep);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return jsn;
		}
	}
}
