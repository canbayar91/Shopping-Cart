package com.trendyol;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.trendyol.delivery.DeliveryMethod;
import com.trendyol.discount.Campaign;
import com.trendyol.discount.Coupon;
import com.trendyol.discount.DiscountType;
import com.trendyol.product.Category;
import com.trendyol.product.CategoryInfoHolder;
import com.trendyol.product.Product;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Contains all the logic of the application
 */
public class ShoppingCart {
	
	// Epsilon value for double precision comparison
	public static final double EPSILON = 0.001;
	
	// Formatter for outputting currency values
	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	// Formatter for products and price
	private static String productFormat = "%-25s%-10sx%-5d%s%n";
	private static String priceFormat = "%-20s%s%n";
	
	// Stores the count of each added product in the cart
	// Note: Keys could be String (product title) but instead I overridden the @{hashCode} method of Product
	private Map<Product, Integer> cart = new HashMap<Product, Integer>();

	// A fast lookup table for category - product mapping
	// I sacrificed storage in order to speed up the price calculations
	// In either case, neither adding items nor price calculation methods will receive too many calls
	// Then again, the number of items in the card will not be very big so it shouldn't really matter
	private Map<Category, CategoryInfoHolder> categoryMapping = new HashMap<Category, CategoryInfoHolder>();
	
	// List of applied campaigns
	private List<Campaign> appliedCampaigns = new ArrayList<Campaign>();

	// Applied coupon if one exists
	// Note: I understood only a single coupon can be applied to the cart from description
	private Coupon coupon;
	
	// Delivery price calculation method of preference
	private DeliveryMethod deliveryMethod;
	
	// Shortcut to the total price of all items in the cart
	private double totalPrice = 0;
	
