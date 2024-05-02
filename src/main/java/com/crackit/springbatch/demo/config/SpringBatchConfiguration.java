package com.crackit.springbatch.demo.config;

import com.crackit.springbatch.demo.dto.EmployeeDto;
import com.crackit.springbatch.demo.entity.Employee;
import com.crackit.springbatch.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringBatchConfiguration {

    private JobRepository jobRepository;
    private PlatformTransactionManager platformTransactionManager;
    private DataSource dataSource;

    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeDto> reader(
            @Value("#{jobParameters['inputFilePath']}")
            FileSystemResource fileSystemResource) {
        return new FlatFileItemReaderBuilder<EmployeeDto>()
                .name("employeeItemReader")
                .linesToSkip(1)
                .resource(fileSystemResource)
                .delimited()
                .names("employeeId","fullName", "jobTitle","department"
                        ,"businessUnit","gender","ethnicity","age")
                .targetType(EmployeeDto.class)
                .build();
    }

    @Bean
    public ItemProcessor<EmployeeDto, Employee> itemProcessor() {
        return new EmployeeProcessor();
    }

    @Bean
    public EmployeeWriter employeeWriter(final EmployeeRepository employeeRepository) {
        return new EmployeeWriter(employeeRepository);
    }

    @Bean
    public Step importEmployeeStep(final JobRepository repository,
                                   final PlatformTransactionManager platformTransactionManager,
                                   final EmployeeRepository employeeRepository) {
        return new StepBuilder("importEmpoyeesStep", repository)
                .<EmployeeDto, Employee>chunk(100, platformTransactionManager)
                .reader(reader(null))
                .processor(itemProcessor())
                .writer(employeeWriter(employeeRepository))
                .build();
    }

    @Bean
    public Job importEmployeesJob(final JobRepository repository,
                                  final PlatformTransactionManager transactionManager,
                                  final EmployeeRepository employeeRepository) {
        return new JobBuilder("job" , repository)
                .incrementer(new RunIdIncrementer())
                .start(importEmployeeStep(repository, transactionManager, employeeRepository))
                .build();
    }

}
