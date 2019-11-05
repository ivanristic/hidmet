package com.sargije.rest.hidmet.app.graphql.service.publishers;

import com.sargije.rest.hidmet.app.model.FivedayForecast;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FivedayForecastPublisher {
    private final Flowable<List<FivedayForecast>> publisher;

    private ObservableEmitter<List<FivedayForecast>> emitter;

    public FivedayForecastPublisher(){
        Observable<List<FivedayForecast>> currentForecastObservable = Observable.create(emitter -> {this.emitter = emitter;});
        ConnectableObservable<List<FivedayForecast>> connectableObservable = currentForecastObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void publish(final List<FivedayForecast> fivedayForecast){
        emitter.onNext(fivedayForecast);
    }

    public Flowable<List<FivedayForecast>> getPublisher() {
        return publisher;
    }
}
