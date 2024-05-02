package com.crackit.springbatch.demo.repository;

import com.crackit.springbatch.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
