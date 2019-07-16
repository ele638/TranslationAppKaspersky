package ru.ele638.testtranslate.Helpers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class TrampolineSchedulerProvider implements BaseSchedulerProvider {

    private TestScheduler scheduler;

    public TrampolineSchedulerProvider() {
    }

    TrampolineSchedulerProvider(TestScheduler testScheduler) {
        scheduler = testScheduler;
    }

    public Scheduler computation() {
        return scheduler == null ? Schedulers.trampoline() : scheduler;
    }

    public Scheduler ui() {
        return scheduler == null ? Schedulers.trampoline() : scheduler;
    }

    public Scheduler io() {
        return scheduler == null ? Schedulers.trampoline() : scheduler;
    }
}