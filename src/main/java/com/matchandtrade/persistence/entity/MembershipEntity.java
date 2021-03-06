package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "membership")
public class MembershipEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Type {
		OWNER, MEMBER
	}
	
	private Set<ArticleEntity> articles = new HashSet<>();
	private Set<OfferEntity> offers = new HashSet<>();
	private TradeEntity trade;
	private Integer membershipId;
	private Type type;
	private UserEntity user;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MembershipEntity that = (MembershipEntity) o;
		return Objects.equals(trade, that.trade) &&
			Objects.equals(membershipId, that.membershipId) &&
			type == that.type &&
			Objects.equals(user, that.user);
	}

	@OneToMany
	@JoinTable(name="membership_to_article", joinColumns=@JoinColumn(name="membership_id", foreignKey=@ForeignKey(name="membership_to_article_membership_id_fk")), inverseJoinColumns = @JoinColumn(name="article_id", foreignKey=@ForeignKey(name="membership_to_article_article_id_fk")))
	public Set<ArticleEntity> getArticles() {
		return articles;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="membership_to_offer", joinColumns=@JoinColumn(name="membership_id", foreignKey=@ForeignKey(name="membership_to_offer_membership_id_fk")), inverseJoinColumns=@JoinColumn(name="offer_id", foreignKey=@ForeignKey(name="membership_to_offer_offer_id_fk")))
	public Set<OfferEntity> getOffers() {
		return offers;
	}

	@OneToOne
	@JoinColumn(name="trade_id", foreignKey=@ForeignKey(name="membership_trade_id_fk"))
	public TradeEntity getTrade() {
		return trade;
	}

	@Id
	@Column(name="membership_id")
	@SequenceGenerator(name="membership_id_generator", sequenceName = "membership_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "membership_id_generator")
	public Integer getMembershipId() {
		return membershipId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false, length = 100)
	public Type getType() {
		return type;
	}

	@OneToOne
	@JoinColumn(name="user_id", foreignKey=@ForeignKey(name="membership_user_id_fk"))
	public UserEntity getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(trade, membershipId, type, user);
	}

	public void setArticles(Set<ArticleEntity> articles) {
		this.articles = articles;
	}

	public void setOffers(Set<OfferEntity> offers) {
		this.offers = offers;
	}

	public void setTrade(TradeEntity trade) {
		this.trade = trade;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
