package com.example.demo.repository;

import com.example.demo.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRecordItemRepository extends JpaRepository<com.example.demo.model.BorrowRecordItem, Integer>, JpaSpecificationExecutor<BorrowRecord> {

}
