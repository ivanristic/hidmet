package com.sargije.rest.hidmet.app.graphql.service.publishers;

import com.sargije.rest.hidmet.app.model.ShortTermForecast;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShortTermForecastPublisher {
    private final Flowable<List<ShortTermForecast>> publisher;

    private ObservableEmitter<List<ShortTermForecast>> emitter;

    public ShortTermForecastPublisher(){
        Observable<List<ShortTermForecast>> currentForecastObservable = Observable.create(emitter -> {this.emitter = emitter;});
        ConnectableObservable<List<ShortTermForecast>> connectableObservable = currentForecastObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void publish(final List<ShortTermForecast> shortTermForecast){
        emitter.onNext(shortTermForecast);
    }

    public Flowable<List<ShortTermForecast>> getPublisher() {
        return publisher;
    }
}
