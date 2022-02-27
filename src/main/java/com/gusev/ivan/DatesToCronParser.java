package com.gusev.ivan;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parser for converting dates into cron-expression.
 */
public class DatesToCronParser implements DatesToCronConverter {

    private static final String AUTHOR_NAME = "Гусев Иван Евгеньевич";
    private static final String GITHUB_LINK = "https://github.com/IvanGusevGit/digital-gesign-test.git";

    @Override
    public String convert(List<String> dates) throws DatesToCronConvertException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        List<LocalDateTime> localDates;
        try {
            localDates = dates.stream().map(time -> LocalDateTime.parse(time, formatter))
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new DatesToCronConvertException();
        }
        String seconds = collectAttribute(localDates, LocalDateTime::getSecond, LocalDateTime.MAX.getSecond());
        String minutes = collectAttribute(localDates, LocalDateTime::getMinute, LocalDateTime.MAX.getMinute());
        String hours = collectAttribute(localDates, LocalDateTime::getHour, LocalDateTime.MAX.getHour());
        String dayOfMonth = collectAttribute(localDates, LocalDateTime::getDayOfMonth, LocalDateTime.MAX.getDayOfMonth());
        String month = collectAttribute(localDates, LocalDateTime::getMonthValue, LocalDateTime.MAX.getMonthValue());
        String dayOfWeek = collectAttribute(localDates, x -> x.getDayOfWeek().getValue(), DayOfWeek.SUNDAY.getValue());

        return String.join(" ", seconds, minutes, hours, dayOfMonth, month, dayOfWeek);
    }


    private String collectAttribute(List<LocalDateTime> dates, Function<LocalDateTime, Integer> mapper, int maxValue) {
        Set<Integer> attributes = dates.stream().map(mapper).collect(Collectors.toSet());
        if (attributes.size() == maxValue) {
            return "*";
        } else {
            return attributes.stream().map(Object::toString).collect(Collectors.joining(","));
        }
    }


    @Override
    public String getImplementationInfo() {
        Class<DatesToCronParser> clazz = DatesToCronParser.class;
        return String.join(", ", AUTHOR_NAME, clazz.getSimpleName(), clazz.getPackage().getName(), GITHUB_LINK);
    }
}
