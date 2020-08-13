package com.liu.study.mq.basis;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 14:30
 */
public class Desc {
    /**
     * MQ中的Topic和Tags。
     *      Topic：表示一类消息的集合，每个主题包含若干条消息，每条消息只能属于一个主题，是RocketMQ进行消息订阅的基本单位。
     *      Tag：为消息设置的标志，用于同一主题下区分不同类型的消息。
     *          来自同一业务单元的消息，可以根据不同业务目的在同一主题下设置不同标签。
     *          标签能够有效地保持代码的清晰度和连贯性，并优化RocketMQ提供的查询系统。
     *          消费者可以根据Tag实现对不同子主题的不同消费逻辑，实现更好的扩展性。
     */
}