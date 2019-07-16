package ru.ele638.testtranslate.Helpers;

import io.reactivex.Scheduler;

public interface BaseSchedulerProvider {
    Scheduler io();

    Scheduler computation();

    Scheduler ui();
}
