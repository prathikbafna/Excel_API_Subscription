package com.springBatch.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.springBatch.entity.Product;
import com.springBatch.listener.MyJobListener;
import com.springBatch.processor.ProductProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	//reader class object
	
	@Bean
    public PoiItemReader<Product> reader() {
        PoiItemReader<Product> reader = new PoiItemReader<>();
        reader.setLinesToSkip(0);
        reader.setResource(new ClassPathResource("trail.xlsx"));
//        reader.setRowMapper(excelRowMapper());
        reader.setRowMapper(new RowMapperImpl());
        return reader;
    }
 
//    private RowMapper<Product> excelRowMapper() {
//        BeanWrapperRowMapper<Product> rowMapper = new BeanWrapperRowMapper<>();
//        rowMapper.setTargetType(Product.class);
//        return rowMapper;
//    }
	
	public class RowMapperImpl implements RowMapper<Product> {
	    public RowMapperImpl() {
	    }

		@Override
		public Product mapRow(RowSet rs) throws Exception {
			// TODO Auto-generated method stub
			
			if (rs == null || rs.getCurrentRow() == null) {
	            return null;
	        }
	        Product bl = new Product();
	        String[] l = rs.getCurrentRow();
	        System.out.println(l);
	        bl.setEmail(l[0]);
	        bl.setPlan(l[1]);
	        bl.setEmpId(l[2]);
	        System.out.println(bl.toString());

	        return bl;
		}

	}
	
	//processor class object
	@Bean
	public ItemProcessor<Product, Product> processor(){
		return new ProductProcessor();
	}
	
	
	
	
	  private static void createList(Product user, Row row) // creating cells for each row
	  {
	          Cell cell = row.createCell(0);
	          cell.setCellValue(user.getEmail());
	       
	          cell = row.createCell(1);
	          cell.setCellValue(user.getPlan());
	          
	          cell = row.createCell(2);
	          cell.setCellValue(user.getStatus());
	       
	         
	    }		
    
	//writer class object
	@Bean
	public ItemWriter<Product> writer(){
		ItemWriter<Product> writer = new ItemWriter<Product>() {

			@Override
			public void write(List<? extends Product> items) throws Exception {
				
				try {
					FileInputStream fileInputStream = new FileInputStream("updated.xlsx");
					Workbook workbook = WorkbookFactory.create(fileInputStream);
					org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
					int rownum = sheet.getLastRowNum();
					
					for (Product product : items)
			          {
			        	System.out.println(product.toString());
			            Row row = sheet.createRow(++rownum);
			            createList(product, row);     
			          }   
					
					fileInputStream.close();
					FileOutputStream fileOutputStream = new FileOutputStream("updated.xlsx");
					workbook.write(fileOutputStream);
					fileOutputStream.close();
					
					
				}	
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		};
		
		
		return writer;
	}
	
	//Listener class object
	@Bean
	public JobExecutionListener listener() {
		return new MyJobListener();
	}
	
	//autowire step Builder factory
	@Autowired
	private StepBuilderFactory sf;
	
	//step object
	@Bean
	public Step stepA() {
		
		return sf.get("stepA")
				.<Product,Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.taskExecutor(taskExecutor())
				.build();
	}
	
	
	//autowire job builder factory
	@Autowired
	private JobBuilderFactory jf;
	
	
	//job object
	@Bean
	public Job jobA() {
		return jf.get("jobA")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(stepA())
				//.next(stepB())
				//.next(stepC())
				.build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleAsyncTaskExecutor.setConcurrencyLimit(1);
		
		return simpleAsyncTaskExecutor;
	}
	
	
}
