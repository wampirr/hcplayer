package com.jr.structure.dao;

import com.jr.structure.model.Crit;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritRepositoryFile implements CritRepository { //todo

    @Override
    public Crit getOne(Long aLong) {
        return null;
    }

    @Override
    public List<Crit> findAll() {
        return null;
    }

    @Override
    public List<Crit> findAll(Iterable<Long> id) {
        return null;
    }

    public Crit getByName(String name) {
        return null;
    }

    @Override
    public boolean save(Crit item) {
        return false;
    }

    @Override
    public boolean save(Iterable<Crit> items) {
        return false;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(String name) {

    }

    @Override
    public void delete(Iterable<Crit> items) {

    }

    @Override
    public void delete(Crit item) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    //костыль просто стремнейший. должен генерить айди на основе самого большого айди
    public static long getIdForNewCrit() {
        return 0;
    }
}