	/*
	 * Note: I had to pass the calculator and break the given interface rules on the pseudocode in this method
	 * Passing it on the calculation method would be my preference but print method needed it, too
	 * It could be initialized through field injection using Spring Framework
	 */
	public ShoppingCart(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	
	/**
	 * Adds the given amount of a product into the shopping cart
	 * 
	 * @param product the product
	 * @param count the count of product
	 */
	public void addItem(Product product, int count) {
		
		// Make sure the parameters are valid
		if (product != null && count > 0) {
			
			// Updates the count of the existing product or adds it to the cart
			if (cart.containsKey(product)) {
				int totalCount = cart.get(product) + count;
				cart.replace(product, totalCount);
			} else {
				cart.put(product, count);
				updateLookupTable(product, count);
			}
			
			// Update the total cart price
			double productPrice = product.getPrice();
			totalPrice += productPrice * count;
		}
	}
	
	/**
	 * Adds the new product information into consequent categories
	 * 
	 * @param product the product
	 */
	private void updateLookupTable(Product product, int count) {
		
		// If the lookup table does not contain the category, add it first
		Category category = product.getCategory();
		do {
			
			if (!categoryMapping.containsKey(category)) {
				CategoryInfoHolder categoryInfoHolder = new CategoryInfoHolder();
				categoryMapping.put(category, categoryInfoHolder);
			}
			
			// Add the product into the lookup table
			categoryMapping.get(category).update(product, count);
			
			// The block ends when there are no more parents
			category = category.getParent();
			
		} while (category != null);
	}

	/**
	 * Adds all applied campaigns into the campaign list
	 * 
	 * @param campaigns the list of applied campaigns
	 */
	public void applyDiscounts(Campaign... campaigns) {
		
		// Add all campaigns into the list
		for (Campaign campaign : campaigns) {
			
			// Skip the invalid campaigns
			if (campaign != null) {
				appliedCampaigns.add(campaign);
			}
		}
	}
	
	/**
	 * Sets the coupon
	 * 
	 * @param coupon the coupon
	 */
	public void applyCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	
	/**
	 * Returns the total cart price (added just in case)
	 * 
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Return the total price after all discounts are applied
	 * 
	 * @return the price
	 */
	public double getTotalAmountAfterDiscounts() {
		return totalPrice - getCampaignDiscount() - getCouponDiscount();
	}
	
	/**
	 * Return the coupon discount amount
	 * 
	 * @return the discount
	 */
	public double getCouponDiscount() {
		
		// Only apply if a coupon is applied
		if (coupon != null) {
			
			// Check coupon validity
			double priceAfterCampaigns = totalPrice - getCampaignDiscount();
			double minPriceTotal = coupon.getMinPriceTotal();
			if (priceAfterCampaigns >= minPriceTotal || Math.abs(priceAfterCampaigns - minPriceTotal) < EPSILON) {
				
				// For DiscountType.RATE, calculate the discount by percentage 
				DiscountType discountType = coupon.getDiscountType();
				if (discountType == DiscountType.RATE) {
					return priceAfterCampaigns * coupon.getDiscount() / 100;
				}
				
				// Otherwise, return the discount amount directly
				return coupon.getDiscount();
			}
		}
		
		// No discount will be applied
		return 0;
	}
	
	/**
	 * Return the total campaign discount amount
	 * 
	 * @return the discount
	 */
	public double getCampaignDiscount() {
		
		// Each campaign will be applied individually to calculate the total discount
		double totalDiscount = 0;
		for (Campaign campaign : appliedCampaigns) {
			
			// Campaign will be applied only when there are items on the corresponding category
			// Note: I ignored the overlaps on the same/parent categories
			Category category = campaign.getCategory();
			if (categoryMapping.containsKey(category)) {
				
				// If there are enough products of the given category, calculate the discount amount
				CategoryInfoHolder holder = categoryMapping.get(category);
				if (holder.getProductCount() > campaign.getMinItemCount()) {
					
					// Discount amount is depending on the discount type
					if (campaign.getDiscountType() == DiscountType.RATE) {
						double categoryTotal = holder.getTotalPrice();
						totalDiscount += categoryTotal * campaign.getDiscount() / 100;
					} else {
						totalDiscount += campaign.getDiscount();
					}
				}
			}
		}
		
		// Return the total discount
		return totalDiscount;
	}
	
	/**
	 * Calculates the delivery cost for the current state of cart
	 * 
	 * @return the delivery cost
	 */
	public double getDeliveryCost() {
		
		// Return 0, if no delivery method is specified
		if (deliveryMethod == null) {
			return 0;
		}
		
		// Calculate and return the delivery cost
		return deliveryMethod.calculateFor(this);
	}
	
	/**
	 * Print out the cart information
	 */
	public void print() {
		
		// If the cart is empty, return without printing information
		if (isEmpty()) {
			System.out.println("Your cart is empty.");
			return;
		}
		
		// Categorize the products using streams
		Map<Category, List<Product>> categoriedProducts = cart.keySet().stream().collect(Collectors.groupingBy(p -> p.getCategory()));
		for (Category category : categoriedProducts.keySet()) {
		
			// Output the category name
			System.out.println(category.getTitle() + ":");
			System.out.println("------------------------------");
			
			// Output each product under that category
			List<Product> productList = categoriedProducts.get(category);
			for (Product product : productList) {
			
				// Acquire product information
				String title = product.getTitle();
				int count = cart.get(product);
				double price = product.getPrice();
				double total = price * count;
				
				// Output product information
				System.out.printf(productFormat, title, currencyFormat.format(price), count, currencyFormat.format(total));
			}
			
			// Add extra space after each category
			System.out.println();
		}
		
		// Output total price
		System.out.printf(priceFormat, "Total Price: ", currencyFormat.format(totalPrice));
		
		// Output total campaign discount
		double campaignDiscount = getCampaignDiscount();
		if (campaignDiscount > 0) {
			System.out.printf(priceFormat, "Campaign Discount: ", currencyFormat.format(campaignDiscount));
		}

		// Output coupon discount
		double couponDiscount = getCouponDiscount();
		if (couponDiscount > 0) {
			System.out.printf(priceFormat, "Coupon Discount: ", currencyFormat.format(couponDiscount));
		}
		
		// Add extra space
		System.out.println();
		
		// Calculate the final price
		double shippingPrice = getDeliveryCost();
		double totalPrice = getTotalAmountAfterDiscounts();
		double finalPrice = totalPrice + shippingPrice;
		
		// Output shipping and final price
		System.out.printf(priceFormat, "Shipping Price: ", currencyFormat.format(shippingPrice));
		System.out.printf(priceFormat, "Final Price: ", currencyFormat.format(finalPrice));
	}
	
	/**
	 * Returns the number of distinct categories in the cart
	 * 
	 * @return count of categories
	 */
	public int deliveryCount() {
		
		// Collect distinct categories from the products in the cart
		// Note: I assumed the parent categories do not affect this method
		Set<Category> categorySet = new HashSet<Category>();
		for (Product product : cart.keySet()) {
			Category category = product.getCategory();
			categorySet.add(category);
		}
		
		// Return the size of the set
		return categorySet.size();
	}
	
	/**
	 * Returns the number of products in the cart
	 * 
	 * @return count of products
	 */
	public int productCount() {
		return cart.size();
	}
	
	/**
	 * Checks if the cart is empty
	 * 
	 * @return empty status
	 */
	public boolean isEmpty() {
		return cart.size() == 0;
	}
}
