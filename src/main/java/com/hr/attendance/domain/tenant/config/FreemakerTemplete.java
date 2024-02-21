package com.hr.attendance.domain.tenant.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class FreemakerTemplete {
	@Autowired
	private Configuration freemarkerConfig;

	public String processTempleteIntoString(Map<String, Object> model, String templeteName)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException {

		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
		Template t = freemarkerConfig.getTemplate(templeteName);
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		return text;

	}

}
