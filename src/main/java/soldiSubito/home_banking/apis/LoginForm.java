package soldiSubito.home_banking.apis;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;
public class LoginForm {
	
	private String cf;	
	private String pwd;
	
	@JsonbCreator
	public LoginForm(@JsonbProperty("username") String cf, @JsonbProperty("password") String pwd) {
		this.setCf(cf);
		this.setPwd(pwd);
	}

	public String toJson() {
		JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {
			
			@Override
			public boolean isVisible(Method arg0) {
				return false;
			}
			
			@Override
			public boolean isVisible(Field arg0) {
				return true;
			}
		});
		return JsonbBuilder.newBuilder().withConfig(config).build().toJson(this);
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}

