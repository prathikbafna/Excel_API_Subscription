package com.springBatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import com.springBatch.entity.Product;
import com.springBatch.entity.RequestPayload;

public class ProductProcessor implements ItemProcessor<Product,Product>{
	
	public ResponseEntity<String> apiCall(String email,int plan){
		String url1="http://localhost:9000/enrolluser";
		System.out.println(email);
		System.out.println(plan);
		RequestPayload request1=new RequestPayload(email,plan);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.postForEntity(url1,request1,String.class);
		
	}

	@Override
	public Product process(Product item) throws Exception {
		//write the code here to provide the subscription
		System.out.println("Hiiiiiiiiiiiiiii");
		String email = item.getEmail();
		String p = item.getPlan();
		int plan = Integer.parseInt(p);
		
		ResponseEntity<String> response2 =apiCall(email, plan);
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
		System.out.println(invs2.toString());
		System.out.println("processor ended");
		return item;
	}

}
