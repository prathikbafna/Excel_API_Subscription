package com.springBatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import com.springBatch.entity.Product;
import com.springBatch.entity.RequestPayload;

public class ProductProcessor implements ItemProcessor<Product,Product>{

	@Override
	public Product process(Product item) throws Exception {
		// TODO Auto-generated method stub
		
		//write the code here to provide the subscription
		
		String email = item.getEmail();
		String p = item.getPlan();
		int plan = Integer.parseInt(p);
		
		
		
		
		String url1="http://localhost:9000/enrolluser";
		System.out.println(email);
		System.out.println(plan);
		RequestPayload request1=new RequestPayload(email,plan);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response2 = restTemplate.postForEntity(url1,request1,String.class);
		String invs2 = response2.getBody();
		if (invs2.toString().equals("Failed")) {
			item.setStatus("Failed");
		}
		else if (invs2.toString().equals("User Already Exists")) {
			item.setStatus("Already Granted");
			return null;
		}
		else {
			item.setStatus("granted");
		}
		System.out.println("Hi2");
		System.out.println(invs2.toString());
		
//		Double cost = item.getProductCost();
//		item.setProductDiscount(cost*12/100);
//		item.setProductGst(cost*18/100);;
		return item;
	}

}
