package ar.edu.unicen.exa.games.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.discretizers.DiscretizerComposite;
import ar.edu.unicen.exa.games.data.discretizers.IDiscretizer;
import ar.edu.unicen.exa.games.data.filters.FilterComposite;
import ar.edu.unicen.exa.games.data.filters.IFilter;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretizer;
import ar.edu.unicen.exa.games.gameschema.XmlFilter;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.gameschema.XmlParam;

public abstract class UtilsReflection {
	private static final Logger logger = Logger.getLogger(UtilsReflection.class);

	/**
	 * Crea los discretizadores
	 * 
	 * @param props Propiedades del juego
	 * @return
	 */
	public static DiscretizerComposite createDiscretizers(XmlGameProperties props) {
		DiscretizerComposite cf = new DiscretizerComposite();

		try {
			for (XmlDiscretizer xmlDiscretizer : props.getDiscretization().getDiscretizer()) {
				// Crea el fully qualified name de la clase
				String clazz = IDiscretizer.class.getPackage().getName() + "." +  xmlDiscretizer.getClazz();
				
				// Crea los parametros fijos
				List<Class<?>> paramClasses = new ArrayList<Class<?>>();
				List<Object> paramValues = new ArrayList<Object>();
				paramClasses.add(XmlDiscretization.class);
				paramClasses.add(String.class);
				paramValues.add(props.getDiscretization());
				paramValues.add(xmlDiscretizer.getName());
				
				// Crea los pamametros extras
				for (XmlParam param : xmlDiscretizer.getParam()) {
					paramClasses.add(param.getValue().getClass());
					paramValues.add(param.getValue());
				}
				
				// Instancia el discretizador
				Constructor<?> cons = Class.forName(clazz).getConstructor(paramClasses.toArray(new Class<?>[0]));
				IDiscretizer discretizer = (IDiscretizer)cons.newInstance(paramValues.toArray());
				cf.add(discretizer);
			}
		} catch (Exception e) {
			logger.error("Ocurrio un error al crear los discretizadores", e);
		}
		
		return cf;
	}	

	/**
	 * Crea los filtros
	 * 
	 * @param props Propiedades del juego
	 * @return
	 */
	public static FilterComposite createFilters(XmlGameProperties props) {
		FilterComposite cf = new FilterComposite();

		try {
			if (props.getFiltering() != null) {
				for (XmlFilter xmlFilter : props.getFiltering().getFilter()) {
					// Crea el fully qualified name de la clase
					String clazz = IFilter.class.getPackage().getName() + "." +  xmlFilter.getClazz();
					
					// Crea los parametros
					Class<?>[] paramClasses = new Class<?>[xmlFilter.getParam().size()];
					Object[] params = new Object[xmlFilter.getParam().size()];
					int index = 0;
					for (XmlParam value : xmlFilter.getParam()) {
						paramClasses[index] = Integer.class;
						params[index++] = Integer.valueOf(value.getValue()); 
					}
					
					// Instancia el filtro
					Constructor<?> cons = Class.forName(clazz).getConstructor(paramClasses);
					IFilter filter = (IFilter)cons.newInstance(params);
					cf.add(filter);
				}
			}
		} catch (Exception e) {
			logger.error("Ocurrio un error al crear los filtros", e);
		}
		
		return cf;
	}
	
}
