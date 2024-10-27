/**
 * 
 */
package com.thacker.todo.api.base.entity;

import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

/**
 * @author Pratik Thacker
 *
 */

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractTimestampEntity {

	@Column(name = "CREATE_TS")
	@CreatedDate
	protected Timestamp createTs;
	
	@Column(name = "LAST_MODIFY_TS")
	@LastModifiedDate
	protected Timestamp lastModifyTs;

}
