package com.service.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String binaryFileName;

    private String savedBinaryFileName;

    private String savedEncFileName;

    private String iv;

    @CreatedDate // 엔티티 생성시 자동 생성을 위한 어노테이션
    private LocalDateTime createdAt;

    @Builder
    public File(Long id, String binaryFileName, String savedBinaryFileName, String savedEncFileName, String iv, LocalDateTime createdAt) {
        this.id = id;
        this.binaryFileName = binaryFileName;
        this.savedBinaryFileName = savedBinaryFileName;
        this.savedEncFileName = savedEncFileName;
        this.iv = iv;
        this.createdAt = createdAt;
    }
}
