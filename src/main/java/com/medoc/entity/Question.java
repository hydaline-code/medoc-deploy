package com.medoc.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "questions")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "question")
	private String questionContent;

	private String answer;

	@Column(name = "ask_time")
	private Date askTime;

	@Column(name = "answer_time")
	private Date answerTime;

	@ManyToOne
	@JoinColumn(name = "ordo_id")
	private Ordo ordo;

	@ManyToOne
	@JoinColumn(name = "answerer_id")
	private User answerer;

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String question) {
		this.questionContent = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public User getAnswerer() {
		return answerer;
	}

	public void setAnswerer(User answerer) {
		this.answerer = answerer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ordo getOrdo() {
		return ordo;
	}

	public void setOrdo(Ordo ordo) {
		this.ordo = ordo;
	}

	public Date getAskTime() {
		return askTime;
	}

	public void setAskTime(Date askTime) {
		this.askTime = askTime;
	}

	@Transient
	public boolean isAnswered() {
		return this.answer != null && !answer.isEmpty();
	}

	@Transient
	public String getOrdoName() {
		return this.ordo.getShortDescription();
	}

//	
//	@Transient
//	public String getAskerFullName() {
//		return asker.getFirstName() + " " + asker.getLastName();
//	}
//	
	@Transient
	public String getAnswererFullName() {
		if (answerer != null) {
			return answerer.getFirstName() + " " + answerer.getLastName();
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", questionContent=" + questionContent + ", answer=" + answer + ", askTime="
				+ askTime + ", answerTime=" + answerTime + ", ordo=" + ordo + ", answerer=" + answerer + "]";
	}
	
	
}
