package com.growfin.supportserv.models;

import com.growfin.supportserv.constants.enums.Priority;
import com.growfin.supportserv.constants.enums.Status;
import com.growfin.supportserv.constants.enums.Type;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code")
    private String code;

    @Column(name = "type")
    private Type type;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "agent_id",referencedColumnName = "id")
    private Agent agent;

    @Column(name = "priority")
    private Priority priority;

    @Column(name = "status")
    private Status status;

    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

}
