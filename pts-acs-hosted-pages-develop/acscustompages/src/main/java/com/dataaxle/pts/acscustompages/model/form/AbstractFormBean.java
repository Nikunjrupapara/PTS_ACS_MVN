package com.dataaxle.pts.acscustompages.model.form;

@Deprecated
public abstract class AbstractFormBean<T> {

	protected final T subject;

	protected AbstractFormBean(T subject) {
		this.subject = subject;
	}

	public T getSubject() { return subject; }
}
