package com.tianyalan.prescription.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tianyalan.prescription.model.Medicine;

@Service
public class DiscoveryService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<String> getEurekaServices(){
        List<String> services = new ArrayList<String>();
        
        List<String> serviceNames = discoveryClient.getServices();
        for(String serviceName : serviceNames) {
        	List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
        	for(ServiceInstance instance :serviceInstances) {
        		serviceNames.add(String.format("%s:%s", serviceName,instance.getUri()));
        	}        	
        }

        return services;
    }

    public Medicine getProduct(String medicineCode) {
        ResponseEntity<Medicine> result =
        	restTemplate.exchange("http://productservice/v1/products/{productCode}", HttpMethod.GET, null, Medicine.class, medicineCode);
        
        return result.getBody();
    }

}
