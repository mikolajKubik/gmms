package net.mkubik.gmms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    //@EntityListeners(AuditingEntityListener.class)
    // @CreatedDate
    // @Column(name = "created_at", nullable = false)
    // private LocalDateTime createdAt;
    //
    // @LastModifiedDate
    // @Column(name = "modified_at")
    // private LocalDateTime modifiedAt;
}

