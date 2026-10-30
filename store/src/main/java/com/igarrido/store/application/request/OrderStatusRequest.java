package com.igarrido.store.application.request;
import lombok.Data;
@Data public class OrderStatusRequest { private boolean ordered; private boolean prepared; private boolean paid; }
