package com.android.taitasciore.privaliatest.exception

/**
 * Custom exception signaling that a search did not return any results for the query
 */
class MovieQueryNotFoundException : Exception("Your query did not match any movies!")