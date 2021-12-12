package com.client.enums;

public enum MessageType {
//    Server Response
    PUBLIC_KEY,
    PRODUCT_INFO,
    PRODUCTS,
    CONFIGURABLE_PRODUCTS,
    PRODUCT_HISTORIES,
    PRODUCT_HISTORIES_BY_ID,
    CONFIGURABLE_PRODUCT_HISTORIES,
    REVIEWS,
    CATEGORIES,
    ADVANCE_PRODUCTS,
    TIMELINE_BY_REVIEWID,
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
    GET_ADVANCE_PRODUCTS,
    GET_TIMELINE_BY_REVIEWID,

//    Connection state
    USER_DISCONNECT
}
