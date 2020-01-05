package com.imuliar.decima.service.util;

import java.util.regex.Pattern;

/**
 * <p>Store compiled regex matching patterns</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public class RegexPatterns {

    public static final Pattern SET_FREE_MATCHING_PATTERN =
            Pattern.compile("set_free:\\d{4}-\\d{2}-\\d{2}:\\d{4}-\\d{2}-\\d{2}");

    public static final Pattern BOOK_MATCHING_PATTERN =
            Pattern.compile("book#([0-9]{1,2}?[a-zA-Z])#((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])");

    public static final Pattern DROP_BOOKING_MATCHING_PATTERN =
            Pattern.compile("drop_booking:(\\w){2}");

    public static final Pattern DATE_MATCHING_PATTERN = Pattern.compile("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$");

    public static final Pattern ALPHANUMERIC_SLOT_NUMBER_PATTERN = Pattern.compile("([a-zA-Z0-9_-]){1,3}");

    public static final Pattern PERIOD_ID_PATTERN = Pattern.compile("periodId:\\d+");

    public static final Pattern PERIOD_REMOVE_PATTERN = Pattern.compile("remove_period_id:\\d+");

}
