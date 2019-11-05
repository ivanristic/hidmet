package com.sargije.rest.hidmet.app.graphql.service.publishers;

import com.sargije.rest.hidmet.app.model.AirQuality;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AirQualityPublisher {
    private final Flowable<List<AirQuality>> publisher;

    private ObservableEmitter<List<AirQuality>> emitter;

    public AirQualityPublisher(){
        Observable<List<AirQuality>> airQualityObservable = Observable.create(emitter -> {this.emitter = emitter;});
        ConnectableObservable<List<AirQuality>> connectableObservable = airQualityObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void publish(final List<AirQuality> airQualities){
        emitter.onNext(airQualities);
    }

    public Flowable<List<AirQuality>> getPublisher() {
        return publisher;
    }
}
