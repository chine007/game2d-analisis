package ar.edu.unicen.exa.games.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ar.edu.unicen.exa.games.gameschema.ObjectFactory;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretizer;
import ar.edu.unicen.exa.games.gameschema.XmlFilter;
import ar.edu.unicen.exa.games.gameschema.XmlFiltering;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.gameschema.XmlProperty;

public abstract class UtilsXML {

	/**
	 * Retorna las propiedades para el juego. Las propiedades se obtienen de los siguientes files:
	 * 	1. discretize/all.xml: propiedades generales a discretizar.
	 * 	2. discretize/game.xml: propiedades a discretizar del juego.
	 * 	3. bayesDir/class.xml: discretizacion de la clase y filtros generales.
	 * 	4. bayesDir/game.xml: filtros del juego.
	 * 	
	 * La generacion de las propiedades se hace de la siguiente manera:
	 * 
	 * Para <analizedProperties>:
	 * 	1. discretize/all.xml (propiedades a analizar todos los juegos).
	 * 	2. discretize/game.xml (propiedades a analizar del juego).  
	 * 
	 * Para <filtering>:
	 * 	1. bayesDir/class.xml (filtros todos los juegos).
	 * 	2. bayesDir/game.xml (filtros del juego).
	 * 
	 * Para <discretization>
	 * 	1. bayesDir/class.xml (discretizacion clase).
	 * 	2. discretize/all.xml (discretizacion todos los juegos).
	 * 	3. discretize/game.xml (discretizacion del juego).  
	 * 
	 * Para <discretizer>:
	 * 	1. bayesDir/clas.xml (discretizer clase).
	 * 	2. discretize/all.xml (discretizer todos los juegos).
	 *  3. discretize/game.xml (discretizer del juego).
	 * 
	 * @param bayesDir
	 *            Nombre del directorio donde se encuentra el file XML para la
	 *            clase
	 * @param game
	 *            Nombre del juego
	 * @return Propiedades del juego
	 * @throws Exception
	 */
	public static XmlGameProperties parseXML(String bayesDir, String game) throws Exception {
		XmlGameProperties result = new XmlGameProperties();
		
		XmlGameProperties discAllProps = parseXML("input/discretize/all.xml");
		XmlGameProperties discGameProps = parseXML(String.format("input/discretize/%s.xml", game));
		XmlGameProperties felderDimensionProps = parseXML(String.format("input/%s/class.xml", bayesDir));
		XmlGameProperties filterGameProps = parseXML(String.format("input/%s/%s.xml", bayesDir, game));
		
		// Analiza analizedProperties
		String analizedProperties = discAllProps.getAnalizedProperties();
		analizedProperties = discGameProps.getAnalizedProperties() != null ? discGameProps.getAnalizedProperties() : analizedProperties;
		
		// Analiza filtering
		List<XmlFiltering> filtering = new ArrayList<XmlFiltering>();
		filtering.add(felderDimensionProps.getFiltering());
		filtering.add(filterGameProps.getFiltering());
		filtering.removeAll(Collections.singleton(null));
		List<XmlFilter> filters = new LinkedList<XmlFilter>();
		for (XmlFiltering xmlFiltering : filtering) {
			for (XmlFilter xmlFilter : xmlFiltering.getFilter()) {
				filters.add(xmlFilter);
			}
		}
		
		// Analiza discretization
		List<XmlDiscretization> discretization = new ArrayList<XmlDiscretization>();
		discretization.add(felderDimensionProps.getDiscretization());
		discretization.add(discAllProps.getDiscretization());
		discretization.add(discGameProps.getDiscretization());
		discretization.removeAll(Collections.singleton(null));
		Map<String, XmlProperty> discret = new LinkedHashMap<String, XmlProperty>();
		for (XmlDiscretization xmlDiscretization : discretization) {
			for (XmlProperty xmlProperty : xmlDiscretization.getProperty()) {
				discret.put(xmlProperty.getName(), xmlProperty);
			}
		}
		
		// Analiza discretizer
		List<XmlDiscretizer> discretizers = new ArrayList<XmlDiscretizer>();
		discretizers.addAll(felderDimensionProps.getDiscretization().getDiscretizer());
		discretizers.addAll(discAllProps.getDiscretization().getDiscretizer());
		if (discGameProps.getDiscretization() != null) {
			discretizers.addAll(discGameProps.getDiscretization().getDiscretizer());
		}
		discretizers.removeAll(Collections.singleton(null));
		Map<String, XmlDiscretizer> discretizersHash = new LinkedHashMap<String, XmlDiscretizer>();
		for (XmlDiscretizer xmlDiscretizer : discretizers) {
			discretizersHash.put(xmlDiscretizer.getName(), xmlDiscretizer);
		}		
		
		// Genera filtering final
		XmlFiltering finalFiltering = new XmlFiltering(); 
		for (XmlFilter xmlFilter : filters) {
			finalFiltering.getFilter().add(xmlFilter);
		}
		
		// Genera discretization final
		XmlDiscretization finalDiscretization = new XmlDiscretization(); 
		for (XmlProperty xmlProperty : discret.values()) {
			finalDiscretization.getProperty().add(xmlProperty);
		}
		
		// Genera discretizer final
		for (XmlDiscretizer xmlDiscretizer : discretizersHash.values()) {
			 finalDiscretization.getDiscretizer().add(xmlDiscretizer);
		}		

		result.setAnalizedProperties(analizedProperties);
		result.setFiltering(finalFiltering);
		result.setDiscretization(finalDiscretization);
		
//		printXML(result);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static XmlGameProperties parseXML(String fileName) throws Exception {
		Unmarshaller um = JAXBContext.newInstance(XmlGameProperties.class.getPackage().getName()).createUnmarshaller();
		File file = new File(fileName);
		if (file.exists()) {
			return ((JAXBElement<XmlGameProperties>)um.unmarshal(new File(fileName))).getValue();
		}
		return new XmlGameProperties();
	}

	@SuppressWarnings("unused")
	private static void printXML(XmlGameProperties result) throws Exception {
		Marshaller marshall = JAXBContext.newInstance(XmlGameProperties.class.getPackage().getName()).createMarshaller();
		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		JAXBElement<XmlGameProperties> je = new ObjectFactory().createGameProperties(result);
		marshall.marshal(je, System.out);
	}
	
}
