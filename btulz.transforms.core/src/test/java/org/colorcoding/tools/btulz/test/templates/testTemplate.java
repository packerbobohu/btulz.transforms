package org.colorcoding.tools.btulz.test.templates;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.test.transformers.testXmlTransformer;
import org.colorcoding.tools.btulz.transformers.XmlTransformer;
import org.colorcoding.tools.btulz.transformers.regions.RegionBusinessObject;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.RegionModel;

import junit.framework.TestCase;

public class testTemplate extends TestCase {

	private static String domain_file = testXmlTransformer.old_xml_path + File.separator + "domain_models_old.xml";
	private static String template_file = "/eclipse/ibas_classic/{artifactid}.{domain.getname.tolowercase}/src/main/java/{groupid}/{artifactid}/{domain.getname.tolowercase}/bo/Template_Model.{Model.getName}.java.txt";

	public void testJudgmentRegion() throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.load(Environment.getWorkingFolder() + domain_file, false);
		String tpltFile = Environment.getResource("code").getPath();
		RegionDomain template = new RegionDomain();
		template.setTemplateFile(tpltFile + File.separator + template_file);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			for (IBusinessObject businessObject : domain.getBusinessObjects()) {
				for (IModel model : domain.getModels()) {
					if (!model.getName().equals(businessObject.getMappedModel())) {
						continue;
					}
					File outputFile = new File(Environment.getWorkingFolder() + File.separator
							+ String.format("%s.%s.out.txt", domain.getName(), model.getName()));
					ArrayList<Parameter> parameters = new ArrayList<>();
					parameters.add(new Parameter(RegionDomain.REGION_PARAMETER_NAME, domain));
					parameters.add(new Parameter(RegionBusinessObject.REGION_PARAMETER_NAME, businessObject));
					parameters.add(new Parameter(RegionModel.REGION_PARAMETER_NAME, model));
					template.export(parameters, outputFile);
				}
			}
		}

	}
}
