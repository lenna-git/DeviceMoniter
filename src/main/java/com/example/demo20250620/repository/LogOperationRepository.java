package com.example.demo20250620.repository;

import com.example.demo20250620.entity.LogOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogOperationRepository extends JpaRepository<LogOperation, Long> {

    /**
     * 根据操作用户ID查询日志
     */
    Page<LogOperation> findByOperatorId(Long operatorId, Pageable pageable);

    /**
     * 根据操作类型查询日志
     */
    Page<LogOperation> findByOperationType(String operationType, Pageable pageable);

    /**
     * 根据操作模块查询日志
     */
    Page<LogOperation> findByOperationModule(String operationModule, Pageable pageable);

    /**
     * 根据时间范围查询日志
     */
    Page<LogOperation> findByOperationTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据操作结果查询日志
     */
    Page<LogOperation> findByOperationResult(String operationResult, Pageable pageable);

    /**
     * 根据目标类型和目标ID查询日志
     */
    List<LogOperation> findByTargetTypeAndTargetId(String targetType, Long targetId);

    /**
     * 多条件查询日志
     */
    @Query("SELECT l FROM LogOperation l WHERE " +
           "(:operatorId IS NULL OR l.operatorId = :operatorId) AND " +
           "(:operatorName IS NULL OR :operatorName = '' OR l.operatorName LIKE %:operatorName%) AND " +
           "(:operationType IS NULL OR :operationType = '' OR l.operationType LIKE %:operationType%) AND " +
           "(:operationModule IS NULL OR :operationModule = '' OR l.operationModule LIKE %:operationModule%) AND " +
           "(:operationResult IS NULL OR :operationResult = '' OR l.operationResult = :operationResult) AND " +
           "(:startTime IS NULL OR l.operationTime >= :startTime) AND " +
           "(:endTime IS NULL OR l.operationTime <= :endTime) AND " +
           "(:targetType IS NULL OR :targetType = '' OR l.targetType = :targetType) AND " +
           "(:targetId IS NULL OR l.targetId = :targetId)")
    Page<LogOperation> findByMultipleConditions(
            @Param("operatorId") Long operatorId,
            @Param("operatorName") String operatorName,
            @Param("operationType") String operationType,
            @Param("operationModule") String operationModule,
            @Param("operationResult") String operationResult,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("targetType") String targetType,
            @Param("targetId") Long targetId,
            Pageable pageable);

    /**
     * 查询最近的操作日志
     */
    List<LogOperation> findTop10ByOrderByOperationTimeDesc();

    /**
     * 统计指定时间范围内的操作数量
     */
    @Query("SELECT l.operationType, COUNT(l) FROM LogOperation l WHERE l.operationTime BETWEEN :startTime AND :endTime GROUP BY l.operationType")
    List<Object[]> countByOperationTypeInTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}