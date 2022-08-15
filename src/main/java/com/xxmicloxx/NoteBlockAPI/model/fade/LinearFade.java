package com.xxmicloxx.NoteBlockAPI.model.fade;

public final class LinearFade implements IFade {

    private final double duration;

    public LinearFade(double duration){
        this.duration = duration;
    }

    @Override
    public double getDurationInSeconds() {
        return duration;
    }

    @Override
    public byte calculateFadeVolume(byte initialVolume, byte targetVolume, double time) {
        if (time > duration)
            return targetVolume;

        int difference = targetVolume - initialVolume;
        int volume = (int)(initialVolume + Math.round((difference / duration) * time));
        if (volume < 0)
            volume = 0;
        if (volume > 100)
            volume = 100;
        return (byte)volume;
    }
}
