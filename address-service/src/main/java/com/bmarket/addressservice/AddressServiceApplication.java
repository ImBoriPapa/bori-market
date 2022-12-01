package com.bmarket.addressservice;

import com.bmarket.addressservice.entity.Address;
import com.bmarket.addressservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class AddressServiceApplication {

    private final AddressRepository addressRepository;

    public static void main(String[] args) {
        SpringApplication.run(AddressServiceApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        addressData();
    }

    private void addressData()throws IOException {
        if (addressRepository.count().block() == 0) {
            AtomicInteger addressCode = new AtomicInteger(1000);

            ClassPathResource resource = new ClassPathResource("seoul_address.csv");

            List<Address> list = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream().map(line -> {
                        String[] split = line.split(",");
                        return Address.builder()
                                .city(split[0])
                                .district(split[1])
                                .town(split[2])
                                .addressCode(addressCode.getAndIncrement())
                                .build();
                    }).collect(Collectors.toList());

			addressRepository.saveAll(list);
        }
    }
}
