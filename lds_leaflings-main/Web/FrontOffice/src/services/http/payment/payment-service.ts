import { MyLocalStorage } from "../../myLocalStorage";
import MyAPI from "../tokenInterceptor";

/**
 * Creates a PayPal order on the server.
 * 
 * This function communicates with the server to initiate the creation of a PayPal order with a given amount.
 * 
 * @param {number} amount - The amount for the PayPal order to be created.
 * 
 * @returns {Promise<any>} - The data returned by the server after the order creation.
 * @throws {Error} - Throws an error if the server request fails, logging the error message.
 */
export async function createOrderServer(amount: number) {
  try {
    const response = await MyAPI.post("/Paypal/create-order", { Amount: amount });

    return response.data; 
  } catch (error: any) {
    console.error("Error creating PayPal order, happened communicating with server create order:", error.response?.data || error.message);
    throw error;
  }
}

/**
 * Completes a PayPal order on the server.
 * 
 * This function communicates with the server to finalize a PayPal order by providing the `orderId` and `userId`.
 * 
 * @param {string} orderId - The ID of the order to be completed.
 * 
 * @returns {Promise<any>} - The data returned by the server after completing the order.
 * @throws {Error} - Throws an error if the server request fails, logging the error message.
 */
export async function completeOrderServer(orderId: string) {
  const userID = MyLocalStorage.getItem("userId")
  try {
    const response = await MyAPI.post("/Paypal/complete-order", {
      OrderID: orderId,
      UserID: userID,
    });
    return response.data;
  } catch (error: any) {
    console.error("Error completing PayPal order in the server:", error.response?.data || error.message);
    throw error;
  }
}
