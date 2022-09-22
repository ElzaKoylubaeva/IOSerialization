package ru.koylubaevaem.netology.config;

public class Config {

    private Prop load;

    private Prop save;

    private Prop log;

    public Prop getLoad() {
        return load;
    }

    public void setLoad(Prop load) {
        this.load = load;
    }

    public Prop getSave() {
        return save;
    }

    public void setSave(Prop save) {
        this.save = save;
    }

    public Prop getLog() {
        return log;
    }

    public void setLog(Prop log) {
        this.log = log;
    }
}
