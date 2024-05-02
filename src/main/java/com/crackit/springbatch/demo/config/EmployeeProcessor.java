package com.crackit.springbatch.demo.config;

import com.crackit.springbatch.demo.dto.EmployeeDto;
import com.crackit.springbatch.demo.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeProcessor implements ItemProcessor<EmployeeDto, Employee> {
    @Override
    public Employee process(EmployeeDto employeeDto) throws Exception {
        if(employeeDto.jobTitle().equals("Vice President")) {
            return null;
        }
        return mapEmployee(employeeDto);
    }

    private Employee mapEmployee(EmployeeDto employeeDto) {
        return Employee.builder()
                .employeeId(employeeDto.employeeId())
                .age(employeeDto.age())
                .gender(employeeDto.gender())
                .department(employeeDto.department())
                .fullName(employeeDto.fullName())
                .ethnicity(employeeDto.ethnicity())
                .businessUnit(employeeDto.businessUnit())
                .jobTitle(employeeDto.jobTitle())
                .build();
    }
}
