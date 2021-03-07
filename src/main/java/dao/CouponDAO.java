package dao;

import entities.Coupon;

import java.util.List;

public interface CouponDAO {

	Coupon addCoupon(Coupon coupon);

	Coupon updateCoupon(Coupon coupon);

	Coupon deleteCoupon(long couponId);

	Coupon getOneCoupon(long couponId);

	List<Coupon> getAllCoupons();

	Coupon addCouponPurchase(long couponId, long customerId);

	Coupon deleteCouponPurchase(long couponId, long customerId);

}
