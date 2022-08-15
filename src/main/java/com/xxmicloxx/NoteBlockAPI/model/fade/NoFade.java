package com.xxmicloxx.NoteBlockAPI.model.fade;

public final class NoFade implements IFade {

    public static NoFade Instance = new NoFade();

    private NoFade(){}

    @Override
    public double getDurationInSeconds() {
        return 0;
    }

    @Override
    public byte calculateFadeVolume(byte initialVolume, byte targetVolume, double fadeTick) {
        return targetVolume;
    }
}
