package r.utils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WritterArff {

	public void write(String file, List<Map<String, Object>> data) {
		try (PrintWriter pw = new PrintWriter(file)) {
			// generate header
			pw.println("@relation r");
			pw.println("");
			for (Entry<String, Object> entry : data.get(0).entrySet()) {
				String type = getType(entry.getValue().getClass());
				pw.println(String.format("@attribute '%s' %s", entry.getKey(), type));
			}
			
			// generate data
			pw.println("");
			pw.println("@data");
			for (Map<String, Object> entry : data) {
				StringBuilder sb = new StringBuilder();
				for (String key : entry.keySet()) {
					sb.append(entry.get(key)).append(",");
				}
				pw.println(String.format("%s", sb.toString().substring(0, sb.length() - 1)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getType(Class<? extends Object> clazz) {
		if (String.class.equals(clazz)) {
			return "string";
		}
		if (Number.class.isAssignableFrom(clazz)) {
			return "numeric";
		}
		return "string";
	}
	
}
