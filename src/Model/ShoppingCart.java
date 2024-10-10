package model;

/**
 * This class represents a shopping cart item with a price and detail
 * description.
 */
public interface ShoppingCart {
	int getNoOfCopies();
	void setNoOfCopies(int noOfCopies);
	int getsoldCopies();
	double getPrice();
	String getDetail();
	void setSoldCopies(int soldCopies);
}