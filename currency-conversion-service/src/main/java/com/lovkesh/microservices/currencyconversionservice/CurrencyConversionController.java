package com.lovkesh.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class CurrencyConversionController {
	
	@Autowired
	currencyExchangeServiceProxy exchangeProxy;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, 
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		System.out.println("Currency conversion controller");
		
		Map<String,String> uriVariable = new HashMap<String, String>();
		uriVariable.put("from", from);
		uriVariable.put("to", to);
		
		ResponseEntity<CurrencyConversionBean> resposeEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversionBean.class, uriVariable);
		CurrencyConversionBean response = resposeEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
		
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, 
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		
		CurrencyConversionBean response = exchangeProxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
		
	}
}
