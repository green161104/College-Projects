import {
  PayPalButtons,
  PayPalButtonsComponentProps,
  PayPalScriptProvider,
  ReactPayPalScriptOptions,
} from "@paypal/react-paypal-js";
import {
  completeOrderServer,
  createOrderServer,
} from "../../services/http/payment/payment-service";
import { useState } from "react";

const CreateOrderButton = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [statusMessage, setStatusMessage] = useState<string>("");

  const initialOptions: ReactPayPalScriptOptions = {
    clientId:
      "Aeg1Hg9AllR0YbLMxZ5rMqMaxtTbTC5PjO0Il8h0vdEhJR_0PsxZe-WV6dUw-_DNs8nLPgyrV1iqSwip",
    currency: "EUR",
  };

  const createOrder: PayPalButtonsComponentProps["createOrder"] = async () => {
    setLoading(true);
    setStatusMessage("Creating PayPal order...");
    try {
      const orderData = await createOrderServer(10); // Adjust amount as needed

      if (!orderData.id) {
        throw new Error("Order ID not found!");
      }

      setStatusMessage("Order created successfully! Proceeding to PayPal...");
      return orderData.id;
    } catch (error) {
      setStatusMessage("Error creating PayPal order. Please try again.");
      console.error(error);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const onApprove: PayPalButtonsComponentProps["onApprove"] = async (data) => {
    if (!data.orderID) {
      setStatusMessage(
        "Order ID is missing. Unable to complete payment. Happened on approve."
      );
      return;
    }

    setLoading(true);
    setStatusMessage("Completing PayPal order...");
    try {
      await completeOrderServer(data.orderID);

      setStatusMessage("Payment successful! Thank you for your purchase.");
    } catch (error) {
      setStatusMessage(
        "Error completing PayPal order. Please contact support."
      );
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const onCancel = () => {
    setStatusMessage("Payment process was cancelled.");
  };

  const onError = (err: any) => {
    setStatusMessage("An error occurred during the payment process.");
    console.error("PayPal error:", err);
  };

  return (
    <div className="CreateOrderButton">
      <PayPalScriptProvider options={initialOptions}>
        <div style={{ marginBottom: "20px" }}>
          {loading && <p>Loading... Please wait.</p>}
          {!loading && statusMessage && <p>{statusMessage}</p>}
        </div>
        <PayPalButtons
          createOrder={createOrder}
          onApprove={onApprove}
          onCancel={onCancel}
          onError={onError}
        />
      </PayPalScriptProvider>
    </div>
  );
};

export default CreateOrderButton;
