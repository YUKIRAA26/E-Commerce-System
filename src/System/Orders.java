package System;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Orders {
	private int order_id;
	private double total_amount;
	private String status;
	private LocalDateTime created_at;
	
	Orders(int order_id,double total_amount, String status, LocalDateTime created_at){
		this.order_id = order_id;
		this.total_amount = total_amount;
		this.status = status;
		this.created_at = created_at;
	}
	
	public int getOrder_id() {
		return order_id;
	}
	
	public double getTotalAmount() {
		return total_amount;
	}
	
	public String getStatus() {
		return status;
	}
	
	public LocalDateTime getCreatedAt() {
		return created_at;
	}
}	
