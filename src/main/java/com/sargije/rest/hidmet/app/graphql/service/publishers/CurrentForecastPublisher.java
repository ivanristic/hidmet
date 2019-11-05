package com.sargije.rest.hidmet.app.graphql.service.publishers;

import com.sargije.rest.hidmet.app.model.CurrentForecast;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrentForecastPublisher {
    private final Flowable<List<CurrentForecast>> publisher;

    private ObservableEmitter<List<CurrentForecast>> emitter;

    public CurrentForecastPublisher(){
        Observable<List<CurrentForecast>> currentForecastObservable = Observable.create(emitter -> {this.emitter = emitter;});
        ConnectableObservable<List<CurrentForecast>> connectableObservable = currentForecastObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void publish(final List<CurrentForecast> currentForecasts){
        emitter.onNext(currentForecasts);
    }

    public Flowable<List<CurrentForecast>> getPublisher() {
        return publisher;
    }
}
