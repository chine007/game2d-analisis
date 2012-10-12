package ar.edu.unicen.exa.games.utils;

import java.io.FileWriter;
import java.io.StringWriter;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import ar.edu.unicen.exa.games.data.analizers.weka.utils.HelperPrediction;
import ar.edu.unicen.exa.games.data.analizers.weka.utils.HelperPredictionAccuracy;

/**
 * Imprime el resumen de los resultados
 * 
 * @author Juan
 *
 */
public class VelocityPrinter {
	private static final Logger logger = Logger.getLogger(VelocityPrinter.class);

	/**
	 * Imprime el resumen de los resultados
	 * 
	 * @param prediction Resultados obtenidos
	 * @param classifierOptions Opciones del clasificador
	 */
	public void print(HelperPrediction prediction, String classifierOptions) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		ve.setProperty(RuntimeConstants.RUNTIME_LOG, "logs\\velocity.log");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		
		VelocityContext context = new VelocityContext();
		context.put("prediction", prediction);
		context.put("accuracyAlgor", new HelperPredictionAccuracy());
		context.put("classifierOptions", classifierOptions);
		context.put("felderClasses", UtilsFelderDimension.getMappingDiscretizationFelder());
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		context.put("nf", nf);

		Template template = null;
		try {
			template = ve.getTemplate("summary.vm");

			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			
			FileWriter fw = new FileWriter(UtilsConfig.get().getValue("arff.dir.path") + "summary.html", true);
			fw.write(sw.toString());
			fw.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
