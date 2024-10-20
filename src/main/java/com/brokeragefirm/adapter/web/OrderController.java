package com.brokeragefirm.adapter.web;


import com.brokeragefirm.adapter.web.dto.CancelOrderResponse;
import com.brokeragefirm.application.port.input.OrderListingUseCase;
import com.brokeragefirm.application.port.input.OrderPlacingUseCase;
import com.brokeragefirm.application.port.input.command.PlaceOrderCommand;
import com.brokeragefirm.domain.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order", description = "Order API")
public class OrderController {

  private final OrderPlacingUseCase orderPlacingUseCase;
  private final OrderListingUseCase orderListingUseCase;

  @Operation(summary = "Place a new order")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Order placed successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> createOrder(@RequestBody PlaceOrderCommand placeOrderCommand) {
    orderPlacingUseCase.placeOrder(placeOrderCommand);
    return ResponseEntity.ok().build();
  }


  @Operation(summary = "Cancel an existing order")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Order canceled successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid order status", content = @Content),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
  @PutMapping("{orderId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable("orderId") UUID orderId) {
    log.info("REST request to cancel order with orderId: {}", orderId);
    return ResponseEntity.ok(orderPlacingUseCase.cancelOrder(orderId));
  }

  @Operation(summary = "Get all orders by customer ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
  @GetMapping("{customerId}")
  public ResponseEntity<List<Order>> getAllOrdersByCustomerId(@PathVariable("customerId") UUID customerId,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    log.info("REST request to get all orders by customerId: {}", customerId);
    return ResponseEntity.ok(orderListingUseCase.getAllOrdersByCustomerId(customerId, pageable));
  }
}
