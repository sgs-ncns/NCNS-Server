package dev.ncns.sns.common.util;

public class Topic {

    private static final String PREFIX = "NCNS-";

    private static final String CONSUMER_FEED = "FEED-";
    private static final String CONSUMER_SEARCH = "SEARCH-";

    private static final String PRODUCER_USER = "USER-";
    private static final String PRODUCER_POST = "POST-";

    private static final String EVENT_CREATE = "CREATE";
    private static final String EVENT_UPDATE = "UPDATE";
    private static final String EVENT_DELETE = "DELETE";

    private static final String FEED = PREFIX + CONSUMER_FEED;
    private static final String SEARCH = PREFIX + CONSUMER_SEARCH;

    public static final String FEED_USER_CREATE = FEED + PRODUCER_USER + EVENT_CREATE;
    public static final String FEED_USER_LIST_UPDATE = FEED + "LIST-" + EVENT_UPDATE;
    public static final String FEED_USER_DELETE = FEED + PRODUCER_USER + EVENT_DELETE;
    public static final String FEED_POST_UPDATE = FEED + PRODUCER_POST + EVENT_UPDATE;
    public static final String FEED_POST_LIKE_UPDATE = FEED + "LIKE-" + EVENT_UPDATE;

    public static final String SEARCH_USER_CREATE = SEARCH + PRODUCER_USER + EVENT_CREATE;
    public static final String SEARCH_USER_UPDATE = SEARCH + PRODUCER_USER + EVENT_UPDATE;
    public static final String SEARCH_USER_DELETE = SEARCH + PRODUCER_USER + EVENT_DELETE;
    public static final String SEARCH_POST_CREATE = SEARCH + PRODUCER_POST + EVENT_CREATE;
    public static final String SEARCH_POST_UPDATE = SEARCH + PRODUCER_POST + EVENT_UPDATE;
    public static final String SEARCH_POST_DELETE = SEARCH + PRODUCER_POST + EVENT_DELETE;

}
