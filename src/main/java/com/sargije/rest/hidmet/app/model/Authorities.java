package com.sargije.rest.hidmet.app.model;
// Generated Jun 14, 2017 10:17:23 PM by Hibernate Tools 5.2.3.Final

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.aexp.nodes.graphql.annotations.GraphQLIgnore;

import javax.persistence.*;

/**
 * Authorities generated by hbm2java
 */
@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = { "username", "authority" }))
public class Authorities implements java.io.Serializable {


	/**
	 * 
	 */
	@GraphQLIgnore
	private static final long serialVersionUID = -3616732086118168488L;
	private AuthoritiesId id;

	@GraphQLIgnore
	@JsonBackReference
	private Users users;

	public Authorities() {
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "username", column = @Column(name = "username", nullable = false, length = 50)),
			@AttributeOverride(name = "authority", column = @Column(name = "authority", nullable = false, length = 50)) })
	public AuthoritiesId getId() {
		return this.id;
	}

	public void setId(AuthoritiesId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false, insertable = false, updatable = false)
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {this.users = users;	}

}
