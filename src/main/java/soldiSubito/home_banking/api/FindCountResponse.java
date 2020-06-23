package soldiSubito.home_banking.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.config.PropertyVisibilityStrategy;

public class FindCountResponse {

	private final List<ContoApi> contoApiList;
	
	
	public FindCountResponse(List<ContoApi> contoApiList) {
		this.contoApiList = contoApiList;

	}
	
	
	public List<ContoApi> getContoApiList() {
		return contoApiList;
	}
	
	
	public String toJson() {

		Jsonb jsonb = JsonbBuilder.create();
		String result = jsonb.toJson(this.contoApiList);
		return result;

	}

}
