/*
 * Copyright 2013 WhiteByte (Nick Russler, Ahmet Yueksektepe).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.whitebyte.component.commentbox;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("info.whitebyte.ConditionalPrettyTimeConveter")
public class ConditionalPrettyTimeConveter implements Converter {
	private static Converter prettyTimeConverter = null;
	
	static {
		try {			
			prettyTimeConverter = FacesContext.getCurrentInstance().getApplication().createConverter("org.ocpsoft.PrettyTimeConverter");
		} catch (Exception e) {
			// Nothing to do here
		}
    }
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (prettyTimeConverter != null) {
			return prettyTimeConverter.getAsString(arg0, arg1, arg2);
		} else {
			// Fallback to simple method
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy");
			return sdf.format((Date) arg2);
		}
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		throw new ConverterException("Does not yet support converting String to Date");
	}
}