package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.fade.IFade;
import org.jetbrains.annotations.NotNull;

class FadeInstance {

    private final IFade fade;

    private double elapsedTime = 0;
    private byte initialVolume = 0;
    private byte targetVolume = 100;

    FadeInstance(@NotNull IFade fade){
        this.fade = fade;
    }

    FadeInstance(@NotNull FadeInstance fadeInstance, @NotNull IFade fade){
        elapsedTime = fadeInstance.elapsedTime;
        initialVolume = fadeInstance.initialVolume;
        targetVolume = fadeInstance.targetVolume;
        this.fade = fade;
    }

    byte calculateVolume(double deltaTime){
        if (deltaTime < 0)
            throw new IllegalArgumentException("Delta time can not be negative");

        elapsedTime += deltaTime;
        byte volume = fade.calculateFadeVolume(initialVolume, targetVolume, elapsedTime);
        if (volume < 0)
            volume = 0;
        if (volume > 100)
            volume = 100;
        return volume;
    }

    double getElapsedTime() {
        return elapsedTime;
    }

    void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    byte getInitialVolume() {
        return initialVolume;
    }

    void setInitialVolume(byte initialVolume) {
        this.initialVolume = initialVolume;
    }

    byte getTargetVolume() {
        return targetVolume;
    }

    void setTargetVolume(byte targetVolume) {
        this.targetVolume = targetVolume;
    }

    public IFade getFade() {
        return fade;
    }

    boolean isDone(){
        return elapsedTime >= fade.getDurationInSeconds();
    }
}
