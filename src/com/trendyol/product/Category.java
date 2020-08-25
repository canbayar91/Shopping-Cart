package com.trendyol.product;

import java.util.Objects;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Each product has a category or a hierarchy of categories
 */
public class Category {

	// Name of the category - assuming it is unique and can be used as identifier
	private String title;
	
	// Parent category (can be empty)
	private Category parent;

	/*
	 * Constructor
	 */
	public Category(String title) {
		this.title = title;
	}

	/*
	 * Extended constructor with the parent category
	 * At this point, I had to make a decision whether to use Composite or Decorator design patterns
	 * I decided to use Composite pattern after reading this entry: https://stackoverflow.com/q/2233952/1663215
	 */
	public Category(String title, Category parent) {
		this.title = title;
		this.parent = parent;
	}

	/*
	 * Returns the title of the product
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * Returns the parent category, if one such exists
	 */
	public Category getParent() {
		return parent;
	}
	
	/*
	 * {@hashCode} method is overridden with the assumption of titles are unique
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	/*
	 * {@equals} method is overridden with the assumption of titles are unique
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Category)) {
			return false;
		}
		
		Category other = (Category) obj;
		return Objects.equals(title, other.getTitle());
	}
}
