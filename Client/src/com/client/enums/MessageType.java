package com.client.enums;

public enum MessageType {
//    Server Response
    PUBLIC_KEY,
    PRODUCT_INFO,
    PRODUCTS,
    CONFIGURABLE_PRODUCTS,
    PRODUCT_HISTORIES,
    CONFIGURABLE_PRODUCT_HISTORIES,
    REVIEWS,
    CATEGORIES,
    ADVANCE_CATEGORIES,
    ERROR,

//    Client Request
    GET_PUBLIC_KEY,
    SEND_SECRET_KEY,
    GET_PRODUCT,
    FILTER_PRODUCTS,
    GET_CONFIGURABLE_PRODUCTS,
    GET_PRODUCT_HISTORIES_BY_URL,
    GET_PRODUCT_HISTORIES_BY_PRODUCT_ID,
    GET_CONFIGURABLE_PRODUCT_HISTORIES,
    GET_REVIEWS_BY_PRODUCT_ID,
    GET_CATEGORIES,
    GET_ADVANCE_CATEGORIES,

//    Connection state
    USER_DISCONNECT
}
