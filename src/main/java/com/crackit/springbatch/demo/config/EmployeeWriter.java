package com.crackit.springbatch.demo.config;

import com.crackit.springbatch.demo.entity.Employee;
import com.crackit.springbatch.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class EmployeeWriter implements ItemWriter<Employee> {
    private final EmployeeRepository employeeRepository;


    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        employeeRepository.saveAll(chunk);
    }
}
